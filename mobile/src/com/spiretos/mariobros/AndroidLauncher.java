package com.spiretos.mariobros;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.spiretos.mariobros.Screens.PlayScreen;
import com.spiretos.wearemote.communication.Communicator;

public class AndroidLauncher extends AndroidApplication {

	//public static final String GAME_MARIO = "game_space";
    Communicator mCommunicator;
    MarioBros mMarioBros;
    PlayScreen mPlayScreen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mCommunicator = new Communicator(this);
        mCommunicator.setCommunicationListener(new Communicator.CommunicationListener()
        {
            @Override
            public void onConnected()
            {
                OnConnectedWithWear();
            }
        });
        mCommunicator.connect();

        mMarioBros = new MarioBros();

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(mMarioBros, config);
        mPlayScreen = mMarioBros.getPlayscreen();

	}
    protected void OnConnectedWithWear()
    {
        mCommunicator.sendMessage(Communicator.MESSAGE_START_ACTIVITY);
        Log.i("START HERE", "IT IS WORKING");
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String type=intent.getStringExtra("type");
            float yValue = intent.getFloatExtra("value", 0);
            //float xValue = intent.getFloatExtra("value", 0);

            Log.w("m-", "*** GOT '" + type + "'=" + yValue);

            //onYchanged(yValue);
            OnReceivedRemoteValue(type, yValue);
            //OnReceivedRemoteValue(type, xValue);
        }
    };

    protected void OnReceivedRemoteValue(String type, float value) {
        Log.i("DEBUG", "Playscreen is " + mPlayScreen);
            if (mPlayScreen == null) {
                mPlayScreen = mMarioBros.getPlayscreen();
                return;
            }
            mPlayScreen.setMarioSpeedY(value);


        mPlayScreen.marioJump(false);

    }


    @Override
    protected void onResume()
    {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("sensor_data"));
    }

    @Override
    protected void onPause()
    {
        if (mMessageReceiver != null)
        {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        }

        super.onPause();
    }

}

