package com.spiretos.mariobros.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spiretos on 28/3/2016.
 */
public class GameDataReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        float yValue= intent.getFloatExtra("data_y", 0);
        float xValue= intent.getFloatExtra("data_x", 0);
        //Log.v("data1", yValue + "");

        sendYchanged(yValue);
        sendXchanged(xValue);
    }



    // EVENTS

    public void setGameDataListener(GameDataListener listener)
    {
        addListener(listener);
    }

    public void removeGameDataListener(GameDataListener listener)
    {
        removeListener(listener);
    }

    private void sendYchanged(float yValue)
    {
        for (GameDataListener l : mListeners)
        {
            l.onYchanged(yValue);
        }
    }

    private void sendXchanged(float xValue)
    {
        for (GameDataListener l : mListeners)
        {
            l.onXchanged(xValue);
        }
    }

    public interface GameDataListener
    {
        public void onYchanged(float yValue);
        public void onXchanged(float xValue);
    }



    List<GameDataListener> mListeners = new ArrayList<GameDataListener>();

    private void addListener(GameDataListener toAdd)
    {
        mListeners.add(toAdd);
    }

    public void removeListener(GameDataListener toRemove)
    {
        mListeners.remove(toRemove);
    }

}
