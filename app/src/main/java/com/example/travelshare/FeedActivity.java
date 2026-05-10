    package com.example.travelshare;

    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.widget.ImageButton;
    import android.widget.Toast;

    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.contract.ActivityResultContracts;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.ArrayList;
    import java.util.List;

    public class FeedActivity extends AppCompatActivity {
        private ActivityResultLauncher<Intent> creationPostLauncher;

        private String username;
        private RecyclerView recyclerViewPosts;
        private PostAdapter adapter;
        private List<Post> posts;

        private ImageButton btnRecherche, btnNotifications, btnCreerPost, btnParametres, btnProfil;
        private boolean isConnected;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feed);
            creationPostLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            // Recharger les posts depuis le stockage local
                            List<Post> postsLocaux = new PostStorage(FeedActivity.this).getTousPosts();                            posts.addAll(0, postsLocaux); // ajoute en tête de feed
                            adapter.notifyDataSetChanged();
                        }
                    });
            username = getIntent().getStringExtra("username");
            isConnected = getIntent().getBooleanExtra("isConnected", false);

            recyclerViewPosts = findViewById(R.id.recyclerViewPosts);
            btnRecherche      = findViewById(R.id.btnRecherche);
            btnNotifications  = findViewById(R.id.btnNotifications);
            btnCreerPost      = findViewById(R.id.btnCreerPost);
            btnParametres     = findViewById(R.id.btnParametres);
            btnProfil         = findViewById(R.id.btnProfil);

            //données fictives car je n'ai pas de mode connecter pour le moment
            PostStorage storage = new PostStorage(this);
            SharedPreferences prefs = getSharedPreferences("travelshare_prefs", Context.MODE_PRIVATE);
            if (!prefs.getBoolean("init_done", false)) {
                storage.ajouterPost(new Post("big_travel", "24/03/2026 à 17h42", "Coucher de soleil à Lisbonne.",  "#Lisbonne #Portugal #Soleil",      21, R.drawable.ville2));
                storage.ajouterPost(new Post("big_travel", "22/03/2026 à 09h55", "Coucher de soleil à Lisbonne.",  "#Lisbonne #Portugal #Soleil",      61, R.drawable.paysage1));
                storage.ajouterPost(new Post("big_travel", "20/03/2026 à 14h10", "Vue incroyable sur les Alpes !", "#Alpes #Montagne #Nature",         32, R.drawable.ville1));
                storage.ajouterPost(new Post("big_travel", "18/03/2026 à 23h37", "Photo de la tour Eiffel...",     "#PARIS #TourEiffel #France #nuit", 47, R.drawable.image_tour));
                prefs.edit().putBoolean("init_done", true).apply();
            }

            posts = new ArrayList<>(storage.getTousPosts());

            posts = new ArrayList<>(storage.getTousPosts());
            adapter = new PostAdapter(this, posts, isConnected);
            recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewPosts.setAdapter(adapter);

            btnRecherche.setOnClickListener(v -> {
                startActivity(new Intent(this, RechercheActivity.class));
            });

            btnProfil.setOnClickListener(v -> {
                        if (isConnected) {
                            Intent intent = new Intent(this, ActiviteUtilisateurActivity.class);
                            intent.putExtra("auteur", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this,
                                    "Connectez-vous pour accéder au profil",
                                    Toast.LENGTH_SHORT).show();
                        }
            });

            btnParametres.setOnClickListener(v -> {
                Intent intent = new Intent(this, isConnected
                        ? ParametresActivity.class
                        : ParametresAnonymeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            });

            btnCreerPost.setOnClickListener(v -> {
                if (isConnected) {
                    Intent intent = new Intent(this, CreationPostActivity.class);
                    intent.putExtra("username", username);
                    creationPostLauncher.launch(intent);
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