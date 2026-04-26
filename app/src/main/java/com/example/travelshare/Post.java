package com.example.travelshare;

public class Post {
    private String auteur;
    private String date;
    private String description;
    private String tags;
    private int likes;
    private boolean liked;
    private int imageResId;

    public Post(String auteur, String date, String description, String tags, int likes, int imageResId) {
        this.auteur = auteur;
        this.date = date;
        this.description = description;
        this.tags = tags;
        this.likes = likes;
        this.liked = false;
        this.imageResId = imageResId;
    }

    public String getAuteur() { return auteur; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getTags() { return tags; }
    public int getLikes() { return likes; }
    public boolean isLiked() { return liked; }
    public int getImageResId() { return imageResId; }

    public void toggleLike() {
        if (liked) { likes--; } else { likes++; }
        liked = !liked;
    }
}