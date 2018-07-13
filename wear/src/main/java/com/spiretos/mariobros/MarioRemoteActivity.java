package com.spiretos.mariobros;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


import com.spiretos.wearemote.communication.Communicator;
import com.spiretos.wearemote.sensor.SensorActivity;
import com.spiretos.wearemote.sensor.WearSensor;

public class MarioRemoteActivity extends SensorActivity
{

    //private ImageView marioStanding;
    private ImageView marioImage;

    float tempY, tempValueY, lastSendValueY;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mario_remote);
        setAmbientEnabled();

        // mShipImage = (ImageView) findViewById(R.id.ship_image);
        marioImage = (ImageView) findViewById(R.id.little_mario);

        addSensor("accelerometer", Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);

        setCommunicationType(Communicator.TYPE_MESSAGE_API);
        //setCommunicationType(Communicator.TYPE_CHANNEL_API);

        addSensor("my_accelerometer", Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);

    }


    @Override
    protected void onGotSensorData(WearSensor sensor, SensorEvent event)
    {
        if (sensor.getName().equals("accelerometer"))
        {
            tempY = event.values[1];
            //tempX = event.values[1];
            tempValueY = 0;
            //tempValueX = 0;

            if (tempY < -6) {
                tempValueY = -3f;
            }
            else if (tempY < -4){
                tempValueY = -2f;
            }
            else if (tempY < -2) {
                tempValueY = -1f;
            }
            else if (tempY > 6) {
                tempValueY = 3f;
            }
            else if (tempY > 4){
                tempValueY = 2f;
            }
            else if (tempY > 2) {
                tempValueY = 1f;
            }
            else {
                tempValueY = 0;
            }

            if (tempValueY != lastSendValueY) {
                lastSendValueY = tempValueY;
                sendSensorData("accelerometer_Y", lastSendValueY);
                updateImage((int) lastSendValueY);
            }
        }
    }



    private void updateImage(int value) {
        if (value == 0) {
            marioImage.setImageResource(R.drawable.little_mario_standing);
        }
        else if(value >= 1) {
            marioImage.setImageResource(R.drawable.little_mario_running);
        }
        else if(value <= -1) {
            marioImage.setImageResource(R.drawable.little_mario_running_left);
        }

    }

}
