package com.example.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ParametresActivity extends AppCompatActivity {

    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        Button btnRetour = findViewById(R.id.btnRetour);
        Button btnInfosPerso = findViewById(R.id.btnInfosPerso);
        Button btnDeconnexion = findViewById(R.id.btnDeconnexion);

        username = getIntent().getStringExtra("username");


        btnRetour.setOnClickListener(v -> finish());

        btnInfosPerso.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiviteUtilisateurActivity.class);
            intent.putExtra("auteur", username);
            startActivity(intent);
        });

        btnDeconnexion.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConnexionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}