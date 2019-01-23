package com.treen.charlotte.treeanapp.vue;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.treen.charlotte.treeanapp.R;
import com.treen.charlotte.treeanapp.controleur.Controle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultatScan extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consigne_produit);
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if (b != null) {
            this.barcode = b.getString("barcode");
            this.ville = b.getString("ville");
            init();
            this.controle = Controle.getInstance();


        }
    }


    private ListView listView;
    private String ville;
    private String[] nameArray = new String[1];
    private String[] infoArray = new String[1];
    private int[] consignesArray = new int[1];
    private String nomProduit;
    //propriétés:
    private String barcode;
    private Controle controle;
    private String image_url;
    private TextView txtNomProduit;

    private ImageView imgProduct;

    Map<String, Integer> mapTriLyon;


    private void init() {
        txtNomProduit = (TextView) findViewById(R.id.txtNomProduit);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);

        Log.d("***", " ville init");
        AssetManager assetManager = getAssets();
        try {
            InputStream strProduit = assetManager.open("produit.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(strProduit));
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                Log.i("**", csvLine);
                String[] row = csvLine.split(";");

                if (row.length > 1 && row[0].equals(barcode)) {
                    nomProduit = row[1];
                    txtNomProduit.setText(nomProduit);
                    imgProduct.setImageResource(getResources().getIdentifier("id_" + barcode, "drawable", getPackageName()));
                    nameArray = Arrays.copyOfRange(row, 3, row.length);
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //créer "base de données" consignes de tri sur lyon
        mapTriLyon = new HashMap<String, Integer>();
        creerMapTriLyon((HashMap<String, Integer>) mapTriLyon);
        infoArray = new String[nameArray.length];
        consignesArray = new int[nameArray.length];

        try {
            for (int i = 0; i < nameArray.length; i++) {
                infoArray[i] = getConsigne(nameArray[i]);
                consignesArray[i] = mapTriLyon.get(nameArray[i]);
            }

            CustomListAdapter whatever = new CustomListAdapter(this, nameArray, infoArray, consignesArray);
            listView = (ListView) findViewById(R.id.listviewID);
            listView.setAdapter(whatever);
        }catch(Exception e){
            Intent intent = new Intent(ResultatScan.this, AjoutProduit1.class);
            Bundle b = new Bundle();
            b.putString("barcode", barcode);
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    private String getConsigne(String item) {
        int consigne = -1;
        String text;
        try{
        consigne = mapTriLyon.get(item);
            switch (consigne) {
                case -1:
                    text = "erreur!";
                    break;
                case 0:
                    text = "À jeter dans la poubelle grise";

                    break;
                case 1:
                    text = "À recycler dans la  poubelle jaune";
                    break;
                case 2:
                    text = "À déposer dans le silo à verre le plus proche";
                    break;
                case 3:
                    text = "À composter";
                    break;
                case 4:
                    text = "À déposer dans le point de collecte le plus proche";
                    break;
                default:
                    text = "fais ce que tu veux avec ton " + item + " frr";
            }
        }catch(Exception e){
            text="Non reconnu, à mettre dans poubelle grise";
        }
        return text;
    }


    //map tri sur lyon
    /* ****CODE****
        0 : jeter
        1 : trier
        2 : poubelle verre
        3 : organique
        4 : point de collecte
     */
    public void creerMapTriLyon(HashMap<String, Integer> mapTriLyon) {
        AssetManager assetManager = getAssets();
        try {
            InputStream strConsignes = assetManager.open("consignes.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(strConsignes));
            String csvLine;
            csvLine = reader.readLine();
            String[] row = csvLine.split(";");
            int idVille = Arrays.asList(row).indexOf(ville);
            Log.i("*****",ville+"  +id "+idVille);
            Log.i("*****", csvLine);

            if (idVille != -1) {
                while ((csvLine = reader.readLine()) != null) {
                    row = csvLine.split(";");
                    if (row.length > 1) {

                        Log.i("*****",row[0]+" "+ Integer.parseInt(row[idVille]));
                        mapTriLyon.put(row[0], Integer.parseInt(row[idVille]));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}