package com.mferovante.android.consumerwebservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String webServiceUrl = "http://192.168.1.6:5000/computing/api/v1.0/students";
    private ProgressDialog pDialog = null;
    private JSONArray jsonArray = null;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);

        // prepare the loading Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        // Check if Internet is working
        if (!isNetworkAvailable(this)) {
            // Show a message to the user to check his Internet
            Toast.makeText(this, "Please check your Internet connection", Toast.LENGTH_LONG).show();
        } else {
            hidePDialog();
            // make HTTP request to retrieve the weather
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webServiceUrl,
                    new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    new ParseJSonDataClass(MainActivity.this, response).execute();
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("tag", "Error: " + error.getMessage());
                    Log.i("CRL", error.getMessage());
                    Toast.makeText(getApplicationContext(), "Error while loading ... ", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    hidePDialog();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap < String, String > headers = new HashMap < String, String > ();
                    // add headers <key,value>

                    String credentials = "mferovante"+":"+"d06fe49d20cb218e662fd0e034ef8387";

                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);

                    headers.put("Authorization", auth);

                    return headers;
                }
            };
            // Adding request to request queue
            AppController.getInstance(this).add(jsonArrayRequest);
        }

    }

    ////////////////////check internet connection
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    // Creating method to parse JSON object.
    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {

        public Context context;
        private List<RowItemStudent> rowItemStudentList = null;
        private JSONArray response;

        public  ParseJSonDataClass(Context context, JSONArray response){
            this.context = context;
            this.response = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            rowItemStudentList = new ArrayList<RowItemStudent>();
            for (int i = 0; i < this.response.length(); i++) {
                try {
                    rowItemStudentList.add (
                            new RowItemStudent(
                                    i,
                                    (this.response.getJSONObject(i)).getString("name")
                            )
                    );
                    Log.i("element ", "- "+this.response.getJSONObject(i).getJSONArray("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error , try again ! ", Toast.LENGTH_LONG).show();
                    hidePDialog();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            CustomListViewAdapter customListViewAdapter = new CustomListViewAdapter(
                    MainActivity.this,
                    rowItemStudentList
            );
            listView.setAdapter(customListViewAdapter);
            hidePDialog();
        }
    }
}



