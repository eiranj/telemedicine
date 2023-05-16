package com.example.telemedicineui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Appoint extends AppCompatActivity {

    private static Button btnQuery;
    private static EditText nme, idno, em, con, msg;
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.42.236/telemed/InsertTrans.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String name = "";
    public static String id = "";
    public static String email = "";
    public static String contact = "";
    public static String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint);

        btnQuery = (Button) findViewById(R.id.btnQuery);
        nme = (EditText) findViewById(R.id.edname);
        idno = (EditText) findViewById(R.id.edidnum);
        em = (EditText) findViewById(R.id.edemail);
        con = (EditText) findViewById(R.id.edcontact);
        msg = (EditText) findViewById(R.id.edmsg);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nme.getText().toString();
                id = idno.getText().toString();
                email = em.getText().toString();
                contact = con.getText().toString();
                message = msg.getText().toString();
                new uploadDatatoURL().execute();
            }
        });

    }
    private class uploadDatatoURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(Appoint.this);

        public uploadDatatoURL() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params){
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = " '" + id + "' , '" + name + "' , '" + contact + "' , '" + email + "' , '" + message + "' ";
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(Appoint.this);
            if (s !=null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(Appoint.this, s, Toast.LENGTH_SHORT).show();
            }else{
                alert.setMessage("Query Interrupted ... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }

    }
}