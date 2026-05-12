package com.example.travelshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ConnexionActivity extends AppCompatActivity {

    private EditText nomUtilisateur;
    private EditText zoneMotDePasse;
    private Button btnConnexion;
    private TextView createAccountBtn;
    private TextView boutonAnonyme;

    private UtilisateurStorage utilisateurStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        nomUtilisateur = findViewById(R.id.txtIdentifiant);
        zoneMotDePasse = findViewById(R.id.txtMdp);
        btnConnexion = findViewById(R.id.btnConnexion);
        createAccountBtn = findViewById(R.id.btnPasDeCompte);
        boutonAnonyme = findViewById(R.id.btnContinuerAnonyme);

        utilisateurStorage = new UtilisateurStorage(this);

        SharedPreferences prefs = getSharedPreferences("travelshare_users", Context.MODE_PRIVATE);

        prefs.edit().clear().apply();

        utilisateurStorage.ajouterUtilisateur(new Utilisateur("test", "test", "monNom", "nomPrenom"));
        utilisateurStorage.ajouterUtilisateur(new Utilisateur("big_travel", "123", "Brico", "Thomas"));
        utilisateurStorage.ajouterUtilisateur(new Utilisateur("alice", "123", "Cola", "Alice"));

        for (Utilisateur u : utilisateurStorage.getTousUtilisateurs()) {
            android.util.Log.d("USERS", u.nomUtilisateur + " / " + u.motDePasse);
        }

        btnConnexion.setOnClickListener(v -> {
            String username = nomUtilisateur.getText().toString().trim();
            String password = zoneMotDePasse.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            Utilisateur u = utilisateurStorage.trouverUtilisateur(username);
            if (u != null) {
                utilisateurStorage.connecter(u.nomUtilisateur);

                List<Post> posts = new PostStorage(this).getTousPosts();
                List<String> ids = new ArrayList<>();
                for (int i = 0; i < posts.size(); i++) ids.add(String.valueOf(i));
                new LikeStorage(this).transfererLikesAnonymes(u.nomUtilisateur, ids);

                Intent intent = new Intent(this, FeedActivity.class);
                intent.putExtra("isConnected", true);
                intent.putExtra("username", u.nomUtilisateur);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
        });

        createAccountBtn.setOnClickListener(v ->
                startActivity(new Intent(this, CreationCompteActivity.class)));

        boutonAnonyme.setOnClickListener(v -> {
            utilisateurStorage.deconnecter();
            Intent intent = new Intent(this, FeedActivity.class);
            intent.putExtra("isConnected", false);
            startActivity(intent);
        });
    }
}