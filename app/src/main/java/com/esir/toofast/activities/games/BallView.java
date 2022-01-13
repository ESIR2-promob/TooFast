package com.esir.toofast.activities.games;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.esir.toofast.R;
import com.esir.toofast.controllers.Player;
import com.esir.toofast.utils.PointCalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BallView extends View {


    private final Bitmap local;
    private Bitmap bloc, space_brick;
    public static String AXE_X_GAUCHE = "x_gauche";
    public static String AXE_X_DROITE = "x_droite";
    public static String AXE_Y_BAS = "y_descente";

    int coordBallX = 0;
    int coordBallY = 0;

    Date currentTime ;

    int width;
    int height;

    int tailleColonne;
    int tailleLigne;

    int nbColGrille = 15;

    int nbLigneGrille = 30;

    int doOnlyOne = 0;

    Paint brickPaint = new Paint();
    Paint ballPaint = new Paint();
    Paint blocPaint = new Paint();

    int[] caseOuverte = new int[nbLigneGrille / 2];

    public BallView(Context context) {
        this(context,null,0);
    }

    public BallView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        for (int idCase = 0; idCase < caseOuverte.length; idCase++) {
            caseOuverte[idCase] = (int) (Math.random() * nbColGrille);
        }

        bloc =  BitmapFactory.decodeResource(getResources(), R.drawable.concrete_v3);
        space_brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        local = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        currentTime = Calendar.getInstance().getTime();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        height = getHeight();
        width = getWidth();

        tailleLigne = height / nbLigneGrille;
        tailleColonne = width / nbColGrille;

        Bitmap ballBitmap = Bitmap.createScaledBitmap(local, tailleColonne, tailleLigne, true);
        Bitmap blocBitmap = Bitmap.createScaledBitmap(bloc, tailleColonne, tailleLigne, true);
        Bitmap brickBitmap = Bitmap.createScaledBitmap(space_brick, tailleColonne, tailleLigne, true);


        for (int ligne = 0; ligne < height; ligne += tailleLigne) {
            for (int colonne = 0; colonne < width; colonne += tailleColonne) {
                if ((ligne / tailleLigne) % 2 == 0) {
                    canvas.drawBitmap(brickBitmap, colonne, ligne, brickPaint);
                } else {
                    if (colonne / tailleColonne != caseOuverte[((ligne / tailleLigne) / 2)]) {
                        canvas.drawBitmap(blocBitmap, colonne , ligne ,blocPaint);
                    } else {
                        canvas.drawBitmap(brickBitmap, colonne, ligne, brickPaint);
                    }
                }
            }
        }

        canvas.drawBitmap(ballBitmap, coordBallX, coordBallY, ballPaint);

    }

    public void moveXAndY(int pos, String orientation)  {
        if (orientation.equals(AXE_X_DROITE)) {
            for (int pointeur = 0; pointeur < pos; pointeur++) {
                if (coordBallY % (2 * tailleLigne) == 0) {
                    coordBallX++;
                    if (coordBallX + (tailleColonne) > width) {
                        coordBallX = width - tailleColonne;
                    }
                }
            }
        } else if (orientation.equals(AXE_X_GAUCHE)) {
            for (int pointeur = 0; pointeur < pos; pointeur++) {
                if (coordBallY % (2 * tailleLigne) == 0) {
                    coordBallX--;
                    if (coordBallX < 0) {
                        coordBallX = 0;
                    }
                }
            }
        } else if (orientation.equals(AXE_Y_BAS)) {
            if (coordBallY+tailleLigne == height) {
                if(doOnlyOne == 0){
                    Player player = ((PageLabyrinthe) getContext()).getPlayer();
                    float actualTime = Calendar.getInstance().getTime().getTime() - currentTime.getTime();

                    player.addScore(PointCalculator.calculator(25000.0f, 60000.0f, 1000, actualTime, getContext()));
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Fini !")
                            .setMessage(String.format(Locale.FRANCE,"Jeu réussi ! (%d s)", (Calendar.getInstance().getTime().getTime() - currentTime.getTime())/ 1000 ))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    getContext().startActivity(new Intent(getContext(), player.nextChallenge()).putExtra("Joueur",player));
                                }
                            }).show();
                    doOnlyOne = 1;
                }
            } else {
                for (int pointeur = 0; pointeur < pos; pointeur++) {
                    if ((((coordBallY % (2 * tailleLigne)) >= tailleLigne) && ((coordBallY % (2 * tailleLigne)) < 2 * tailleLigne))
                            || (((coordBallX / tailleColonne) == caseOuverte[(coordBallY / tailleLigne) / 2]) && (coordBallX % tailleColonne == 0))) {
                        coordBallY++;
                        if (coordBallY + tailleLigne > height) {
                            coordBallY = height - tailleLigne;
                        }
                    }
                }
            }
        }
    }
}
