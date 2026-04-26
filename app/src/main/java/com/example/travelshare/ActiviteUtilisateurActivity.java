package com.example.travelshare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActiviteUtilisateurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activite_utilisateur);

        Button btnRetour = findViewById(R.id.btnRetour);
        TextView tvNomUtilisateur = findViewById(R.id.tvNomUtilisateur);

        // Récupère le nom passé en extra (depuis le feed par ex.)
        String auteur = getIntent().getStringExtra("auteur");
        if (auteur != null && tvNomUtilisateur != null) {
            tvNomUtilisateur.setText(auteur);
        }

        if (btnRetour != null) {
            btnRetour.setOnClickListener(v -> finish());
        }
    }
}