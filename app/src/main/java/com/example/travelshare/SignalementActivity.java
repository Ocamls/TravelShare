package com.example.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class SignalementActivity extends AppCompatActivity {

    private ListView listRaisons;
    private Spinner echelleGravite;
    private EditText etPseudo, etRechercheRaison, etRaisonUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalement);

        Button btnAnnuler = findViewById(R.id.btn_cancel_report);
        Button btnValider = findViewById(R.id.btn_valider_report);
        listRaisons         = findViewById(R.id.list_raisons);
        echelleGravite      = findViewById(R.id.echelle_gravite);
        etPseudo            = findViewById(R.id.et_pseudo);
        etRechercheRaison   = findViewById(R.id.et_recherche_raison);
        etRaisonUtilisateur = findViewById(R.id.et_raison_utilisateur);

        List<String> raisons = Arrays.asList(
                "Contenu inapproprié",
                "Harcèlement",
                "Spam",
                "Fausse information",
                "Contenu violent",
                "Atteinte à la vie privée"
        );
        ArrayAdapter<String> raisonsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, raisons);
        listRaisons.setAdapter(raisonsAdapter);

        List<String> gravites = Arrays.asList("Faible", "Modéré", "Grave", "Très grave");
        ArrayAdapter<String> graviteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, gravites);
        graviteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        echelleGravite.setAdapter(graviteAdapter);

        btnAnnuler.setOnClickListener(v -> finish());

        btnValider.setOnClickListener(v -> {
            Toast.makeText(this,
                    "Votre signalement a bien été pris en compte par nos équipes",
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, FeedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}