package com.example.travelshare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;
    private Context context;
    private boolean isConnected;

    public PostAdapter(Context context, List<Post> posts, boolean isConnected) {
        this.context     = context;
        this.posts       = posts;
        this.isConnected = isConnected;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.tvUser.setText("Par : " + post.getAuteur());
        holder.tvDate.setText("Publié le : " + post.getDate());
        holder.tvDescription.setText(post.getDescription());
        holder.tvTags.setText(post.getTags());
        holder.tvLikes.setText(String.valueOf(post.getLikes()));
        holder.ivPost.setImageResource(post.getImageResId());

        // Icône like selon état
        holder.ivLike.setImageResource(post.isLiked()
                ? android.R.drawable.btn_star_big_on
                : android.R.drawable.btn_star_big_off);

        if (isConnected) {
            // Like actif
            holder.likeContainer.setAlpha(1f);
            holder.likeContainer.setOnClickListener(v -> {
                post.toggleLike();
                holder.tvLikes.setText(String.valueOf(post.getLikes()));
                holder.ivLike.setImageResource(post.isLiked()
                        ? android.R.drawable.btn_star_big_on
                        : android.R.drawable.btn_star_big_off);
            });

            // Signalement actif
            holder.btnReport.setAlpha(1f);
            holder.btnReport.setOnClickListener(v -> {
                Intent intent = new Intent(context, SignalementActivity.class);
                context.startActivity(intent);
            });

            // Clic sur photo → profil auteur
            holder.ivPost.setOnClickListener(v -> {
                Intent intent = new Intent(context, ActiviteUtilisateurActivity.class);
                intent.putExtra("auteur", post.getAuteur());
                context.startActivity(intent);
            });

        } else {
            // Mode anonyme : like et signalement désactivés visuellement
            holder.likeContainer.setAlpha(0.4f);
            holder.likeContainer.setOnClickListener(v ->
                    Toast.makeText(context,
                            "Connectez-vous pour liker une photo",
                            Toast.LENGTH_SHORT).show()
            );

            holder.btnReport.setAlpha(0.4f);
            holder.btnReport.setOnClickListener(v ->
                    Toast.makeText(context,
                            "Connectez-vous pour signaler une photo",
                            Toast.LENGTH_SHORT).show()
            );

            // Clic sur photo → désactivé en mode anonyme
            holder.ivPost.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() { return posts.size(); }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser, tvDate, tvDescription, tvTags, tvLikes, btnReport;
        ImageView ivPost, ivLike;
        LinearLayout likeContainer;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser        = itemView.findViewById(R.id.tvUser);
            tvDate        = itemView.findViewById(R.id.tvDate);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTags        = itemView.findViewById(R.id.tvTags);
            tvLikes       = itemView.findViewById(R.id.tvLikes);
            btnReport     = itemView.findViewById(R.id.btnReport);
            ivPost        = itemView.findViewById(R.id.ivPost);
            ivLike        = itemView.findViewById(R.id.ivLike);
            likeContainer = itemView.findViewById(R.id.likeContainer);
        }
    }
}