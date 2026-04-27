package com.example.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPosts;
    private PostAdapter adapter;
    private List<Post> posts;

    private ImageButton btnRecherche, btnNotifications, btnCreerPost, btnParametres;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        isConnected = getIntent().getBooleanExtra("isConnected", false);

        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);
        btnRecherche      = findViewById(R.id.btnRecherche);
        btnNotifications  = findViewById(R.id.btnNotifications);
        btnCreerPost      = findViewById(R.id.btnCreerPost);
        btnParametres     = findViewById(R.id.btnParametres);

        // Données fictives pour la démo
        posts = new ArrayList<>();
        posts.add(new Post("big_travel",   "18/03/2026 à 23h37", "Photo de la tour Eiffel...",     "#PARIS #TourEiffel #France #nuit", 47, R.drawable.image_tour));
        posts.add(new Post("travel_marie", "20/03/2026 à 14h10", "Vue incroyable sur les Alpes !", "#Alpes #Montagne #Nature",         32, R.drawable.image_tour));
        posts.add(new Post("wanderlust42", "22/03/2026 à 09h55", "Coucher de soleil à Lisbonne.",  "#Lisbonne #Portugal #Soleil",      61, R.drawable.image_tour));

        adapter = new PostAdapter(this, posts, isConnected);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPosts.setAdapter(adapter);

        // Boutons accessibles à tous
        btnRecherche.setOnClickListener(v -> {
            startActivity(new Intent(this, RechercheActivity.class));
        });

        btnParametres.setOnClickListener(v -> {
            Intent intent = new Intent(this, isConnected
                    ? ParametresActivity.class
                    : ParametresAnonymeActivity.class);
            startActivity(intent);
        });

        btnCreerPost.setOnClickListener(v -> {
            if (isConnected) {
                startActivity(new Intent(this, CreationPostActivity.class));
            } else {
                Toast.makeText(this,
                        "Connectez-vous pour publier une photo",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnNotifications.setOnClickListener(v -> {
            if (isConnected) {
                // TODO : NotificationsActivity
                Toast.makeText(this, "Notifications (bientôt disponible)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Connectez-vous pour accéder aux notifications",
                        Toast.LENGTH_SHORT).show();
            }
        });

        if (!isConnected) {
            btnCreerPost.setAlpha(0.4f);
            btnNotifications.setAlpha(0.4f);
        }
    }
}