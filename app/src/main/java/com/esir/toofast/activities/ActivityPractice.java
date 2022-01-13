package com.esir.toofast.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.esir.toofast.R;
import com.esir.toofast.activities.games.PageStrength;
import com.esir.toofast.controllers.Player;
import com.esir.toofast.activities.games.PageShakePhone;
import com.esir.toofast.activities.games.PageDefiQuestion;
import com.esir.toofast.activities.games.PageClickBall;
import com.esir.toofast.activities.games.PageFlipPhone;
import com.esir.toofast.activities.games.PageLabyrinthe;

public class ActivityPractice extends AppCompatActivity {

    Button defis1, defis2, defis3, defis4, defis5, defis6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_entrainement);

        Player player = new Player("", Player.ENTRAINEMENT);

        defis1 = findViewById(R.id.button_challenge1);
        defis2 = findViewById(R.id.button_challenge2);
        defis3 = findViewById(R.id.button_challenge3);
        defis4 = findViewById(R.id.button_challenge4);
        defis5 = findViewById(R.id.button_challenge5);
        defis6 = findViewById(R.id.button_challenge6);

        defis1.setOnClickListener(v -> {startActivity(new Intent(this, PageFlipPhone.class).putExtra("Joueur",player));});
        defis2.setOnClickListener(v -> {startActivity(new Intent(this, PageDefiQuestion.class).putExtra("Joueur",player));});
        defis3.setOnClickListener(v -> {startActivity(new Intent(this, PageClickBall.class).putExtra("Joueur",player));});
        defis4.setOnClickListener(v -> {startActivity(new Intent(this, PageLabyrinthe.class).putExtra("Joueur",player));});
        defis5.setOnClickListener(v -> {startActivity(new Intent(this, PageShakePhone.class).putExtra("Joueur",player));});
        defis6.setOnClickListener(v -> {startActivity(new Intent(this, PageStrength.class).putExtra("Joueur",player));});

    }
}