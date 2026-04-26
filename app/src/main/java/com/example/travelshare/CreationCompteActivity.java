package com.example.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreationCompteActivity extends AppCompatActivity {

    private EditText usernameZone, nomZone, prenomZone, passwordZone, confirmPasswordZone;
    private TextView connectBtn;
    private Button createAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_compte);

        usernameZone        = findViewById(R.id.txtIdentifiant);
        nomZone             = findViewById(R.id.txtNom);
        prenomZone          = findViewById(R.id.txtPrenom);
        passwordZone        = findViewById(R.id.txtMdp);
        confirmPasswordZone = findViewById(R.id.txtConfirmMdp);
        createAccountBtn    = findViewById(R.id.btnConnexion);
        connectBtn          = findViewById(R.id.btnAlreadyAccount);

        createAccountBtn.setOnClickListener(v -> {
            String username       = usernameZone.getText().toString();
            String nom            = nomZone.getText().toString();
            String prenom         = prenomZone.getText().toString();
            String password       = passwordZone.getText().toString();
            String confirmPassword = confirmPasswordZone.getText().toString();

            Log.d("CREATE_ACCOUNT", "Username: " + username);

            // Après inscription → mode connecté
            Intent intent = new Intent(this, FeedActivity.class);
            intent.putExtra("isConnected", true);
            startActivity(intent);
            Toast.makeText(this, "Compte créé, bienvenue !", Toast.LENGTH_SHORT).show();
        });

        connectBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ConnexionActivity.class));
            Toast.makeText(this, "J'ai déjà un compte", Toast.LENGTH_SHORT).show();
        });
    }
}