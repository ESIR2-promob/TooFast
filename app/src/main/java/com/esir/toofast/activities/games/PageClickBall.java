package com.esir.toofast.activities.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esir.toofast.R;
import com.esir.toofast.controllers.Player;
import com.esir.toofast.utils.PointCalculator;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PageClickBall extends AppCompatActivity {

    Button fastButton;
    Player player;
    ProgressBar progressBar;
    ConstraintLayout consigne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_click_ball);

        AtomicInteger userClick = new AtomicInteger(10);

        player = (Player)getIntent().getSerializableExtra("Joueur");

        progressBar = findViewById(R.id.progressbar_regles_clickball);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        fastButton = findViewById(R.id.fastbutton);
        consigne = findViewById(R.id.layout_regles_clickball);

        Random random = new Random();

        new CountDownTimer(4000, 100) {
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressBar.getProgress() + 100);
            }

            @Override
            public void onFinish() {
                float dip = 50f;
                Resources r = getResources();
                float px = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dip,
                        r.getDisplayMetrics()
                );
                consigne.setVisibility(View.INVISIBLE);
                fastButton.setVisibility(View.VISIBLE);
                long begin = System.currentTimeMillis();
                fastButton.setOnClickListener(v ->{
                    //Pour créer le nouvel emplacement du bouton, on créé des coordonées au hasard
                    float dx = (random.nextFloat() * width) - px;
                    float dy = (random.nextFloat() * height) - px;

                    if(dx<0.0) dx = 0.0f;
                    if(dy<0.0) dx = 0.0f;

                    fastButton.animate().x(dx).y(dy).setDuration(150).rotation(fastButton.getRotation()+360*2).start();

                    userClick.set(userClick.get() - 1);

                    if(userClick.get() ==0) endingActivity(begin);
                });
            }
        }.start();


    }

    private void endingActivity(long begin){

        long result = System.currentTimeMillis() - begin;

        Toast.makeText(this,String.format(Locale.FRANCE,"Jeu terminé (%.3f s)",(float)(result/1000.0)),Toast.LENGTH_LONG).show();

        player.addScore(PointCalculator.calculator(1.0f,10.0f,1000,(float)(result/1000.0), getApplicationContext()));

        startActivity(new Intent(this, player.nextChallenge()).putExtra("Joueur",player));
    }
}