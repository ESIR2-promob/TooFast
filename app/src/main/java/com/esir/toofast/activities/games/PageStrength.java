//old speedclick

package com.esir.toofast.activities.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esir.toofast.R;
import com.esir.toofast.controllers.Player;
import com.esir.toofast.utils.PointCalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PageStrength extends AppCompatActivity {


    ConstraintLayout consigne_before_countdown;
    ProgressBar barreDeChargement;
    Button buttonStrength;

    Date currentTime;

    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_strength);

        consigne_before_countdown = findViewById(R.id.layout_regle_strength);
        barreDeChargement = findViewById(R.id.progressBar_regle_strength);
        buttonStrength = findViewById(R.id.button_strength);
        player = (Player)getIntent().getSerializableExtra("Joueur");


        new CountDownTimer(3000, 100) {
            public void onTick(long millisUntilFinished) {
                barreDeChargement.setProgress(barreDeChargement.getProgress() + 100);
            }

            public void onFinish() {
                consigne_before_countdown.setVisibility(View.INVISIBLE);
                buttonStrength.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Use the Builder class for convenient dialog construction

                        float actualTime = Calendar.getInstance().getTime().getTime() - currentTime.getTime();

                        player.addScore(PointCalculator.calculator(200.0f, 500.0f, 1000, actualTime, getApplicationContext()));

                        Toast.makeText(getApplicationContext(), String.format(Locale.FRANCE,"Jeu terminé (%.2f s)", ((float)(Calendar.getInstance().getTime().getTime() - currentTime.getTime())/1000)), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getBaseContext(), player.nextChallenge()).putExtra("Joueur",player));
                    }
                });
                int timeBeforeShowBang = genererIntAvecBorneEnMs(2000,8000);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    buttonStrength.setVisibility(View.VISIBLE);
                    currentTime = Calendar.getInstance().getTime();
                }, timeBeforeShowBang);


            }
        }.start();





    }

    private int genererIntAvecBorneEnMs(int borneInf, int borneSup){
        Random random = new Random();
        int nb;
        nb = borneInf + random.nextInt(borneSup-borneInf);
        return nb;
    }



}