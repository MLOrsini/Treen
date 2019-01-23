package com.treen.charlotte.treeanapp.vue;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.treen.charlotte.treeanapp.R;
import com.treen.charlotte.treeanapp.controleur.Controle;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String product = new String();
    int responseStatus = 0;
    String jsonData = new String();
    int idVille;
    List<String> listeVilles = new ArrayList<String>();
    String ville;

    private Spinner spinnerVille;
    private TextView txtVille;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_main);
        getListVille();


        this.controle = Controle.getInstance();
        // ecouteScan();
        ecouteTriVille();

        spinnerVille = (Spinner) findViewById(R.id.spinnerVille);
        txtVille = (TextView) findViewById(R.id.txtVille);
        Log.i("*****",txtVille.getText()+"");
        ecouteSpinnerChoice(listeVilles);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Scan Button
        Button buttonBarCodeScan = (Button) findViewById(R.id.buttonScan);
        buttonBarCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initiate scan with our custom scan activity
                new IntentIntegrator(MainActivity.this).setCaptureActivity(scannerActivity.class).initiateScan();
            }
        });


    }


    private void ecouteSpinnerChoice( final List<String> listeProduits){
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeProduits);
        spinnerVille.setAdapter(adapter);
        spinnerVille.setPrompt("Selectionner ville");
        spinnerVille.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                ville=listeVilles.get(i);
                idVille=i+1;
                txtVille.setText(ville);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    //propriétés
    private Controle controle;


    /**
     * ecoute evenement sur bouton de calcul
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //show dialogue with result
                try {
                    showResultDialogue(result.getContents());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //method to construct dialogue with scan results
    public void showResultDialogue(final String result) throws JSONException {
        AlertDialog.Builder builder;
          Intent intent = new Intent(MainActivity.this, ResultatScan.class);
          Bundle b = new Bundle();
          b.putString("barcode", result);
          b.putString("ville", spinnerVille.getSelectedItem().toString());
          b.putInt("idVille", idVille);
          intent.putExtras(b); //Put your id to your next Intent
          startActivity(intent);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
       // builder.setTitle("Produit reconnu").setMessage("Scanned result is " + result).show();
    }


    public String get(String barcode, final VolleyCallback callback) throws IOException, JSONException {
        String data=null;
        String urlString= "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            InputStream stream = conn.getInputStream();
            data = convertStreamToString(stream);
            stream.close();

        }catch(SocketTimeoutException e){

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // JSONObject jsonObject = new JSONObject(data);
        // data = jsonObject.getString("product_name");

        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlString, new JSONObject(), new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response); // call call back function here

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("Volley error json object ", "Error: " + error.getMessage());

            }
        }){
            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        // Adding request to request queue
        ExampleRequestQueue.add(jsonObjReq);
        return product;
    }


    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    private String readIt(InputStream is) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            response.append(line).append('\n');
        }
        return response.toString();
    }

    private void ecouteTriVille(){
        ((Button)findViewById(R.id.btnAccueilTriVille)).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this, AjoutProduit1.class);
                startActivity(intent);
            }
        });
    }




    public List<String> getListVille(){
        AssetManager assetManager = getAssets();
        List<String> liste = new ArrayList<String>();

        InputStream strConsignes = null;
        try {
            strConsignes = assetManager.open("consignes.csv");

            BufferedReader reader = new BufferedReader(new InputStreamReader(strConsignes));
            String csvLine;
            //csvLine = reader.readLine();
            String[] row = new String[1];
            while ((csvLine = reader.readLine()) != null) {
                row = csvLine.split(";");
                if (row.length > 1) {
                    if (row[0].contains("Villes")) {
                        for (int i = 1; i < row.length; i++) {
                            listeVilles.add(row[i]);
                            liste.add(row[i]);
                            Toast.makeText(MainActivity.this, row[i],Toast.LENGTH_SHORT);
                        }
                    }
                break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste;



    }


}