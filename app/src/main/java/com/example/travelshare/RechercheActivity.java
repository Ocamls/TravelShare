package com.example.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RechercheActivity extends AppCompatActivity {

    private EditText etRecherche;
    private ListView listResultats;

    private final List<String> tags = new ArrayList<>();
    private final List<String> resultats = new ArrayList<>();

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        Button btnRetour    = findViewById(R.id.btnRetour);
        Button btnRecherche = findViewById(R.id.btnLancerRecherche);

        etRecherche   = findViewById(R.id.etRecherche);
        listResultats = findViewById(R.id.listResultats);

        chargerTags();

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,resultats);

        listResultats.setAdapter(adapter);

        btnRetour.setOnClickListener(v -> finish());

        btnRecherche.setOnClickListener(v -> lancerRecherche());

        listResultats.setOnItemClickListener((parent, view, position, id) -> {

            String tagSelectionne = resultats.get(position);

            Intent intent = new Intent(this, FeedActivity.class);

            intent.putExtra("tag", tagSelectionne);

            intent.putExtra("isConnected",getIntent().getBooleanExtra("isConnected", false));

            intent.putExtra("username",getIntent().getStringExtra("username"));

            startActivity(intent);
        });
    }

    private void lancerRecherche() {

        String query = etRecherche.getText().toString().trim().toLowerCase();
        if (query.isEmpty()) {
            Toast.makeText(this,"Entrez un mot-clé",Toast.LENGTH_SHORT).show();
            return;
        }
        resultats.clear();
        for (String tag : tags) {
            if (tag.toLowerCase().contains(query)) {
                resultats.add(tag);
            }
        }
        adapter.notifyDataSetChanged();
        if (resultats.isEmpty()) {
            Toast.makeText(this,"Aucun résultat",Toast.LENGTH_SHORT).show();
        }
    }

    private void chargerTags() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("tags.csv"))
            );
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                ligne = ligne.trim();
                if (!ligne.isEmpty()) {
                    tags.add(ligne);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Erreur lecture CSV",Toast.LENGTH_SHORT).show();
        }
    }
}