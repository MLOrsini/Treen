package com.treen.charlotte.treeanapp.controleur;

import com.treen.charlotte.treeanapp.modele.Profil;

/* Ici, on va faire un singleton. c'est à dire qu'il ne pourra exister qu'une seule instance de la classe Controle.
   Pour se faire, on initialise la class à final, pour qu'elle ne puisse pas être modifiée,
   son constructeur à private, pour qu'il ne puisse être appelé qu'ici,
   et dans getInstance, on crée une instance seulement s'il n'en existait pas, et on retourne :
        l'instance existante si elle existait
        l'instance crée s'il n'en existait pas
 */
public final class Controle {
    private static Controle instance =null;
    private Profil profile;

    /**
     * constructeur private
     */
    private Controle(){
        super();
    }

    /**
     * creation ou récupération de l'instance
     * @return instance
     */
    public static final Controle getInstance(){
        if(Controle.instance==null){
            Controle.instance=new Controle();
        }
        return Controle.instance;
    }

    /**
     *Creation du profil
     * @param poids
     * @param age
     * @param sexe femme : 0, homme : 1
     * @param taille en cm
     */
    public void creerProfil(int poids, int age, int sexe, int taille){
        profile=new Profil(poids,taille,age,sexe);
    }



/* ********** Fonctions IMG ********** */

    /**
     * recuperation img de profile
     * @return img
     */
    public float getIMG(){
        return profile.getImg();
    }

    /**
     * recuperation message de profile
     * @return message
     */
    public String creerMessage(){
        return profile.getMessage();
    }

}
