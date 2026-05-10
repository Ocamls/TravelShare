package com.example.travelshare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ActiviteUtilisateurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activite_utilisateur);

        Button btnRetour            = findViewById(R.id.btnRetour);
        TextView tvNomUtilisateur   = findViewById(R.id.tvNomUtilisateur);
        TextView tvPublications     = findViewById(R.id.tvPublications);
        TextView tvLikes            = findViewById(R.id.tvLikes);
        RecyclerView recyclerView   = findViewById(R.id.recyclerViewUserPosts);

        String auteur = getIntent().getStringExtra("auteur");
        if (auteur != null) tvNomUtilisateur.setText(auteur);

        // Récupération des posts de cet auteur depuis le stockage local
        PostStorage storage = new PostStorage(this);
        List<Post> tousPosts = storage.getTousPosts();
        List<Post> postsUtilisateur = storage.getPostsUtilisateur(auteur);
        int totalLikes = 0;
        for (Post post : postsUtilisateur) {
            totalLikes += post.getLikes();
        }

        tvPublications.setText(String.valueOf(postsUtilisateur.size()));
        tvLikes.setText(String.valueOf(totalLikes));

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new PostGrilleAdapter(this, postsUtilisateur));

        btnRetour.setOnClickListener(v -> finish());
    }

}