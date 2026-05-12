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

    private final List<Post>  posts;
    private final Context     context;
    private final boolean     isConnected;
    private final String      userId;
    private final PostStorage postStorage;

    public PostAdapter(Context context, List<Post> posts, boolean isConnected, String username) {
        this.context     = context;
        this.posts       = posts;
        this.isConnected = isConnected;
        this.userId      = username;
        this.postStorage = new PostStorage(context);
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

        if (post.getPhotoUri() != null && !post.getPhotoUri().isEmpty()) {
            holder.ivPost.setImageURI(android.net.Uri.parse(post.getPhotoUri()));
        } else {
            holder.ivPost.setImageResource(post.getImageResId());
        }

        if (isConnected) {
            holder.ivLike.setImageResource(post.isLikedBy(userId)
                    ? android.R.drawable.btn_star_big_on
                    : android.R.drawable.btn_star_big_off);

            holder.likeContainer.setAlpha(1f);
            holder.likeContainer.setOnClickListener(v -> {
                boolean liked = postStorage.toggleLike(position, userId);
                post.toggleLike(userId);
                holder.tvLikes.setText(String.valueOf(post.getLikes()));
                holder.ivLike.setImageResource(liked
                        ? android.R.drawable.btn_star_big_on
                        : android.R.drawable.btn_star_big_off);
            });

            holder.btnReport.setAlpha(1f);
            holder.btnReport.setOnClickListener(v ->
                    context.startActivity(new Intent(context, SignalementActivity.class)));

            holder.ivPost.setOnClickListener(v -> {
                Intent intent = new Intent(context, ActiviteUtilisateurActivity.class);
                intent.putExtra("auteur", post.getAuteur());
                context.startActivity(intent);
            });

        } else {
            holder.ivLike.setImageResource(android.R.drawable.btn_star_big_off);

            holder.likeContainer.setAlpha(0.4f);
            holder.likeContainer.setOnClickListener(v ->
                    Toast.makeText(context,
                            "Connectez-vous pour liker une photo",
                            Toast.LENGTH_SHORT).show());

            holder.btnReport.setAlpha(0.4f);
            holder.btnReport.setOnClickListener(v ->
                    Toast.makeText(context,
                            "Connectez-vous pour signaler une photo",
                            Toast.LENGTH_SHORT).show());

            holder.ivPost.setOnClickListener(null);
        }
        if (isConnected && post.getAuteur().equals(userId)) {
            android.util.Log.d("DEBUG_POST",
                    "post=" + post +
                            " auteur=" + post.getAuteur() +
                            " userId=" + userId +
                            " btn=" + holder.btnSupprimer);
            holder.btnSupprimer.setVisibility(View.VISIBLE);

            holder.btnSupprimer.setOnClickListener(v -> {

                int currentPosition = holder.getAdapterPosition();

                if (currentPosition == RecyclerView.NO_POSITION) return;

                postStorage.supprimer(post);

                posts.remove(currentPosition);

                notifyDataSetChanged();

                Toast.makeText(context, "Post supprimé", Toast.LENGTH_SHORT).show();
            });

        } else {

            holder.btnSupprimer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return posts.size(); }

    static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView     tvUser, tvDate, tvDescription, tvTags, tvLikes, btnReport, btnSupprimer;
        ImageView    ivPost, ivLike;
        LinearLayout likeContainer;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser        = itemView.findViewById(R.id.tvUser);
            tvDate        = itemView.findViewById(R.id.tvDate);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTags        = itemView.findViewById(R.id.tvTags);
            tvLikes       = itemView.findViewById(R.id.tvLikes);
            btnReport     = itemView.findViewById(R.id.btnReport);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimer);
            ivPost        = itemView.findViewById(R.id.ivPost);
            ivLike        = itemView.findViewById(R.id.ivLike);
            likeContainer = itemView.findViewById(R.id.likeContainer);
        }
    }
}