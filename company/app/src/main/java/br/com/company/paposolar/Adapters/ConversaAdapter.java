package br.com.company.paposolar.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import br.com.company.paposolar.Beans.Conversa;
import br.com.company.paposolar.R;

/**
 * Created by AndreLucas on 13/12/2017.
 */

public class ConversaAdapter extends BaseAdapter {
    private ArrayList<Conversa> conversas;
    private ArrayList<Conversa> conversasFilter;
    private Activity activity;

    public ConversaAdapter(ArrayList<Conversa> conversas, Activity activity) {
        this.conversas = conversas;
        this.activity = activity;
        this.conversasFilter = new ArrayList<>();
        this.conversasFilter.addAll(this.conversas);
    }

    @Override
    public int getCount() {
        return conversas.size();
    }

    @Override
    public Object getItem(int i) {
        return conversas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View cview, ViewGroup viewGroup) {
        Conversa conversa = conversas.get(i);

        View view = activity.getLayoutInflater().inflate(R.layout.item_conversa, viewGroup, false);
        TextView nome = (TextView) view.findViewById(R.id.nome);
        TextView email = (TextView) view.findViewById(R.id.email);

        nome.setText(conversa.getNomeUser());
        email.setText(conversa.getEmailUser());

        return view;
    }

    public void edit(){
        this.conversasFilter.clear();
        this.conversasFilter.addAll(this.conversas);
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        this.conversas.clear();
        if (charText.length() == 0) {
            this.conversas.addAll(this.conversasFilter);
        } else {
            for (Conversa c : this.conversasFilter) {
                if (c.getNomeUser().toLowerCase(Locale.getDefault()).contains(charText) ||
                        c.getEmailUser().toLowerCase(Locale.getDefault()).contains(charText)){
                    this.conversas.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }
}
