package br.com.client.paposolar.Services;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.client.paposolar.Utils.Requests;

/**
 * Created by AndreLucas on 25/01/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferences = getSharedPreferences("token",0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("token", String.valueOf(refreshedToken));
        edit.commit();
    }
}
