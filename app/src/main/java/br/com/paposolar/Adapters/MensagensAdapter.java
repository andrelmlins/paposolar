package br.com.paposolar.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import br.com.paposolar.Beans.Mensagem;
import br.com.paposolar.Beans.Status;
import br.com.paposolar.Beans.UserType;
import br.com.paposolar.R;

/**
 * Created by AndreLucas on 13/12/2017.
 */

public class MensagensAdapter extends BaseAdapter {
    private ArrayList<Mensagem> chatMessages;
    private Context context;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a", Locale.US);

    public MensagensAdapter(ArrayList<Mensagem> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        Mensagem message = chatMessages.get(position);
        ViewHolder1 holder1;
        ViewHolder2 holder2;

        if (message.getUserType() == UserType.SELF) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.mensagem1, null, false);
                holder1 = new ViewHolder1();


                holder1.messageTextView = (TextView) v.findViewById(R.id.textview_message);
                holder1.timeTextView = (TextView) v.findViewById(R.id.textview_time);
                holder1.nomeTextView = (TextView) v.findViewById(R.id.textview_nome);

                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();
            }

            holder1.messageTextView.setText(message.getMessageText());
            holder1.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            holder1.nomeTextView.setText(message.getNomeText());

        } else if (message.getUserType() == UserType.OTHER) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.mensagem2, null, false);

                holder2 = new ViewHolder2();

                holder2.messageTextView = (TextView) v.findViewById(R.id.textview_message);
                holder2.timeTextView = (TextView) v.findViewById(R.id.textview_time);
                holder2.messageStatus = (ImageView) v.findViewById(R.id.user_reply_status);
                holder2.nomeTextView = (TextView) v.findViewById(R.id.textview_nome);
                v.setTag(holder2);

            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();
            }

            holder2.messageTextView.setText(message.getMessageText());
            //holder2.messageTextView.setText(message.getMessageText());
            holder2.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            holder2.nomeTextView.setText(message.getNomeText());

            if (message.getMessageStatus() == Status.DELIVERED) {
                holder2.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_receipt_from_target));
            } else if (message.getMessageStatus() == Status.SENT) {
                holder2.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_receipt_from_server));
            } else if (message.getMessageStatus() == Status.WAITING) {
                holder2.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clock));
            }
        }
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem message = chatMessages.get(position);
        return message.getUserType().ordinal();
    }

    private class ViewHolder1 {
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView nomeTextView;
    }

    private class ViewHolder2 {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView nomeTextView;
    }
}
