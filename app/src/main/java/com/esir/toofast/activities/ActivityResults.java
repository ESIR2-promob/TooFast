package com.esir.toofast.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esir.toofast.R;
import com.esir.toofast.controllers.Player;
import com.esir.toofast.utils.SocketHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ActivityResults extends AppCompatActivity {

    Player player;
    TextView resultat;
    TextView bestPoints;
    Button accueil;
    MediaPlayer playerBad;
    MediaPlayer playerGood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_resultat);

        player = (Player)getIntent().getSerializableExtra("Joueur");
        playerGood = MediaPlayer.create(getApplicationContext(), R.raw.game_win);
        playerBad = MediaPlayer.create(getApplicationContext(), R.raw.game_over);

        resultat = findViewById(R.id.textview_afficher_resultat);
        accueil = findViewById(R.id.buttonResultat_accueil);
        bestPoints = findViewById(R.id.textview_affiche_meilleur_resultat);

        if(player!=null) resultat.setText(getString(R.string.scoreDisplay, player.getScore()));

        switch(player.getMode()){
            case "MULTIJOUEUR":
                resultat.append("\nL'autre joueur a obtenu "+player.getEnemyPoint()+" points");
                if(player.getEnemyPoint()>player.getScore()){
                    resultat.append("\nDéfaite");
                    playerBad.start();

                }
                else if(player.getEnemyPoint()<player.getScore()){
                    resultat.append("\nVictoire");
                    playerGood.start();
                }
                else{
                    resultat.append("\nEgalité");
                }

                player.setMultiplayerNotInit();
                SocketHandler.isInitialized = false;
                try {
                    SocketHandler.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case "SOLO":
                String FILENAME = "bestScore";
                int bestScore;
                File file = getBaseContext().getFileStreamPath(FILENAME);
                FileOutputStream fos;
                FileInputStream fis;
                try {
                    if(file.exists()){
                        fis = getBaseContext().openFileInput(FILENAME);
                        String scoreStored = readFile(fis);
                        fis.close();
                        bestScore = (scoreStored.equals("")) ? 0 : Integer.parseInt(scoreStored.replace("\n",""));
                    }else{
                        bestScore = 0;
                    }



                    if(player.getScore() > bestScore){
                        String newScore = String.valueOf(player.getScore());
                        fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                        fos.write(newScore.getBytes(StandardCharsets.UTF_8));
                        fos.close();
                    }

                    bestPoints.setText(getString(R.string.meilleur_score, bestScore));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }


        accueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityResults.this, ActivityAccueil.class));
            }
        });


    }

    private String readFile(FileInputStream fis){
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
            e.printStackTrace();
        } finally {
            return stringBuilder.toString();
        }
    }
}