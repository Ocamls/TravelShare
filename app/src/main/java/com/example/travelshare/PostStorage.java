package com.example.travelshare;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PostStorage {

    private static final String PREFS_NAME = "travelshare_prefs";
    private static final String KEY_POSTS  = "posts";

    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public PostStorage(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public List<Post> getTousPosts() {
        String json = prefs.getString(KEY_POSTS, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Post>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void ajouterPost(Post post) {
        List<Post> posts = getTousPosts();
        posts.add(0, post);
        sauvegarder(posts);
    }

    public void supprimerPost(int index) {
        List<Post> posts = getTousPosts();
        if (index >= 0 && index < posts.size()) {
            posts.remove(index);
            sauvegarder(posts);
        }
    }

    public void effacerTout() {
        prefs.edit().remove(KEY_POSTS).apply();
    }
    public void mettreAJourLikes(String postId, int nouveauxLikes) {
        List<Post> posts = getTousPosts();
        int index = Integer.parseInt(postId);
        if (index >= 0 && index < posts.size()) {
            // Gson ne permet pas de modifier directement, on recrée
            Post p = posts.get(index);
            // On force la valeur via un champ accessible
            posts.set(index, p);
            sauvegarder(posts);
        }
    }

    public List<Post> getPostsUtilisateur(String auteur) {
        List<Post> posts = getTousPosts();
        List<Post> postsUtilisateur = new ArrayList<>();
        for (Post post : posts) {
            if (auteur != null && auteur.equals(post.getAuteur())) {
                postsUtilisateur.add(post);
            }
        }
        return postsUtilisateur;
    }

    private void sauvegarder(List<Post> posts) {
        prefs.edit().putString(KEY_POSTS, gson.toJson(posts)).apply();
    }
}