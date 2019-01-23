package com.treen.charlotte.treeanapp.vue;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.treen.charlotte.treeanapp.R;
import com.treen.charlotte.treeanapp.controleur.Controle;
import com.treen.charlotte.treeanapp.vue.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConsigneProduit extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consigne_produit);
        init();
        this.controle=Controle.getInstance();


    }


    private ListView listView;
    private String[] nameArray = new String[1];
    private String[] infoArray = new String[1];
    private int[] consignesArray = new int[1];

    //propriétés:
    private Controle controle;
    private TextView txtNomProduit;

    private ImageView imgProduct;
    private Spinner spinnerProduct;

    Map<String, Integer> mapTriLyon;
    Map<String, ArrayList> bddProduits;
    List<String> listeProduits;


    private void init() {
        txtNomProduit = (TextView) findViewById(R.id.txtNomProduit);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);

        //créer "base de données" consignes de tri sur lyon
        mapTriLyon = new HashMap<String, Integer>();
        creerMapTriLyon((HashMap<String, Integer>) mapTriLyon);

        //créer "base de données" de quelques exemples de produits :
        final List<String> listeProduits = new ArrayList<String>();
        bddProduits = new HashMap<String, ArrayList>();
        creerMapProduits(bddProduits, (ArrayList) listeProduits);

        //créer le spinner de choix de produit à afficher
        spinnerProduct = (Spinner) findViewById(R.id.spinnerProduct);
        ecouteSpinnerChoice(mapTriLyon,bddProduits,listeProduits);


    }


    private void ecouteSpinnerChoice(Map<String, Integer> mapTriLyon, Map<String, ArrayList> bddProduits, final List<String> listeProduits){
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeProduits);
        spinnerProduct.setAdapter(adapter);
        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(ConsigneProduit.this, listeProduits.get(i),Toast.LENGTH_SHORT);
                String product=listeProduits.get(i);
                printProduct(product);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //TEST afficher les consignes de tri pour les yaourts danone
    private void printProduct(String product){
        int nbItems;
        int consigne=-1;
        int currentItem=1;          //on commence à 1 et pas 0 car le premier est le nom de l'image à afficher

        //récupérer la liste d'éléments d'emballage du produit voulu
        List<String> tempList=bddProduits.get(product);      //!!pour l'exemple !! ici, on affichera juste le yaourt
        //afficher sur l'écran le nom du produit
        txtNomProduit.setText(product);
        //afficher sur l'écran l'image du produit
        imgProduct.setImageResource(getResources().getIdentifier(tempList.get(0),"drawable", getPackageName()));

        //afficher sur l'écran les consignes de tri de chaque élément d'emballage
        //TODO à rendre plus propre quand on aura une vraie database
        nbItems= tempList.size();

        nameArray = new String[nbItems];
        infoArray = new String[nbItems];
        consignesArray = new int[nbItems];
        String consigneText;
        String item;


        currentItem=1;
        nbItems=nbItems-1;
        nameArray = new String[nbItems];
        infoArray = new String[nbItems];
        consignesArray = new int[nbItems];
        item=tempList.get(currentItem);


        for(int i = 0;i<nbItems;i++){

            item=tempList.get(currentItem);
            nameArray[i] = item;
            infoArray[i] = getConsigne(item);
            consignesArray[i] = mapTriLyon.get(item);
            currentItem++;
        }
        CustomListAdapter whatever = new CustomListAdapter(this, nameArray, infoArray, consignesArray);
        listView = (ListView) findViewById(R.id.listviewID);
        listView.setAdapter(whatever);
    }

    private String getConsigne(String item){
        int consigne=-1;
        String text;
        consigne=mapTriLyon.get(item);
        switch(consigne) {
            case -1:
                text="erreur!";
                break;
            case 0:
                text = "À jeter dans la poubelle grise";

                break;
            case 1:
                text = "À recycler dans la  poubelle jaune";
                break;
            case 2:
                text = "À déposer dans le sillot à verre le plus proche";
                break;
            case 3:
                text = "À composter";
                break;
            case 4:
                text = "À déposer dans le point de collecte le plus proche";
                break;
            default :
                text="fais ce que tu veux avec ton " + item + " frr";
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
    public void creerMapTriLyon(HashMap<String,Integer> mapTriLyon){
        mapTriLyon.put("bouteille plastique",1);
        mapTriLyon.put("pot yaourt",0);
        mapTriLyon.put("bouchon en plastique",1);
        mapTriLyon.put("sac plastique",0);
        mapTriLyon.put("brique carton",1);
        mapTriLyon.put("boite oeuf",1);
        mapTriLyon.put("vegetaux",3);
        mapTriLyon.put("papier",1);
        mapTriLyon.put("bouteille verre",2);
        mapTriLyon.put("ampoule",4);
        mapTriLyon.put("opercule en plastique",0);
        mapTriLyon.put("surremballage carton",1);
        mapTriLyon.put("surremballage plastique",0);
        mapTriLyon.put("conserve en métal",1);

    }

    public void creerMapProduits(Map<String, ArrayList> bddProduits, ArrayList listeProduits){
        //création d'une liste de composants de produit :
        // yaourt nature danone
        List<String> yaourtNatureDanoneList=new ArrayList<String>();
        yaourtNatureDanoneList.add(0,"yaourtdanone");
        yaourtNatureDanoneList.add("pot yaourt");
        yaourtNatureDanoneList.add("opercule en plastique");
        yaourtNatureDanoneList.add("surremballage carton");

        // bouteille coca cola
        List<String> cocaCola=new ArrayList<String>();
        cocaCola.add(0,"cocacola");
        cocaCola.add("bouteille plastique");
        cocaCola.add("bouchon en plastique");
        cocaCola.add("surremballage plastique");

        //brique de lait lactel
        List<String> laitLactel=new ArrayList<String>();
        laitLactel.add(0,"briquelaitlactel");
        laitLactel.add("brique carton");

        //conserve mais bonduelle
        List<String> maisBonduelle=new ArrayList<String>();
        maisBonduelle.add(0,"maisbonduelle");
        maisBonduelle.add("conserve en métal");


        //mettre produits dans "base de données" de produits et liste de produits
        bddProduits.put("Yaourt Nature Danone", (ArrayList<String>) yaourtNatureDanoneList);
        listeProduits.add("Yaourt Nature Danone");
        bddProduits.put("Coca Cola", (ArrayList<String>) cocaCola);
        listeProduits.add("Coca Cola");
        bddProduits.put("Brique de Lait Lactel", (ArrayList<String>) laitLactel);
        listeProduits.add("Brique de Lait Lactel");
        bddProduits.put("Conserve de Mais Bonduelle", (ArrayList<String>) maisBonduelle);
        listeProduits.add("Conserve de Mais Bonduelle");


    }
}
