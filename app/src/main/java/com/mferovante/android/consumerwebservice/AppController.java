package com.mferovante.android.consumerwebservice;

/**
 * Created by marx on 04/10/17.
 */


import android.app.Activity;
import android.content.Context;
import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController {

    public static final String TAG = AppController.class.getSimpleName();

    private static RequestQueue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private AppController(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    // if an instance is already create , it will return it . if no instance was created , it will create a new one then reurn it
    public static synchronized RequestQueue getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = Volley.newRequestQueue(activity.getApplicationContext());
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueueWithTag(Request<T> request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}