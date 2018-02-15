package br.com.company.paposolar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.company.paposolar.Adapters.ConversaAdapter;
import br.com.company.paposolar.Beans.Conversa;
import br.com.company.paposolar.Utils.Requests;

public class Conversas extends AppCompatActivity implements AdapterView.OnItemClickListener, Response.Listener<JSONObject>, Response.ErrorListener, SearchView.OnQueryTextListener  {

    public ListView list;
    public ArrayList<Conversa> conversas;
    public ConversaAdapter adapter;
    private Requests r;
    private ProgressDialog progress;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.r = Requests.getInstance(this);
        this.preferences = getSharedPreferences("usuario",0);
        if (!preferences.getString("nome", "").equals("")) {
            SharedPreferences.Editor edit = this.preferences.edit();
            edit.putString("nome", "Papo Solar");
            edit.putString("email", "contato@paposolar.com");
            edit.putString("id", "1");
            edit.commit();
        }
        this.preferences = getSharedPreferences("usuario",0);

        this.progress = ProgressDialog.show(this, "","Carregando Conversas...", true);
        this.r.getObject("/conversas/1", this, this);

        this.conversas = new ArrayList<Conversa>();

        list = (ListView) findViewById(R.id.lista);
        list.setOnItemClickListener(this);
        adapter = new ConversaAdapter(conversas, this);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_lista, menu);

        MenuItem mSearchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        } else if(id == R.id.refresh){
            this.progress = ProgressDialog.show(this, "","Carregando Mensagens...", true);
            this.r.getObject("/conversas/"+preferences.getString("id", ""), this, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Conversa c = this.conversas.get(i);
        Bundle b = new Bundle();
        b.putSerializable("conversa",c);
        Intent intent = new Intent(this, ConversaActivity.class);
        intent.putExtras(b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(this.progress!=null) this.progress.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if(this.conversas.size()!=0){
                this.conversas.clear();
            }
            JSONArray conversas = response.getJSONArray("conversas");

            for (int i=0; i < conversas.length(); i++){
                JSONObject conversa = conversas.getJSONObject(i);
                this.conversas.add(new Conversa(conversa.getString("nome"),conversa.getString("email"),conversa.getString("id")));
            }
            this.adapter.edit();
            if(this.progress!=null) this.progress.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.adapter.filter(newText);
        return true;
    }
}
