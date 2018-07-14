package com.spiretos.mariobros;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spiretos.mariobros.Screens.GameActivity;

//import GameActivity;

public class MainActivity extends AndroidLauncher {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button marioRemoteButton= (Button) findViewById(R.id.main_marioremote_button);
		marioRemoteButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent gameIntent=new Intent(MainActivity.this, AndroidLauncher.class);
				//gameIntent.putExtra("game", GameActivity.GAME_SPACE);
				startActivity(gameIntent);
			}
		});
	}
}