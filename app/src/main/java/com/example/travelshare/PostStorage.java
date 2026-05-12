package com.example.travelshare;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
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
        List<Post> posts = gson.fromJson(json, type);
        for (Post p : posts) {
            if (p.getLikeurs() == null) p.setLikeurs(new HashSet<>());
        }
        return posts;
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


    public boolean toggleLike(int postIndex, String userId) {
        List<Post> posts = getTousPosts();
        if (postIndex < 0 || postIndex >= posts.size()) return false;
        boolean liked = posts.get(postIndex).toggleLike(userId);
        sauvegarder(posts);
        return liked;
    }

    public List<Post> getPostsUtilisateur(String auteur) {
        List<Post> result = new ArrayList<>();
        for (Post p : getTousPosts()) {
            if (auteur != null && auteur.equals(p.getAuteur())) result.add(p);
        }
        return result;
    }

    private void sauvegarder(List<Post> posts) {
        prefs.edit().putString(KEY_POSTS, gson.toJson(posts)).apply();
    }

    public void supprimer(Post postASupprimer) {
        if (postASupprimer == null) return;
        List<Post> posts = getTousPosts();
        for (int i = 0; i < posts.size(); i++) {
            Post p = posts.get(i);
            if (p.getAuteur().equals(postASupprimer.getAuteur())
                    && p.getDate().equals(postASupprimer.getDate())
                    && p.getDescription().equals(postASupprimer.getDescription())) {
                posts.remove(i);
                break;
            }
        }
        sauvegarder(posts);
    }


}