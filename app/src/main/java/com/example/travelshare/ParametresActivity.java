package com.example.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ParametresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        Button btnRetour = findViewById(R.id.btnRetour);
        Button btnInfosPerso = findViewById(R.id.btnInfosPerso);
        Button btnDeconnexion = findViewById(R.id.btnDeconnexion);

        btnRetour.setOnClickListener(v -> finish());

        btnInfosPerso.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiviteUtilisateurActivity.class);
            startActivity(intent);
        });

        btnDeconnexion.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConnexionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}