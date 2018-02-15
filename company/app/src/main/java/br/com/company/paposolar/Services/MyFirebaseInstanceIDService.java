package br.com.company.paposolar.Services;

import android.content.SharedPreferences;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.company.paposolar.Utils.Requests;

/**
 * Created by AndreLucas on 25/01/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Requests r = Requests.getInstance(getApplicationContext());
        JSONObject j = new JSONObject();
        try {
            j.put("token",String.valueOf(refreshedToken));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        r.post("/token", j, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
