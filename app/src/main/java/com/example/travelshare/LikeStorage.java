package com.example.travelshare;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.List;
public class LikeStorage {

    private static final String PREFS_NAME = "travelshare_likes";
    private static final String ANON       = "anon";

    private final SharedPreferences prefs;
    private final UtilisateurStorage utilisateurStorage;

    //TODO : ajouter un fonction qui calcul les likes d'un post en fonction des likes des utilisateurs anonymes.
    // Si un utilisateur est anonyme et qu'il like un post, lors de la convertion du compte,
    // il doit pouvoir revoir tous ses likes anonyme mais sur son compte connecté.
    // POUR LE MOMENT NE PAS PRENDRE EN COMPTE LES ANONYMES
    public LikeStorage(Context context) {
        this.prefs              = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.utilisateurStorage = new UtilisateurStorage(context);
    }

    private String cle(String postId) {
        String user = utilisateurStorage.getUtilisateurCourant();
        return "like_" + (user != null ? user : ANON) + "_" + postId;
    }

    public boolean aLike(String postId) {
        return prefs.getBoolean(cle(postId), false);
    }

    public boolean toggleLike(String postId) {
        boolean etat = !aLike(postId);
        prefs.edit().putBoolean(cle(postId), etat).apply();
        return etat;
    }

    public void transfererLikesAnonymes(String nomUtilisateur, List<String> tousPostIds) {
        SharedPreferences.Editor editor = prefs.edit();
        for (String postId : tousPostIds) {
            String cleAnon  = "like_" + ANON + "_" + postId;
            String cleUser  = "like_" + nomUtilisateur + "_" + postId;
            if (prefs.getBoolean(cleAnon, false)) {
                editor.putBoolean(cleUser, true);
                editor.remove(cleAnon);
            }
        }
        editor.apply();
    }
}