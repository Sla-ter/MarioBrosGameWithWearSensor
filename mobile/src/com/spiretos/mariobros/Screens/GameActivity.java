package com.spiretos.mariobros.Screens;

import android.content.Intent;
import android.os.Bundle;


import com.spiretos.mariobros.AndroidLauncher;
import com.spiretos.wearemote.receiver.ReceiverActivity;

public class GameActivity extends ReceiverActivity {

    public static final String GAME_SPACE = "game_space";
    PlayScreen mGameEngine;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent gameIntent=new Intent(GameActivity.this, AndroidLauncher.class);
        //gameIntent.putExtra("game", AndroidLauncher.GAME_MARIO);
        //startActivity(gameIntent);
        setUpGame();
    }

    private void setUpGame() {
        //String game = getIntent().getStringExtra("game");
        //if (game.equals(GAME_SPACE)) {
        mGameEngine = new PlayScreen();
        //}
        Intent gameIntent=new Intent(GameActivity.this, AndroidLauncher.class);
        gameIntent.putExtra("game", AndroidLauncher.GAME_MARIO);
        startActivity(gameIntent);
    }


    @Override
    protected void OnReceivedRemoteValue(String type, float value) {
        if (mGameEngine != null)
        {
            mGameEngine.setMarioSpeedY(value);
            mGameEngine.setMarioSpeedX(value);
        }

    }


    /*@Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //if(Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen((MarioBros) game));
            //dispose();
        //}

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    } */


    /*@Override
    protected void onResume()
    {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Intent gameIntent=new Intent(GameActivity.this, MainActivity.class);
        startActivity(gameIntent);

    }

    @Override
    protected void onPause()
    {
        super.onPause();


    }*/
}
