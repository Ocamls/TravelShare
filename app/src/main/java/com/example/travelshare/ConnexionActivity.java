package com.example.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConnexionActivity extends AppCompatActivity {

    private EditText nomUtilisateur;
    private EditText zoneMotDePasse;
    private Button btnConnexion;
    private TextView createAccountBtn;
    private TextView boutonAnonyme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        nomUtilisateur   = findViewById(R.id.txtIdentifiant);
        zoneMotDePasse   = findViewById(R.id.txtMdp);
        btnConnexion     = findViewById(R.id.btnConnexion);
        createAccountBtn = findViewById(R.id.btnPasDeCompte);
        boutonAnonyme    = findViewById(R.id.btnContinuerAnonyme);

        // Connexion → mode connecté
        btnConnexion.setOnClickListener(v -> {
            String username = nomUtilisateur.getText().toString();
            String password = zoneMotDePasse.getText().toString();

            if (username.equals("Test") && password.equals("testMDP")) {
                Intent intent = new Intent(this, FeedActivity.class);
                intent.putExtra("isConnected", true);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
        });

        // Créer un compte → mode connecté après inscription
        createAccountBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, CreationCompteActivity.class));
        });

        // Continuer anonyme → mode anonyme
        boutonAnonyme.setOnClickListener(v -> {
            Intent intent = new Intent(this, FeedActivity.class);
            intent.putExtra("isConnected", false);
            startActivity(intent);
        });
    }
}