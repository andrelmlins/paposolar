package br.com.client.paposolar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.client.paposolar.Beans.Conversa;
import br.com.client.paposolar.Utils.Requests;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

    private EditText email;
    private EditText nome;
    private Button entrar;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = getSharedPreferences("usuario",0);
        if (!preferences.getString("nome", "").equals("")) {
            Conversa c = new Conversa("Papo Solar", "contato@paposolar.com","1");
            Bundle b = new Bundle();
            b.putSerializable("conversa",c);
            Intent intent = new Intent(this, ConversaActivity.class);
            intent.putExtras(b);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        this.entrar = (Button) findViewById(R.id.entrar);
        this.email = (EditText) findViewById(R.id.email);
        this.nome = (EditText) findViewById(R.id.nome);
        entrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences settings = getSharedPreferences("token",0);
        Requests r = Requests.getInstance(this);
        JSONObject j = new JSONObject();
        try {
            j.put("nome",String.valueOf(this.nome.getText()));
            j.put("email",String.valueOf(this.email.getText()));
            j.put("token",settings.getString("token",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.progress = ProgressDialog.show(this, "","Cadastrando Usuário...", true);
        r.post("/login",j,this,this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Erro de conexão!!!", Toast.LENGTH_SHORT).show();
        this.progress.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        SharedPreferences settings = getSharedPreferences("usuario",0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("nome", String.valueOf(this.nome.getText()));
        edit.putString("email", String.valueOf(this.email.getText()));
        try {
            edit.putString("id", response.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        edit.commit();
        if(this.progress!=null) this.progress.dismiss();

        Conversa c = new Conversa("Papo Solar", "contato@paposolar.com","1");
        Bundle b = new Bundle();
        b.putSerializable("conversa",c);
        Intent intent = new Intent(this, ConversaActivity.class);
        intent.putExtras(b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
