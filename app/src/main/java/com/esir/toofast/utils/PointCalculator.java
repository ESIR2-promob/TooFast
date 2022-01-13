package com.esir.toofast.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.esir.toofast.R;

public class PointCalculator {

    public static int calculator(float minimumTime, float maximumTime, int maximumPoint, float actualTime, Context context){

        MediaPlayer playerGood = MediaPlayer.create(context, R.raw.win_answer);
        MediaPlayer playerBad = MediaPlayer.create(context, R.raw.loose_answer);

        if(actualTime<minimumTime){
            playerGood.start();
            return maximumPoint;
        }
        else if(actualTime>maximumTime){
            playerBad.start();
            return 0;
        }
        else{ //La réduction de point se fait de manière linéaire entre le temps minimum et le temps maximum

            Log.d("PointCalculator", "calculator: calcul des points");
            float a = maximumPoint/(maximumTime - minimumTime);
            Log.d("PointCalculator", "calculator: valeur de la pente : "+a);

            float timeDifference = actualTime-minimumTime;
            Log.d("PointCalculator", "calculator: valeur de la différence : "+timeDifference);
            playerGood.start();
            return (int)(maximumPoint - (a * timeDifference));

        }
    }

}
