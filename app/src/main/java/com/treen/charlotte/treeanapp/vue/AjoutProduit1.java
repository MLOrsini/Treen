package com.treen.charlotte.treeanapp.vue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.treen.charlotte.treeanapp.R;
import com.treen.charlotte.treeanapp.controleur.Controle;

public class AjoutProduit1 extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE=1;
    ImageView mImageView;
    String barcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if (b != null) {
            this.barcode = b.getString("barcode");
        }

        setContentView(R.layout.activity_ajout_produit1);

        Button fab = (Button) findViewById(R.id.button);
        Button ajouterPhoto = (Button) findViewById(R.id.buttonPicture);
        mImageView = (ImageView) findViewById(R.id.mImageView);

        final EditText  nomProduitInput =(EditText) findViewById(R.id.nomProduitInput);
        final EditText  barcodeInput =(EditText) findViewById(R.id.codeInput);
        barcodeInput.setText(barcode);

        ajouterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ouvrir camera
                dispatchTakePictureIntent();


                //save photo


            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nomProduitInput.getText().toString();

                Intent intent=new Intent(AjoutProduit1.this, AjoutProduit2.class);
                Bundle b = new Bundle();
                b.putString("nomProduit",name); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    //camera
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
