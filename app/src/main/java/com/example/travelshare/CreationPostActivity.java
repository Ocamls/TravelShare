package com.example.travelshare;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreationPostActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private String username;
    private Uri photoUri;
    private EditText etRechercheTag;
    private ListView listTagsResultats, listTagsSelectionnes;
    private final List<String> tagsDisponibles = new ArrayList<>();
    private final List<String> resultatsTags = new ArrayList<>();
    private final List<String> tagsSelectionnes = new ArrayList<>();
    private ArrayAdapter<String> adapterResultatsTags, adapterTagsSelectionnes;
    private final List<String> groupes = Arrays.asList("Aventuriers", "Plages", "Montagne", "City trips");
    private final List<String> interets = Arrays.asList("Nature", "Culture", "Gastronomie", "Sport", "Détente");
    private ActivityResultLauncher<Intent> galerielauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_post);
        Button btnRetour = findViewById(R.id.btnRetour);
        Button btnGalerie = findViewById(R.id.btnGalerie);
        Button btnAppareilPhoto = findViewById(R.id.btnAppareilPhoto);
        Button btnPublier = findViewById(R.id.btnPublier);

        CheckBox checkPublique = findViewById(R.id.checkPublique);

        ListView listGroupes = findViewById(R.id.listGroupes);

        EditText etDescriptionLieu = findViewById(R.id.etDescriptionLieu);
        EditText etDescriptionPost = findViewById(R.id.etDescriptionPost);

        etRechercheTag = findViewById(R.id.etRechercheTag);
        listTagsResultats = findViewById(R.id.listTagsResultats);
        listTagsSelectionnes = findViewById(R.id.listTagsSelectionnes);

        username = getIntent().getStringExtra("username");

        if (savedInstanceState != null) {
            String savedPhoto = savedInstanceState.getString("photo_uri");
            if (savedPhoto != null) photoUri = Uri.parse(savedPhoto);
        }

        listGroupes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, groupes));

        chargerTagsCSV();

        adapterResultatsTags = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultatsTags);
        adapterTagsSelectionnes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagsSelectionnes);

        listTagsResultats.setAdapter(adapterResultatsTags);
        listTagsSelectionnes.setAdapter(adapterTagsSelectionnes);

        etRechercheTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rechercherTags(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listTagsResultats.setOnItemClickListener((parent, view, position, id) -> {
            String tag = resultatsTags.get(position);

            if (!tagsSelectionnes.contains(tag)) {
                tagsSelectionnes.add(tag);
                adapterTagsSelectionnes.notifyDataSetChanged();
            }
        });
        listTagsSelectionnes.setOnItemClickListener((parent, view, position, id) -> {
            tagsSelectionnes.remove(position);
            adapterTagsSelectionnes.notifyDataSetChanged();
        });

        galerielauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                Uri uriSource = result.getData().getData();
                photoUri = copierImageEnLocal(uriSource);

                if (photoUri != null) {
                    Toast.makeText(this, "Photo sélectionnée", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Erreur lors de la sélection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success) Toast.makeText(this, "Photo prise", Toast.LENGTH_SHORT).show();
        });

        btnRetour.setOnClickListener(v -> finish());

        btnGalerie.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galerielauncher.launch(intent);
        });

        btnAppareilPhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                lancerCamera();
            }
        });

        btnPublier.setOnClickListener(v -> publier(checkPublique, listGroupes, listTagsSelectionnes, etDescriptionLieu, etDescriptionPost));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (photoUri != null) outState.putString("photo_uri", photoUri.toString());
    }

    private void rechercherTags(String query) {
        resultatsTags.clear();
        query = query.toLowerCase().trim();
        if (query.isEmpty()) {
            adapterResultatsTags.notifyDataSetChanged();
            return;
        }
        for (String tag : tagsDisponibles) {
            if (tag.toLowerCase().contains(query)) resultatsTags.add(tag);
        }
        adapterResultatsTags.notifyDataSetChanged();
    }

    private void chargerTagsCSV() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("tags.csv")));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                ligne = ligne.trim();
                if (!ligne.isEmpty()) tagsDisponibles.add(ligne);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lancerCamera() {
        try {
            File fichier = creerFichierPhoto();
            photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", fichier);
            cameraLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(this, "Impossible de créer le fichier photo", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri copierImageEnLocal(Uri uriSource) {
        try {
            String horodatage = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            File dossier = getFilesDir();
            File destination = new File(dossier, "POST_" + horodatage + ".jpg");

            java.io.InputStream in = getContentResolver().openInputStream(uriSource);
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
        String horodatage = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        File dossier = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile("PHOTO_" + horodatage, ".jpg", dossier);
    }

    private void publier(CheckBox checkPublique, ListView listGroupes, ListView listInterets, EditText etDescriptionLieu, EditText etDescriptionPost) {
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
        StringBuilder builderTags = new StringBuilder();
        for (String tag : tagsSelectionnes) builderTags.append("#").append(tag).append(" ");
        String tagsTexte = builderTags.toString().trim();
        String dateDuJour = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Post post = new Post(username, dateDuJour, descLieu, tagsTexte, 4);
        post.setPhotoUri(photoUri.toString());
        new PostStorage(this).ajouterPost(post);
        Intent result = new Intent();
        result.putExtra("nouveau_post", true);
        setResult(RESULT_OK, result);
        Toast.makeText(this, "Publication envoyée !", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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