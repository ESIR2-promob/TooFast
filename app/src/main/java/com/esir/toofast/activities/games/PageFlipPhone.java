package com.esir.toofast.activities.games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esir.toofast.R;
import com.esir.toofast.controllers.Player;
import com.esir.toofast.utils.PointCalculator;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PageFlipPhone extends AppCompatActivity   {


    private SensorManager sensorManagerAccel;
    private static final String TAG_Accel = "Accelerometer";
    Sensor accelerometer;
    Button pret;
    Date currentTime ;
    SensorEventListener mAccelerometerSensorListener;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_flip_phone);

        player = (Player)getIntent().getSerializableExtra("Joueur");

        Log.d(TAG_Accel, "onCreate: Initializing Sensor Services");
        sensorManagerAccel = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManagerAccel.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Log.d(TAG_Accel,"onCreate: Registered accelerometer listener");
        pret = findViewById(R.id.button_pret_flip_phone);

        pret.setOnClickListener(v -> {
            pret.setVisibility(View.INVISIBLE);
            currentTime = Calendar.getInstance().getTime();
             mAccelerometerSensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {

                    float Z_Accel = round(event.values[2],1);
                    Log.e(TAG_Accel, "onSensorChanged: X :"  + " | Y :" + " | Z :" + Z_Accel);
                    if(Float.parseFloat("-10")>=Z_Accel && Z_Accel <=Float.parseFloat("-9.7")){

                        float actualTime = Calendar.getInstance().getTime().getTime() - currentTime.getTime();
                        player.addScore(PointCalculator.calculator(150.0f, 700.0f, 1000, actualTime, getApplicationContext()));

                        Toast.makeText(getApplicationContext(), String.format(Locale.FRANCE,"Jeu terminé (%.2f s)", ((float)(Calendar.getInstance().getTime().getTime() - currentTime.getTime())/1000)), Toast.LENGTH_LONG).show();
                        sensorManagerAccel.unregisterListener(mAccelerometerSensorListener);

                        startActivity(new Intent(getBaseContext(), player.nextChallenge()).putExtra("Joueur",player));
                    }

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    Log.d("MY_APP", sensor.toString() + " - " + accuracy);
                }
            };
            sensorManagerAccel.registerListener(mAccelerometerSensorListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        });

    }


    public static float round(float d, int decimalPlace)
    {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManagerAccel.registerListener(mAccelerometerSensorListener, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (accelerometer != null) {
            sensorManagerAccel.unregisterListener(mAccelerometerSensorListener);
        }
    }



}