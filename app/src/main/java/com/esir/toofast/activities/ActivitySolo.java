package com.esir.toofast.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.esir.toofast.R;
import com.esir.toofast.controllers.Player;

public class ActivitySolo extends AppCompatActivity {

    Player player;
    EditText inputPseudo;
    Button btnStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_solo);

        inputPseudo = findViewById(R.id.pseudoInput);
        btnStart = findViewById(R.id.button_startDefis);

        btnStart.setOnClickListener(v -> init());
    }

    private void init(){
        if (!inputPseudo.getText().toString().isEmpty()) {
            player = new Player(inputPseudo.getText().toString(), Player.SOLO);
            startActivity(new Intent(this,player.nextChallenge()).putExtra("Joueur",player));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.textAlertDialogPseudo)
                    .setPositiveButton(R.string.AccepterAlertDialogPseudo, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(R.string.AccueilAlertDialogPseudo, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(ActivitySolo.this, ActivityAccueil.class));
                        }
                    });
            // Create the AlertDialog object and show it
            builder.create().show();
        }
    }



}