package com.treen.charlotte.treeanapp.modele;

public class Profil {

    //constantes
        //IMG
    private static final Integer imgMaxFemme=30;
    private static final Integer imgMinFemme=15;
    private static final Integer imgMaxHomme=25;
    private static final Integer imgMinHomme=10;

    //propriétés
        //IMG
    private Integer poids, taille, age, sexe;
    private float img;
    private String message;

    public Profil(Integer poids, Integer taille, Integer age, Integer sexe) {
            //IMG
        this.poids = poids;
        this.taille = taille;
        this.age = age;
        this.sexe = sexe;
        this.calculIMG();
        this.resultingMessage();
    }


//******************** Fonctions IMG ********************
    public Integer getPoids() {
        return poids;
    }

    public Integer getTaille() {
        return taille;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getSexe() {
        return sexe;
    }

    public float getImg() {
        return img;
    }

    public String getMessage() {
        return message;
    }

    private void calculIMG(){
        float tailleM=(float) taille / 100;
        this.img=(float)((1.2*poids / tailleM*tailleM) + 0.23*age - 10.83*sexe -5.4);
    }

    private void resultingMessage(){
        int min, max;
        if(sexe==0){    //femme
            min=imgMinFemme;
            max=imgMaxFemme;
        }else{          //femme
            min=imgMinHomme;
            max=imgMaxHomme;
        }
        if(img<min){
            message="trop faible";
        }else if(img>=max){
            message="trop élevé";
        }else {
            message = "bien!";
        }
    }


// ******************** fonctions ConsigneProduits ********************


}
