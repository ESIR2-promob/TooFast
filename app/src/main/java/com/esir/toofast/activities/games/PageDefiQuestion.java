package com.esir.toofast.activities.games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.esir.toofast.R;
import com.esir.toofast.controllers.Player;
import com.esir.toofast.utils.PointCalculator;

import java.util.Random;

public class PageDefiQuestion extends AppCompatActivity {

    Button btnResp1, btnResp2, btnResp3, btnResp4;
    TextView question;
    Player player;

    MediaPlayer playerBad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_defi_question);

        player = (Player)getIntent().getSerializableExtra("Joueur");

        btnResp1 = findViewById(R.id.defiBouton1);
        btnResp2 = findViewById(R.id.defiBouton2);
        btnResp3 = findViewById(R.id.defiBouton3);
        btnResp4 = findViewById(R.id.defiBouton4);

        question = findViewById(R.id.defiQuestion);

        playerBad = MediaPlayer.create(getApplicationContext(), R.raw.loose_answer);

        int questionNumber = (int)(Math.random()*4+1);

        String questionResource = "question"+questionNumber;

        int questionId = getResources().getIdentifier(questionResource,"string",getPackageName());

        int goodAnswerId = getResources().getIdentifier("reponse"+(questionNumber)+1,"string",getPackageName());
        int wrongAnswer1Id = getResources().getIdentifier("reponse"+(questionNumber)+2,"string",getPackageName());
        int wrongAnswer2Id = getResources().getIdentifier("reponse"+(questionNumber)+3,"string",getPackageName());
        int wrongAnswer3Id = getResources().getIdentifier("reponse"+(questionNumber)+4,"string",getPackageName());

        int[] answers = new int[]{goodAnswerId,wrongAnswer1Id,wrongAnswer2Id,wrongAnswer3Id};

        Log.e("debug variable", "onCreate: "+ printTab(answers));
        shuffleTab(answers);

        question.setText(questionId);

        Log.e("debug variable", "onCreate: "+ printTab(answers));

        btnResp1.setText(answers[0]);
        btnResp2.setText(answers[1]);
        btnResp3.setText(answers[2]);
        btnResp4.setText(answers[3]);

        long begin = System.currentTimeMillis();

        btnResp1.setOnClickListener(v->{
            //player.majDefis1((btnResp1.getText()==getResources().getString(goodAnswerId)));
            nextActivity(begin,btnResp1.getText().toString(), goodAnswerId);
        });

        btnResp2.setOnClickListener(v->{
            //player.majDefis1((btnResp2.getText()==getResources().getString(goodAnswerId)));
            nextActivity(begin,btnResp2.getText().toString(), goodAnswerId);
        });

        btnResp3.setOnClickListener(v->{
            //player.majDefis1((btnResp3.getText()==getResources().getString(goodAnswerId)));
            nextActivity(begin,btnResp3.getText().toString(), goodAnswerId);
        });

        btnResp4.setOnClickListener(v->{
            //player.majDefis1((btnResp4.getText()==getResources().getString(goodAnswerId)));
            nextActivity(begin,btnResp4.getText().toString(), goodAnswerId);
        });



    }

    private void nextActivity(long begin, String response, int goodAnswerId){

        long result = System.currentTimeMillis() - begin;

        //Si la réponse est bonne alors on calcul les points sinon c'est 0
        if(response.equals(getResources().getString(goodAnswerId))){
            player.addScore(PointCalculator.calculator(2.0f,5.0f,1000,(float)(result/1000.0), getApplicationContext()));
        }
        else{
            player.addScore(0);
            playerBad.start();
        }

        Intent pageResultat = new Intent(this,player.nextChallenge()).putExtra("Joueur",player);
        startActivity(pageResultat);
    }

    private static void shuffleTab(int[] tab){

        Random random = new Random();

        for(int i = 0; i < tab.length; i++){
            int index = random.nextInt(tab.length);
            Log.e("debug variable", "shuffleTab: "+index );
            int temp = tab[index];
            tab[index] = tab[i];
            tab[i] = temp;
        }
    }

    private static String printTab(int[] tab){
        String s = "";
        for(int i = 0; i < tab.length; i++){
            s = s + tab[i] + " | ";
        }
        return s;
    }

    private void playSound() {

    }


}