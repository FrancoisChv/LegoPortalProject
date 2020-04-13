package com.example.legoportalproject;

public class Telecommande {

    private String nom_tel;
    private String mac_tel;
    private String user_tel;

    public String getNom_tel() {
        return nom_tel;
    }

    public void setNom_tel(String nom_tel) {
        this.nom_tel = nom_tel;
    }

    public String getMac_tel() {
        return mac_tel;
    }

    public void setMac_tel(String mac_tel) {
        this.mac_tel = mac_tel;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public Telecommande() {
    }

    public Telecommande(String nom_tel, String mac_tel) {
        this.nom_tel = nom_tel;
        this.mac_tel = mac_tel;
    }

    @Override
    public String toString() {
        return "Telecommande{" +
                "nom_tel='" + nom_tel + '\'' +
                ", mac_tel='" + mac_tel + '\'' +
                ", user_tel='" + user_tel + '\'' +
                '}';
    }
}
