package com.example.travelshare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InformationsPersonnellesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations_perso);

        Button btnRetour            = findViewById(R.id.btnRetour);
        TextView tvNomUtilisateur   = findViewById(R.id.txtIdentifiant);
        TextView tvNom               = findViewById(R.id.txtNom);
        TextView tvPrenom            = findViewById(R.id.txtPrenom);
        Button btnChangementInformations = findViewById(R.id.btnChangementInformations);
        UtilisateurStorage storage = new UtilisateurStorage(this);

        String auteur = getIntent().getStringExtra("auteur");
        if (auteur != null) tvNomUtilisateur.setText(auteur);

        Utilisateur user = storage.trouverUtilisateur(auteur);
        if (user != null) {
            tvNom.setText(user.nom);
            tvPrenom.setText(user.prenom);
        }


        btnRetour.setOnClickListener(v -> finish());

        btnChangementInformations.setOnClickListener(v -> {
            String nom = tvNom.getText().toString().trim();
            String prenom = tvPrenom.getText().toString().trim();

            if (nom.isEmpty() || prenom.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }else {
                storage.trouverUtilisateur(auteur).setNom(nom);
                storage.trouverUtilisateur(auteur).setPrenom(prenom);
            }
            finish();

        });



    }

}
