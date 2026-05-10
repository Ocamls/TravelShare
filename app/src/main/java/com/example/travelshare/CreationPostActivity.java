package com.example.travelshare;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreationPostActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private String username;
    private Uri photoUri;

    // Données fictives — à remplacer par des appels réseau
    private final List<String> groupes = Arrays.asList("Aventuriers", "Plages", "Montagne", "City trips");
    private final List<String> interets = Arrays.asList("Nature", "Culture", "Gastronomie", "Sport", "Détente");

    // Launchers pour les résultats d'activités
    private ActivityResultLauncher<Intent> galerielauncher;
    private ActivityResultLauncher<Uri>    cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_post);

        // --- Vues ---
        Button   btnRetour          = findViewById(R.id.btnRetour);
        Button   btnGalerie         = findViewById(R.id.btnGalerie);
        Button   btnAppareilPhoto   = findViewById(R.id.btnAppareilPhoto);
        Button   btnPublier         = findViewById(R.id.btnPublier);
        CheckBox checkPublique      = findViewById(R.id.checkPublique);
        ListView listGroupes        = findViewById(R.id.listGroupes);
        ListView listInterets       = findViewById(R.id.listInterets);
        EditText etDescriptionLieu  = findViewById(R.id.etDescriptionLieu);
        EditText etDescriptionPost  = findViewById(R.id.etDescriptionPost);

        username = getIntent().getStringExtra("username");


        // --- Adaptateurs des listes ---
        ArrayAdapter<String> adapterGroupes = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_multiple_choice, groupes);
        listGroupes.setAdapter(adapterGroupes);

        ArrayAdapter<String> adapterInterets = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_multiple_choice, interets);
        listInterets.setAdapter(adapterInterets);

        // --- Launchers ---
        galerielauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uriSource = result.getData().getData();
                        // Copie l'image dans le stockage interne pour la rendre persistante
                        photoUri = copierImageEnLocal(uriSource);
                        if (photoUri != null) {
                            Toast.makeText(this, "Photo sélectionnée", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Erreur lors de la sélection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success) {
                        Toast.makeText(this, "Photo prise", Toast.LENGTH_SHORT).show();
                    }
                });

        // --- Boutons ---
        btnRetour.setOnClickListener(v -> finish());

        btnGalerie.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galerielauncher.launch(intent);
        });

        btnAppareilPhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                lancerCamera();
            }
        });

        btnPublier.setOnClickListener(v -> publier(
                checkPublique, listGroupes, listInterets,
                etDescriptionLieu, etDescriptionPost));
    }

    // --- Caméra ---
    private void lancerCamera() {
        try {
            File fichier = creerFichierPhoto();
            photoUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    fichier);
            cameraLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(this, "Impossible de créer le fichier photo", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri copierImageEnLocal(Uri uriSource) {
        try {
            // Crée un fichier destination dans le stockage interne
            String horodatage = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date());
            File dossier = getFilesDir(); // stockage interne privé, persistant
            File destination = new File(dossier, "POST_" + horodatage + ".jpg");

            // Copie flux par flux
            java.io.InputStream in  = getContentResolver().openInputStream(uriSource);
            java.io.OutputStream out = new java.io.FileOutputStream(destination);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);
            in.close();
            out.close();

            return Uri.fromFile(destination);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private File creerFichierPhoto() throws IOException {
        String horodatage = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        File dossier = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("PHOTO_" + horodatage, ".jpg", dossier);
    }

    // --- Publication ---
    private void publier(CheckBox checkPublique, ListView listGroupes,
                         ListView listInterets,
                         EditText etDescriptionLieu, EditText etDescriptionPost) {

        String descLieu = etDescriptionLieu.getText().toString();
        String descPost = etDescriptionPost.getText().toString();

        if (photoUri == null) {
            Toast.makeText(this, "Veuillez sélectionner ou prendre une photo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descLieu.isEmpty()) {
            etDescriptionLieu.setError("La description du lieu est requise");
            return;
        }

        String dateDuJour = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        // Créer et sauvegarder le post en local
        Post post = new Post(username,dateDuJour,descLieu,"tags",0, 4);
        post.setPhotoUri(photoUri.toString());
        new PostStorage(this).ajouterPost(post);

        Intent result = new Intent();
        result.putExtra("nouveau_post", true);
        setResult(RESULT_OK, result);

        Toast.makeText(this, "Publication envoyée !", Toast.LENGTH_SHORT).show();
        finish();
    }
    // --- Résultat de la demande de permission ---
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lancerCamera();
            } else {
                Toast.makeText(this, "Permission caméra refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}