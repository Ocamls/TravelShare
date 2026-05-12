package com.example.travelshare;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.List;
public class LikeStorage {

    private static final String PREFS_NAME = "travelshare_likes";
    private static final String ANON       = "anon";

    private final SharedPreferences prefs;
    private final UtilisateurStorage utilisateurStorage;

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