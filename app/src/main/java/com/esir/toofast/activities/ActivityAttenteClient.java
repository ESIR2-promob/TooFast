package com.esir.toofast.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.esir.toofast.R;
import com.esir.toofast.communication.AsyncClient;
import com.esir.toofast.controllers.Player;

import java.util.Locale;


public class ActivityAttenteClient extends AppCompatActivity {

    private Player player;
    private TextView majDefiTermine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_attente_client);

        player = (Player)getIntent().getSerializableExtra("Joueur");

        majDefiTermine = findViewById(R.id.Affichage_defi_termine_client);
        majDefiTermine.setText(String.format(Locale.FRANCE,"Défi %d / 3 terminé !", player.getCurrentDefiIndex()));

        if(player.isLastPage()){
            new AsyncClient(this).execute("SCORE;"+player.getScore());
        }
        else{
            if(!player.isMultiplayerInit()){
                new AsyncClient(this).execute("PREPARE");
            }
            else{
                new AsyncClient(this).execute("READY");
            }
        }
    }

    public void onAsyncClientResponse(String serverResponse){
        if ("OKREADY".equals(serverResponse)) {
            startActivity(new Intent(this, player.nextChallenge()).putExtra("Joueur", player));
        } else {
            String[] parts = serverResponse.split(";");

            if (parts[0].equals("PREPARE")) {
                player.shuffleChallenges(Integer.parseInt(parts[1]), "CLIENT");
                player.setMultiplayerAsInit();
                new AsyncClient(this).execute("READY");
            } else if (parts[0].equals("SCORE")) {
                player.setEnemyPoint(Integer.parseInt(parts[1]));
                startActivity(new Intent(this, player.nextChallenge()).putExtra("Joueur", player));
            }
        }
    }
}