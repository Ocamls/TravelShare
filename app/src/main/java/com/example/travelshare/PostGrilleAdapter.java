package com.example.travelshare;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostGrilleAdapter extends RecyclerView.Adapter<PostGrilleAdapter.GrilleViewHolder> {

    private final Context    context;
    private final List<Post> posts;

    public PostGrilleAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts   = posts;
    }

    @NonNull
    @Override
    public GrilleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_grille, parent, false);
        return new GrilleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrilleViewHolder holder, int position) {
        Post post = posts.get(position);

        if (post.getPhotoUri() != null && !post.getPhotoUri().isEmpty()) {
            holder.ivPhoto.setImageURI(Uri.parse(post.getPhotoUri()));
        } else {
            holder.ivPhoto.setImageResource(post.getImageResId());
        }
    }

    @Override
    public int getItemCount() { return posts.size(); }

    static class GrilleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;

        public GrilleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhotoGrille);
        }
    }
}