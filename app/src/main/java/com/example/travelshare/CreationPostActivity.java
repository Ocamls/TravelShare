package com.example.travelshare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreationPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_post);

        Button btnRetour  = findViewById(R.id.btnRetour);
        Button btnPublier = findViewById(R.id.btnPublier);

        btnRetour.setOnClickListener(v -> finish());

        btnPublier.setOnClickListener(v -> {
            Toast.makeText(this, "Publication envoyée !", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}