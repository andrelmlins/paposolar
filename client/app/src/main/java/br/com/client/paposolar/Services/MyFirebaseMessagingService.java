package br.com.client.paposolar.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import br.com.client.paposolar.Beans.Conversa;
import br.com.client.paposolar.ConversaActivity;
import br.com.client.paposolar.R;

import static android.content.ContentValues.TAG;

/**
 * Created by AndreLucas on 25/01/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }
        JSONObject j = new JSONObject(remoteMessage.getData());

        NotificationCompat.Builder mBuilder =
                null;
        try {
            Conversa c = new Conversa(j.getString("nome"),j.getString("email"),j.getString("user_id"));
            Bundle b = new Bundle();
            b.putSerializable("conversa",c);
            Intent intent = new Intent(this, ConversaActivity.class);
            intent.putExtras(b);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_sun)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(j.getString("title"))
                    .setContentText(j.getString("body"))
                    .setGroup("paposolar")
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify = new Notification();
            notify.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_ONLY_ALERT_ONCE;
            Random random = new Random();
            mNotificationManager.notify(j.getInt("user_id"), mBuilder.build());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
