
package com.esir.toofast.activities.games;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.esir.toofast.R;
import com.esir.toofast.controllers.Player;

public class PageShakePhone extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acc;
    private float bestScore = 0;
    private float result = 0;

    ConstraintLayout consigne_before_countdown;
    ProgressBar progressBar;
    Player player;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_shake_phone);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        consigne_before_countdown = findViewById(R.id.layout_regle_shakePhone);
        progressBar = findViewById(R.id.progressBar_regle_shakePhone);
        player = (Player)getIntent().getSerializableExtra("Joueur");



        new CountDownTimer(5000, 1000) {

            //barreDeChargement.setProgress(barreDeChargement.getProgress() + 100);

            public void onTick(long millisUntilFinished) {
                result = bestScore;
            }

            public void onFinish() {
                Log.d("done!","score: "+result);
                Log.e("score",""+bestScore);
                if(result>=1000){ player.addScore(1000);}
                if(result<1000){ player.addScore((int)result*2);}
                startActivity(new Intent(getBaseContext(), player.nextChallenge()).putExtra("Joueur",player));

            }
        }.start();
    }



    //SENSOR PART

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float sumAxis = 0;

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];
            sumAxis += axisX+axisY+axisZ;
            if (bestScore < sumAxis){
                bestScore = sumAxis;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}