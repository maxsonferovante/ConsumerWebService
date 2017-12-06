package com.mferovante.android.consumerwebservice;


import android.app.Activity;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class AddStudentActivity extends AppCompatActivity {
    private final String webServiceUrl = "http://172.16.2.235:5000/computing/api/v1.0/students";
    private ProgressDialog pDialog = null;

    private EditText editTextNanme;
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        editTextNanme = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(editTextNanme.getText().toString())){
                    Toast.makeText(getApplicationContext(), R.string.digite_seu_nome, Toast.LENGTH_SHORT).show();
                    editTextNanme.forceLayout();
                    return;
                }
                final RowItemStudent rowItemStudent = new RowItemStudent();
                rowItemStudent.setName(editTextNanme.getText().toString());
                // Check if Internet is working
                if (!isNetworkAvailable(getApplicationContext())) {
                    // Show a message to the user to check his Internet
                    Toast.makeText(getApplicationContext(), "Error while loading ... ", Toast.LENGTH_SHORT).show();
                } else {
                    pDialog.show();
                    // make HTTP request to retrieve the weather
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, webServiceUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("Response ---- ", response);
                                    editTextNanme.setText("");
                                    hidePDialog();
                                    Toast.makeText(getApplicationContext(), "Complete ... ", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
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
                        public Map<String, String> getHeaders(){
                            HashMap< String, String > headers = new HashMap < String, String > ();
                            // add headers <key,value>
                            String credentials = "mferovante"+":"+"d06fe49d20cb218e662fd0e034ef8387";
                            String auth = "Basic "
                                    + Base64.encodeToString(credentials.getBytes(),
                                    Base64.NO_WRAP);
                            headers.put("Authorization", auth);

                            return headers;
                        }
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("name", rowItemStudent.getName());
                            return new JSONObject(params).toString().getBytes();
                        }
                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };
                    // Adding request to request queue
                    AppController.getInstance(AddStudentActivity.this).add(stringRequest);
                }
            }
        });
    }

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

    @Override
    public void onBackPressed() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            //Se a atividade não faz parte do aplicativo, criamos uma nova tarefa
            // para navegação com a pilha de volta sintetizada.
            TaskStackBuilder.create(this)
                    // Adiciona todas atividades parentes na pilha de volta
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            //Se essa atividade faz parte da tarefa do app
            //navegamos para seu parente logico.
            NavUtils.navigateUpTo(this, upIntent);
        }
    }
}
