package com.example.travelshare;

import java.util.HashSet;
import java.util.Set;

public class Post {
    private String auteur;
    private String date;
    private String description;
    private String tags;
    private int imageResId;
    private String photoUri;
    private long dateCreation;
    private Set<String> likeurs = new HashSet<>();

    public Post(String auteur, String date, String description,
                String tags, int imageResId) {
        this.auteur = auteur;
        this.date = date;
        this.description = description;
        this.tags = tags;
        this.imageResId = imageResId;
        this.dateCreation = System.currentTimeMillis();
        this.likeurs = new HashSet<>();
    }

    public Post(String photoUri, String auteur, String description) {
        this.photoUri = photoUri;
        this.auteur = auteur;
        this.description = description;
        this.date = new java.text.SimpleDateFormat("dd/MM/yyyy",
                java.util.Locale.getDefault()).format(new java.util.Date());
        this.dateCreation = System.currentTimeMillis();
        this.likeurs = new HashSet<>();
    }

    public String getAuteur() {
        return auteur;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public long getDateCreation() {
        return dateCreation;
    }

    public Set<String> getLikeurs() {
        return likeurs;
    }

    /**
     * Nombre de likes = nombre d'utilisateurs ayant liké
     */
    public int getLikes() {
        return likeurs == null ? 0 : likeurs.size();
    }

    /**
     * Vrai si cet utilisateur a déjà liké ce post
     */
    public boolean isLikedBy(String userId) {
        return likeurs != null && likeurs.contains(userId);
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public void setLikeurs(Set<String> likeurs) {
        this.likeurs = likeurs;
    }

    /**
     * Ajoute ou retire userId des likeurs.
     *
     * @return true si le post est maintenant liké, false sinon.
     */
    public boolean toggleLike(String userId) {
        if (likeurs == null) likeurs = new HashSet<>();
        if (likeurs.contains(userId)) {
            likeurs.remove(userId);
            return false;
        } else {
            likeurs.add(userId);
            return true;
        }
    }
}