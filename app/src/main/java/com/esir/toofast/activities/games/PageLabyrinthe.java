package com.esir.toofast.activities.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.esir.toofast.R;
import com.esir.toofast.controllers.Player;

public class PageLabyrinthe extends AppCompatActivity implements SensorEventListener {

    private Sensor myGyroscope;
    private SensorManager sensorManager = null;

    private BallView ballView;
    private float cordY;
    private float cordZ;
    private Player player;
    private ProgressBar barreDeChargement;
    private ConstraintLayout consigne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_labyrinthe);
        ballView = findViewById(R.id.ballview);
        ballView.setVisibility(View.INVISIBLE);


        player = (Player)getIntent().getSerializableExtra("Joueur");
        consigne = findViewById(R.id.consigne_moveball_layout);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        myGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        barreDeChargement = findViewById(R.id.progressBar_regle_labyrinthe);



        new CountDownTimer(4000, 100) {
            public void onTick(long millisUntilFinished) {
                barreDeChargement.setProgress(barreDeChargement.getProgress() + 100);
            }

            public void onFinish() {
                consigne.setVisibility(View.INVISIBLE);
                ballView.setVisibility(View.VISIBLE);
                runHandler();
            }
        }.start();

    }

    private void runHandler() {
        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(cordY < 0){
                        // on divise par 3 les cordY pour faire un effet lors de la descente de la balle
                        ballView.moveXAndY((int) -cordY/3, BallView.AXE_Y_BAS);
                    }
                    if (cordZ<0){
                        ballView.moveXAndY((int) -cordZ, BallView.AXE_X_DROITE);
                    }else{
                        ballView.moveXAndY((int) cordZ, BallView.AXE_X_GAUCHE);
                    }
                    ballView.invalidate();
                    handler.postDelayed(this, 1);
                }
            }, 1);
        }, 100);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,myGyroscope,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        cordY = event.values[1];
        cordZ = event.values[2];

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public Player getPlayer() {
        return player;
    }
}