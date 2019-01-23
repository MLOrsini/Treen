package com.treen.charlotte.treeanapp.vue;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.treen.charlotte.treeanapp.R;
import com.treen.charlotte.treeanapp.controleur.Controle;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AjoutProduit2 extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_produit);
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null){
            this.nomProduit = b.getString("nomProduit");
        }
        init();
    }

    String nomProduit;
    String line;
    private ListView listView;
    private String[] nameArray = new String[1];
    private int[] consignesArray = new int[1];
    List<String> produitsList = new ArrayList<String>();
    HashMap<String, List<String>> categoriesList = new HashMap<String, List<String>>();
    List<String> listeItems= new ArrayList<String>();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> categoriesNames = new ArrayList<String>();;
    TextView nomProduitView ;
    Button button ;

    private void init() {
         /*nameArray=(String[]) listeItems.toArray(new String[listeItems.size()]);

        CustomListVille whatever = new CustomListVille(this, nameArray,  consignesArray);
        listView = (ListView) findViewById(R.id.listviewID);
        TextView text = findViewById(R.id.textview_ville);
        text.setText("activity ville");
        listView.setAdapter(whatever);*/

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        nomProduitView = (TextView) findViewById(R.id.nomProduit);
        button = (Button) findViewById(R.id.button);
        nomProduitView.setText(nomProduit);
        // preparing list data
        getItemsList();
        listAdapter = new ExpandableListAdapter(this, categoriesNames, categoriesList);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line = listAdapter.getResults();
                Log.i("****",line);

            }
        });

        Toast.makeText(this, line ,Toast.LENGTH_LONG).show();
        expListView.setAdapter(listAdapter);

    }

    //map tri sur lyon
    /* ****CODE****
        0 : jeter
        1 : trier
        2 : poubelle verre
        3 : organique
        4 : point de collecte
     */
    public void getItemsList(){
        AssetManager assetManager = getAssets();
        try {
            InputStream strConsignes = assetManager.open("consignes.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(strConsignes));
            String csvLine;
            csvLine = reader.readLine();
            String catName=null;
            String[] row = csvLine.split(";");
            listeItems = new ArrayList<String>();
            while ((csvLine = reader.readLine()) != null) {
                row = csvLine.split(";");
                if(row.length>1) {
                    listeItems.add(row[0]);

                }else if(row.length==1){
                    if(row[0].length()>0 && row[0].charAt(0)=='#'){

                        if(listeItems.size()>0){
                            if(catName!= null) {
                                categoriesNames.add(catName);
                                categoriesList.put(catName, listeItems);
                                listeItems = new ArrayList<String>();
                                listeItems.clear();
                            }
                        }

                        catName = row[0].replace("#", "");

                    }
                }
            }
            categoriesNames.add(catName);
            categoriesList.put(catName, listeItems);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
