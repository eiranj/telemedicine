package com.example.telemedicineui;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Medicine extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static Button btnMed;
    private static EditText QtyMed, IdMed, NameMed;
    private static TextView TextViewMed;
    private static Spinner Medspinner;
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.254.106/telemed_mob/InsertMed.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    public static String qty = "";
    public static String id = "";
    public static String name = "";
    public static String medicinestr = "";
    public static String mdcn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        btnMed = (Button) findViewById(R.id.btnmed);
        QtyMed = (EditText) findViewById(R.id.qtymed);
        IdMed = (EditText) findViewById(R.id.idmed);
        NameMed = (EditText) findViewById(R.id.namemed);
        Medspinner = (Spinner) findViewById(R.id.spinner);
        TextViewMed = (TextView) findViewById(R.id.textViewMed);

        TextViewMed.setText(mdcn);


        Medspinner.setOnItemSelectedListener(this);

        List<String> medicine = new ArrayList<String>();
        medicine.add("Biogesic");
        medicine.add("Bioflu");
        medicine.add("Solmux");
        medicine.add("Neozep");
        medicine.add("Diatabs");
        medicine.add("Hydrite");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, medicine);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Medspinner.setAdapter(dataAdapter);

        btnMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = QtyMed.getText().toString();
                id = IdMed.getText().toString();
                name = NameMed.getText().toString();
                if (medicinestr.equals("medicine")){
                    medicinestr = mdcn;
                }

                new Medicine.uploadDatatoURL().execute();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        medicinestr = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class uploadDatatoURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(Medicine.this);

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
                cPostSQL = " '" + id + "' , '" + name + "' , '" + qty + "' , '" + medicinestr +"' ";
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
            AlertDialog.Builder alert = new AlertDialog.Builder(Medicine.this);
            if (s !=null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(Medicine.this, s, Toast.LENGTH_SHORT).show();
            }else{
                alert.setMessage("Query Interrupted ... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }

    }
}
message.txt
        5 KB