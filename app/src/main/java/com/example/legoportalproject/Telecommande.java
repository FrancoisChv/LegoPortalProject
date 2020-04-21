package com.example.legoportalproject;

//Cette classe permet de définir la structure d'un objet télécommande que l'on ajoutera par
// la suite dans la base de données

public class Telecommande  {

    private String nom_tel; //Nom de la télécommande
    private String device_id_tel; //Devide ID de l'appareil de l'utilisateur de la télécommande
    private String user_tel;

    public String getNom_tel() {
        return nom_tel;
    }

    public void setNom_tel(String nom_tel) {
        this.nom_tel = nom_tel;
    }

    public String getdevice_id_tel() {
        return device_id_tel;
    }

    public void setMac_tel(String device_id_tel) {
        this.device_id_tel = device_id_tel;
    }

    /*
    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }
    */

    public Telecommande() {
    }

    public Telecommande(String nom_tel, String device_id_tel) {
        this.nom_tel = nom_tel;
        this.device_id_tel = device_id_tel;
    }

    @Override
    public String toString() {
        return "Telecommande{" +
                "nom_tel='" + nom_tel + '\'' +
                ", device_id_tel='" + device_id_tel + '\'' +
        '}';
    }
}