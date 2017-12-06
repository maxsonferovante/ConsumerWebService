package com.mferovante.android.consumerwebservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String webServiceUrl = "http://172.16.2.235:5000/computing/api/v1.0/students";
    private ProgressDialog pDialog = null;
    private JSONArray jsonArray = null;
    private ListView listView;
    private List<RowItemStudent> rowItemStudentList = null;
    private CustomListViewAdapter customListViewAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rowItemStudentList = new ArrayList<RowItemStudent>();

        customListViewAdapter = new CustomListViewAdapter(
                MainActivity.this,
                rowItemStudentList
        );
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(customListViewAdapter);

        // prepare the loading Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }
    @Override
    protected void onResume() {
        connectServe();
        super.onResume();
    }
    private void connectServe() {
        // Check if Internet is working
        if (!isNetworkAvailable(this)) {
            // Show a message to the user to check his Internet
            Toast.makeText(this, "Please check your Internet connection", Toast.LENGTH_LONG).show();
        } else {
            pDialog.show();
            // make HTTP request to retrieve the weather
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webServiceUrl,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            rowItemStudentList = RowItemStudent.fromJson(response);
                            customListViewAdapter.clear();
                            customListViewAdapter.addAll(rowItemStudentList);
                            hidePDialog();
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
                    HashMap< String, String > headers = new HashMap < String, String > ();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_insert:{
                startActivity(new Intent(MainActivity.this, AddStudentActivity.class));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }
    ////////////////////check internet connection
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    protected void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}



