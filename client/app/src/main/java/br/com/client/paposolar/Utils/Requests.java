package br.com.client.paposolar.Utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by AndreLucas on 13/12/2017.
 */

public class Requests {
    private static Requests mInstance;
    private RequestQueue mQueue;
    public static final String ROOT = "http://35.229.119.213/paposolar";

    private Requests(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public static Requests getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Requests(context);
        }

        return mInstance;
    }

    public void getObject(String url, Response.Listener<JSONObject> callback, Response.ErrorListener error) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ROOT+url, null, callback, error);
        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }

    public void post(String url, JSONObject data, Response.Listener<JSONObject> callback, Response.ErrorListener error) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ROOT+url, data, callback, error);
        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }
}
