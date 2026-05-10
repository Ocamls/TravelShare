package com.example.travelshare;

public class Post {
    private String auteur;
    private String date;
    private String description;
    private String tags;
    private int    likes;
    private boolean liked;
    private int    imageResId;
    private String photoUri;
    private long   dateCreation;


    public Post(String auteur, String date, String description,
                String tags, int likes, int imageResId) {
        this.auteur        = auteur;
        this.date          = date;
        this.description   = description;
        this.tags          = tags;
        this.likes         = likes;
        this.liked         = false;
        this.imageResId    = imageResId;
        this.dateCreation  = System.currentTimeMillis();
    }

    public Post(String photoUri, String auteur, String description) {
        this.photoUri      = photoUri;
        this.auteur        = auteur;
        this.description   = description;
        this.date          = new java.text.SimpleDateFormat("dd/MM/yyyy",
                java.util.Locale.getDefault())
                .format(new java.util.Date());
        this.likes         = 0;
        this.liked         = false;
        this.dateCreation  = System.currentTimeMillis();
    }

    public String  getAuteur()      { return auteur; }
    public String  getDate()        { return date; }
    public String  getDescription() { return description; }
    public String  getTags()        { return tags; }
    public int     getLikes()       { return likes; }
    public boolean isLiked()        { return liked; }
    public int     getImageResId()  { return imageResId; }

    public String  getPhotoUri()    { return photoUri; }
    public long    getDateCreation(){ return dateCreation; }

    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }
    public void toggleLike() {
        if (liked) { likes--; } else { likes++; }
        liked = !liked;
    }
}