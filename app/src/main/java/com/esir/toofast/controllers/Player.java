package com.esir.toofast.controllers;


import com.esir.toofast.activities.ActivityAttenteClient;
import com.esir.toofast.activities.ActivityAttenteServeur;
import com.esir.toofast.activities.games.PageShakePhone;
import com.esir.toofast.activities.games.PageDefiQuestion;
import com.esir.toofast.activities.games.PageClickBall;
import com.esir.toofast.activities.games.PageFlipPhone;
import com.esir.toofast.activities.games.PageLabyrinthe;
import com.esir.toofast.activities.ActivityResults;
import com.esir.toofast.activities.games.PageStrength;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.lang.Math;


public class Player implements Serializable {

    public static String SOLO = "SOLO";
    public static String ENTRAINEMENT = "ENTRAINEMENT";
    public static String MULTIJOUEUR = "MULTIJOUEUR";

    private int randomSeed;
    private List<Class> defis = new ArrayList<Class>();
    private final Class pageFinale;

    private final String nom ;
    private final String mode ;
    private int index;
    private int score;
    private int enemyPoint;

    private boolean isMultiplayerInit = false;

    public Player(String nomJoueur, String mode){

        //On ajoute les défis
        defis.add(PageFlipPhone.class);
        defis.add(PageClickBall.class);
        defis.add(PageLabyrinthe.class);
        defis.add(PageDefiQuestion.class);
        defis.add(PageStrength.class);
        defis.add(PageShakePhone.class);

        pageFinale = ActivityResults.class;

        randomSeed = Math.abs(new Random().nextInt());

        this.score = 0;

        this.index = -1;

        this.nom = nomJoueur;
        if(mode.equals(ENTRAINEMENT)){
            this.mode = ENTRAINEMENT;
        }
        else if(mode.equals(SOLO)){
            this.mode = SOLO;
            Collections.shuffle(defis);
        }
        else if(mode.equals(MULTIJOUEUR)){
            this.mode = MULTIJOUEUR;
        }
        else{
            this.mode = "ERREUR";
        }
    }

    public Class nextChallenge(){
        if(this.mode.equals(ENTRAINEMENT)){
            return pageFinale;
        }
        else if(this.mode.equals(SOLO)) {
            this.index = this.index + 1;
            if(this.index == 3){
                return pageFinale;
            }
            else return defis.get(this.index);
        }
        else if(this.mode.equals(MULTIJOUEUR)) {
            this.index = this.index + 1;
            if(this.index == 6){
                return pageFinale;
            }
            else return defis.get(this.index);
        }
        return null;
    }

    public String getNom(){
        return nom;
    }

    public void addScore(int score){
        this.score = this.score + score;
    }

    public int getScore(){ return this.score; }

    public int getRandomSeed(){ return this.randomSeed; }

    public boolean isMultiplayerInit(){ return this.isMultiplayerInit; }

    public String getMode(){ return this.mode; }

    public void setMultiplayerAsInit(){ this.isMultiplayerInit=true; }

    public void setMultiplayerNotInit(){ this.isMultiplayerInit=false; }

    public int getCurrentDefiIndex(){ return (this.index/2 + 1);}

    public boolean isLastPage(){ return this.index==5; }

    public void setEnemyPoint(int point){ this.enemyPoint=point; }

    public int getEnemyPoint(){ return this.enemyPoint; }

    public void shuffleChallenges(int seed, String side){
        Collections.shuffle(defis,new Random(seed));
        Class classToInsert;

        if(side.equals("CLIENT")){
            classToInsert = ActivityAttenteClient.class;
        }
        else{
            classToInsert = ActivityAttenteServeur.class;
        }

        defis.add(1,classToInsert);
        defis.add(3,classToInsert);
        defis.add(5,classToInsert);
        defis.add(7,classToInsert);
    }

}
