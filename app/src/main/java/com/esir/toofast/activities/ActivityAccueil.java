package com.esir.toofast.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esir.toofast.R;

public class ActivityAccueil extends AppCompatActivity {

    Button btnMulti,btnSolo,btnEntrainement,btnParametres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_accueil);

        btnMulti = findViewById(R.id.button_multijoueurs);
        btnSolo = findViewById(R.id.button_solo);
        btnEntrainement = findViewById(R.id.button_entrainement);
        btnParametres = findViewById(R.id.button_paramètres);

        // Liste des permissions à demander à l'utilisateur
        String[] permissions = new String[]{
                "android.permission.INTERNET",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.CHANGE_WIFI_STATE",
                "android.permission.CHANGE_NETWORK_STATE",

        };

        // On regarde si on possède déjà ces permissions
        if(hasPermissions(permissions))
        {
            btnMulti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pageMulti = new Intent(ActivityAccueil.this, ActivityMulti.class);
                    startActivity(pageMulti);
                }
            });
            btnSolo.setOnClickListener(v -> {
                Intent pageMulti = new Intent(ActivityAccueil.this, ActivitySolo.class);
                startActivity(pageMulti);
            });
            btnEntrainement.setOnClickListener(v -> {
                Intent pageMulti = new Intent(ActivityAccueil.this, ActivityPractice.class);
                startActivity(pageMulti);
            });
            btnParametres.setOnClickListener(v -> {
                Intent pageMulti = new Intent(ActivityAccueil.this, ActivitySettings.class);
                startActivity(pageMulti);
            });
        }
        // Si on ne les possède pas alors on en fait la demande à l'utilisateur
        else{
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btnMulti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pageMulti = new Intent(ActivityAccueil.this, ActivityMulti.class);
                        startActivity(pageMulti);
                    }
                });
                btnSolo.setOnClickListener(v -> {
                    Intent pageMulti = new Intent(ActivityAccueil.this, ActivitySolo.class);
                    startActivity(pageMulti);
                });
                btnEntrainement.setOnClickListener(v -> {
                    Intent pageMulti = new Intent(ActivityAccueil.this, ActivityPractice.class);
                    startActivity(pageMulti);
                });
                btnParametres.setOnClickListener(v -> {
                    Intent pageMulti = new Intent(ActivityAccueil.this, ActivitySettings.class);
                    startActivity(pageMulti);
                });
            } else {
                Toast.makeText(this, "Veuillez accepter les permissions pour pouvoir acceder aux contacts / envoyer des sms / passer des appels  ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                Log.e("debug", "hasPermissions: "+permission);
                if (ActivityCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("debug", "false");
                    return false;
                }
                Log.e("debug", "true");
            }
        }
        return true;
    }
}