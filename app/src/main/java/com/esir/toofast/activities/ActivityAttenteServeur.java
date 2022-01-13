package com.esir.toofast.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.esir.toofast.R;
import com.esir.toofast.communication.AsyncServer;
import com.esir.toofast.controllers.Player;

import java.util.Locale;

public class ActivityAttenteServeur extends AppCompatActivity {

    private TextView majDefiTermine;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_attente_serveur);

        player = (Player) getIntent().getSerializableExtra("Joueur");


        majDefiTermine = findViewById(R.id.Affichage_defi_termine_serveur);
        majDefiTermine.setText(String.format(Locale.FRANCE,"Défi %d / 3 terminé !", player.getCurrentDefiIndex()));

        if(!player.isMultiplayerInit()){
            player.shuffleChallenges(player.getRandomSeed(),"SERVER");
            player.setMultiplayerAsInit();
        }

        new AsyncServer(this).execute(player.getRandomSeed()+"",player.getScore()+"");

    }

    public void onAsyncServerResponse(int enemyPoint){
        player.setEnemyPoint(enemyPoint);
        startActivity(new Intent(this, player.nextChallenge()).putExtra("Joueur",player));
    }
}