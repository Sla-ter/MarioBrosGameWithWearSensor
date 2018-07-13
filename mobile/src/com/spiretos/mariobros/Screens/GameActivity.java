package com.spiretos.mariobros.Screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;


import com.badlogic.gdx.Game;
import com.spiretos.mariobros.AndroidLauncher;
import com.spiretos.mariobros.MarioBros;


public class GameActivity extends Activity {

    public static final String GAME_SPACE = "game_space";
    PlayScreen mGameEngine;

    private Game mGame = new MarioBros();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpGame();
    }

    private void setUpGame() {
       // Intent gameIntent=new Intent(GameActivity.this, AndroidLauncher.class);
        //gameIntent.putExtra("game", AndroidLauncher.GAME_MARIO);
       // startActivity(gameIntent);

        //String game = getIntent().getStringExtra("game");
        //if (game.equals(GAME_SPACE)) {
            Intent gameIntent=new Intent(GameActivity.this, AndroidLauncher.class);
            //gameIntent.putExtra("game", AndroidLauncher.GAME_MARIO);
            startActivity(gameIntent);
        //}
    }

//
//    @Override
//    protected void OnReceivedRemoteValue(String type, float value) {
//       if (mGameEngine != null)
//        {
//
//            mGameEngine.setMarioSpeedY(value);
//            //mGameEngine.setMarioSpeedX(value);
//        }
//
//    }

}
