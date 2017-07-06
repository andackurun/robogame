package com.robotica.robogame;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Options extends Activity implements OnSeekBarChangeListener {
	
	int Volume=0;
	AudioManager am;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		SeekBar volume = (SeekBar) findViewById(R.id.volume);
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
         
        volume.setMax(maxVolume);
        volume.setProgress(curVolume);
         
        //sb.setOnSeekBarChangeListener(this);
        volume.setOnSeekBarChangeListener(this);
		
	}
//	public void onProgressChanged(SeekBar seekb, int progress, boolean arg2) {
//        am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
//        Volume = progress;      
//	}

@Override
public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	// TODO Auto-generated method stub
	am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
    Volume = progress; 
}

@Override
public void onStartTrackingTouch(SeekBar seekBar) {
	// TODO Auto-generated method stub
	
}

@Override
public void onStopTrackingTouch(SeekBar seekBar) {
	// TODO Auto-generated method stub
	Toast.makeText(getApplicationContext(), "Volume: " + Integer.toString(Volume), Toast.LENGTH_SHORT).show();
	
}

}
