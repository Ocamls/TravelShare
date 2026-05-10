package com.example.travelshare;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurStorage {

    private static final String PREFS_NAME    = "travelshare_users";
    private static final String KEY_USERS     = "users";
    private static final String KEY_COURANT   = "user_courant";

    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public UtilisateurStorage(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // --- Comptes ---

    public List<Utilisateur> getTousUtilisateurs() {
        String json = prefs.getString(KEY_USERS, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Utilisateur>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void ajouterUtilisateur(Utilisateur u) {
        List<Utilisateur> users = getTousUtilisateurs();
        users.add(u);
        prefs.edit().putString(KEY_USERS, gson.toJson(users)).apply();
    }

    public Utilisateur trouverUtilisateur(String nom, String motDePasse) {
        for (Utilisateur u : getTousUtilisateurs()) {
            if (u.nom.equals(nom) && u.motDePasse.equals(motDePasse)) return u;
        }
        return null;
    }

    public boolean nomExiste(String nom) {
        for (Utilisateur u : getTousUtilisateurs()) {
            if (u.nom.equals(nom)) return true;
        }
        return false;
    }

    // --- Session courante ---

    public void connecter(String nom) {
        prefs.edit().putString(KEY_COURANT, nom).apply();
    }

    public void deconnecter() {
        prefs.edit().remove(KEY_COURANT).apply();
    }

    public String getUtilisateurCourant() {
        return prefs.getString(KEY_COURANT, null); // null = anonyme
    }

    public boolean isConnecte() {
        return getUtilisateurCourant() != null;
    }
}