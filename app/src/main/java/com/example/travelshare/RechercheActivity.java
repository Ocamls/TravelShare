package com.example.travelshare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RechercheActivity extends AppCompatActivity {

    private EditText etRecherche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        Button btnRetour    = findViewById(R.id.btnRetour);
        Button btnRecherche = findViewById(R.id.btnLancerRecherche);
        etRecherche         = findViewById(R.id.etRecherche);

        btnRetour.setOnClickListener(v -> finish());

        btnRecherche.setOnClickListener(v -> {
            String query = etRecherche.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(this, "Entrez un mot-clé", Toast.LENGTH_SHORT).show();
            } else {
                // TODO : brancher sur le back-end plus tard
                Toast.makeText(this, "Recherche : " + query, Toast.LENGTH_SHORT).show();
            }
        });
    }
}