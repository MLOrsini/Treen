package com.treen.charlotte.treeanapp.vue;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.treen.charlotte.treeanapp.R;

public class CustomListAdapter extends ArrayAdapter {


    Integer[] imageIDarray = {R.drawable.poubelle_grise,
            R.drawable.poubelle_jaune,
            R.drawable.poubelle_verre,
            R.drawable.poubelle_osef,
            R.drawable.poubelle_osef,
            R.drawable.poubelle_osef};


    //to reference the Activity
    private final Activity context;
    ListView listView;
    //to store the animal images
    private  ImageView[] imagearray;
    private  int[] consignesArray;

    //to store the list of countries
    private  String[] nameArray;

    //to store the list of countries
    private  String[] infoArray;
    public CustomListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam, int[] consignesArrayParam){

        super(context,R.layout.listview_row , nameArrayParam);
        this.context=context;
        this.consignesArray = consignesArrayParam;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;

    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.infoTextViewID);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(infoArray[position]);
        imageView.setImageResource(imageIDarray[consignesArray[position]]);

        //imageView=imagearray[position];

        return rowView;

    };

}
