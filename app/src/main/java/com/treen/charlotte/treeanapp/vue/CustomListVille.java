package com.treen.charlotte.treeanapp.vue;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.treen.charlotte.treeanapp.R;

public class CustomListVille extends ArrayAdapter {



    //to reference the Activity
    private final Activity context;
    ListView listView;
    private  int[] consignesArray;
    private  String[] nameArray;
    private RadioGroup[] radioGroupConsignes ;

    public CustomListVille(Activity context, String[] nameArrayParam, int[] consignesArray){

        super(context,R.layout.listview_row , nameArrayParam);
        this.context=context;
        this.nameArray = nameArrayParam;

    }


    public View getView(int position, View view, ViewGroup parent) {
        radioGroupConsignes = new RadioGroup[nameArray.length];
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_rowville, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        nameTextField.setText(nameArray[position]);

        //  this.radioGroupConsignes[position]= (RadioGroup) rowView.findViewById(R.id.radioGroup_consignes);

        return rowView;

    };

    private void doOnConsigneChanged(RadioGroup group) {
        int checkedRadioId = group.getCheckedRadioButtonId();

     /*   if(checkedRadioId== R.id.radioButton_compost) {
        } else if(checkedRadioId== R.id.radioButton_Menager ) {
        } else if(checkedRadioId== R.id.radioButton_Recyclage) {
        }else if(checkedRadioId== R.id.radioButton_verre) {
        }*/
    }

    private int[] doSave(){
        int[] consignes = new int[radioGroupConsignes.length];
        for(int i=0;i<radioGroupConsignes.length;i++){
            consignes[i]=this.radioGroupConsignes[i].getCheckedRadioButtonId();
        }
        return consignes;
    }


}
