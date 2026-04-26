package com.example.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ParametresAnonymeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres_anonyme);

        Button btnRetour        = findViewById(R.id.btnRetour);
        Button btnConvertir     = findViewById(R.id.btnConvertir);
        Button btnCreerCompte   = findViewById(R.id.btnCreerCompte);
        Button btnAccueil       = findViewById(R.id.btnAccueil);

        btnRetour.setOnClickListener(v -> finish());

        btnConvertir.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreationCompteActivity.class);
            startActivity(intent);
        });

        btnCreerCompte.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreationCompteActivity.class);
            startActivity(intent);
        });

        btnAccueil.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConnexionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}