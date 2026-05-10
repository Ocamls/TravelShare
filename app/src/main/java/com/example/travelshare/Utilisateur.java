package com.example.travelshare;

public class Utilisateur {
    public String nomUtilisateur;
    public String motDePasse;

    public String nom;
    public String prenom;


    public Utilisateur(String nomUtilisateur, String motDePasse, String nom, String prenom) {
        this.nomUtilisateur         = nomUtilisateur;
        this.motDePasse  = motDePasse;
        this.nom = nom;
        this.prenom = prenom;

    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
