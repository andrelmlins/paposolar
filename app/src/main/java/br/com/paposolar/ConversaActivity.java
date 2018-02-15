package br.com.paposolar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import br.com.paposolar.Adapters.MensagensAdapter;
import br.com.paposolar.Beans.Conversa;
import br.com.paposolar.Beans.Mensagem;
import br.com.paposolar.Beans.Status;
import br.com.paposolar.Beans.UserType;
import br.com.paposolar.Utils.Requests;

public class ConversaActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, Emitter.Listener  {
    public static ListView chatListView;
    private EditText chatEditText1;
    public static ArrayList<Mensagem> chatMessages;
    private ImageView enterChatView1;
    public static MensagensAdapter listAdapter;
    private SharedPreferences preferences;
    private Requests r;
    private ProgressDialog progress;
    private Socket mSocket;
    private Conversa conversa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.r = Requests.getInstance(this);
        this.preferences = getSharedPreferences("usuario",0);
        this.conversa = (Conversa) getIntent().getExtras().get("conversa");
        this.conversa.setRoom(this.conversa.getId()+"_"+preferences.getString("id", ""));
        this.setTitle(this.conversa.getNomeUser());
        try {
            mSocket = IO.socket("http://35.184.178.70:2020");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on("send", this);
        mSocket.connect();
        mSocket.emit("subscribe", this.conversa.getRoom());

        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));

        this.progress = ProgressDialog.show(this, "","Carregando Mensagens...", true);

        chatMessages = new ArrayList<Mensagem>();
        chatListView = (ListView) findViewById(R.id.chat_list_view);

        chatEditText1 = (EditText) findViewById(R.id.chat_edit_text1);
        enterChatView1 = (ImageView) findViewById(R.id.enter_chat1);
        listAdapter = new MensagensAdapter(chatMessages, this);
        chatListView.setAdapter(listAdapter);
        chatEditText1.setOnKeyListener(keyListener);
        enterChatView1.setOnClickListener(clickListener);
        //chatEditText1.addTextChangedListener(watcher1);

        this.r.getObject("/mensagem/sala/"+this.conversa.getRoom(), this, this);
    }

    private Activity getActivity()
    {
        return this;
    }

    private void sendMessage(final String messageText, final UserType userType)
    {
        if(messageText.trim().length()==0)
            return;

        final Mensagem message = new Mensagem();
        message.setMessageStatus(Status.WAITING);
        message.setMessageText(messageText);
        message.setUserType(userType);
        message.setMessageTime(new Date().getTime());
        message.setNomeText(preferences.getString("nome", ""));

        chatMessages.add(message);

        JSONObject j = new JSONObject();
        try {
            j.put("nome",preferences.getString("nome", ""));
            j.put("texto",messageText);
            j.put("user_id",preferences.getString("id",""));
            j.put("data",message.getMessageTime());
            j.put("room",conversa.getRoom());
            j.put("user_receiver_id",conversa.getId());
            mSocket.emit("send",j);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(listAdapter!=null)
            listAdapter.notifyDataSetChanged();
    }

    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {

                EditText editText = (EditText) v;

                if(v==chatEditText1)
                {
                    sendMessage(editText.getText().toString(), UserType.SELF);
                }

                chatEditText1.setText("");

                return true;
            }
            return false;

        }
    };

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v==enterChatView1)
            {
                sendMessage(chatEditText1.getText().toString(), UserType.OTHER);
            }

            chatEditText1.setText("");

        }
    };

    @Override
    public void onErrorResponse(VolleyError error) {
        if(progress!=null) this.progress.dismiss();
    }

    public void onResponse(JSONObject response) {
        try {
            JSONArray mensagens = response.getJSONArray("mensagens");
            int i=0;
            for(i=0; i<mensagens.length(); i++){
                JSONObject j = mensagens.getJSONObject(i);
                Mensagem m = new Mensagem();
                m.setNomeText(j.getString("nome"));
                m.setMessageText(j.getString("texto"));
                m.setMessageTime(Long.parseLong(j.getString("data")));
                m.setId(Long.parseLong(j.getString("id")));
                if(j.getString("email").equals(preferences.getString("email", ""))){
                    m.setUserType(UserType.OTHER);
                } else {
                    m.setUserType(UserType.SELF);
                }
                m.setMessageStatus(Status.SENT);
                this.chatMessages.add(m);
                this.chatListView.setSelection(this.listAdapter.getCount()-1);
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        if(progress!=null) progress.dismiss();

    }

    @Override
    public void call(final Object... args) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                try {
                    if(preferences.getString("id", "").equals(data.getString("user_id")) || preferences.getString("id", "").equals(data.getString("user_receiver_id"))) {
                        if (preferences.getString("id", "").equals(data.getString("user_id"))) {
                            for (int i = 0; i < chatMessages.size(); i++) {
                                Mensagem m = chatMessages.get(i);
                                if (m.getMessageTime() == Long.parseLong(data.getString("dataOld"))) {
                                    m.setMessageStatus(Status.SENT);
                                    chatMessages.set(i, m);
                                    break;
                                }
                            }
                        } else {
                            Mensagem m = new Mensagem();
                            m.setNomeText(data.getString("nome"));
                            m.setMessageText(data.getString("texto"));
                            m.setMessageTime(Long.parseLong(data.getString("data")));
                            m.setUserType(UserType.SELF);
                            chatMessages.add(m);
                        }
                    }
                } catch (JSONException e) {
                    return;
                }
                if(listAdapter!=null) listAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
