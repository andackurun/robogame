package com.robotica.robogame;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
public class MainActivity extends Activity {

	private PowerManager.WakeLock wl;
	private int slider=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        wl.acquire();
		
		RelativeLayout backgraund = (RelativeLayout) findViewById(R.id.layoutMain);
		backgraund.setBackgroundResource(R.drawable.redcar);
		
		final ImageView game = (ImageView) findViewById(R.id.newgame);
		game.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaPlayer newgamemp = MediaPlayer.create(getApplicationContext(), R.raw.click);
				newgamemp.start();
				startActivity(new Intent(MainActivity.this, GameOptionsActivity.class));
				
			}
		});
		
		final ImageView highscore = (ImageView) findViewById(R.id.highscore);
		highscore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MediaPlayer scoremp = MediaPlayer.create(getApplicationContext(), R.raw.click);
				scoremp.start();
				startActivity(new Intent(MainActivity.this, HighScoresActivity.class));
			}
		});
		
		final ImageView options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaPlayer optionmp = MediaPlayer.create(getApplicationContext(), R.raw.click);
				optionmp.start();
				startActivity(new Intent(MainActivity.this,Options.class));
			}
		});
		final ImageView exit = (ImageView) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaPlayer exitmp = MediaPlayer.create(getApplicationContext(), R.raw.click);
				exitmp.start();
				wl.release();
				finish();
				System.exit(0);
			}
		});
		highscore.setVisibility(4);
		options.setVisibility(4);
		exit.setVisibility(4);
		
		
		
		
		ImageView slideleft = (ImageView) findViewById(R.id.slideleft);
		slideleft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaPlayer leftmp = MediaPlayer.create(getBaseContext(), R.raw.button);
				leftmp.start();
				//buttonsound.stop();
				if(slider>=0 && slider<4){
					slider--;
					if(slider == -1)
						slider=3;
				}
				if(slider==0){
					game.setVisibility(0);
					highscore.setVisibility(4);
					options.setVisibility(4);
					exit.setVisibility(4);	
				}
				else if(slider==1){
					game.setVisibility(4);
					highscore.setVisibility(0);
					options.setVisibility(4);
					exit.setVisibility(4);	
				}
				else if(slider==2){
					game.setVisibility(4);
					highscore.setVisibility(4);
					options.setVisibility(0);
					exit.setVisibility(4);	
				}
				else if(slider==3){
					game.setVisibility(4);
					highscore.setVisibility(4);
					options.setVisibility(4);
					exit.setVisibility(0);	
				}
			}
		});
		
		ImageView slideright = (ImageView) findViewById(R.id.slideright);
		slideright.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaPlayer rightmp = MediaPlayer.create(getBaseContext(), R.raw.button);
				rightmp.start();
				//buttonsound.start();
				//buttonsound.stop();
				if(slider>=0 && slider<4){
					slider++;
					slider%=4;
				}
				if(slider==0){
					game.setVisibility(0);
					highscore.setVisibility(4);
					options.setVisibility(4);
					exit.setVisibility(4);	
				}
				else if(slider==1){
					game.setVisibility(4);
					highscore.setVisibility(0);
					options.setVisibility(4);
					exit.setVisibility(4);	
				}
				else if(slider==2){
					game.setVisibility(4);
					highscore.setVisibility(4);
					options.setVisibility(0);
					exit.setVisibility(4);	
				}
				else if(slider==3){
					game.setVisibility(4);
					highscore.setVisibility(4);
					options.setVisibility(4);
					exit.setVisibility(0);	
				}
				
			}
		});
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
