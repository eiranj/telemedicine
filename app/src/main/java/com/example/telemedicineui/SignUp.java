package com.example.telemedicineui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {
    private static Button btnQuery;
    private static EditText edname, edage, edadd, edemail, edpass;
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.0.105/ancuin/InsertTrans.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String name = "";
    public static String Gender = "";
    public static String age = "";
    public static String address = "";
    public static String email = "";
    public static String password = "";


    RadioButton male, female;
    View.OnClickListener MaleandFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        edname = (EditText) findViewById(R.id.name);
        edage = (EditText) findViewById(R.id.age);
        edemail = (EditText) findViewById(R.id.email);
        edpass = (EditText) findViewById(R.id.password);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edname.getText().toString();
                age = edage.getText().toString();
                address = edadd.getText().toString();
                email = edemail.getText().toString();
                password = edpass.getText().toString();
                new uploadDatatoURL().execute();
            }
        });
        MaleandFemale = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rdoList = (RadioButton) view;
                switch (rdoList.getId()) {
                    case R.id.male:
                        Gender = "Male";
                        break;
                    case R.id.female:
                        Gender = "Female";
                        break;
                }
            }
        };
        male.setOnClickListener(MaleandFemale);
        female.setOnClickListener(MaleandFemale);



}
    private class uploadDatatoURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(SignUp.this);

        public uploadDatatoURL(){}
        @Override
        protected  void onPreExecute(){
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
                cPostSQL = " '" + name + "' , '" + Gender + "' , '" + age + "' , '" + address + "' , '" + email + "' , '" + password + " '"  ;
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
            AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this);
            if (s !=null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(SignUp.this, s, Toast.LENGTH_SHORT).show();
            }else{
                alert.setMessage("Query Interrupted ... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}
