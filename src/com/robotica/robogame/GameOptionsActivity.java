package com.robotica.robogame;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;

public class GameOptionsActivity extends Activity {
    // default ip
    public static String SERVERIP = "10.0.2.15";
    public static int game = 0;


    // designate a port
    public static final int SERVERPORT = 8080;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_options);
		
		ImageView solorace =(ImageView) findViewById(R.id.solorace);
		solorace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaPlayer solomp = MediaPlayer.create(getApplicationContext(), R.raw.click);
				solomp.start();
				game=1;
				String str = getLocalIpAddress();
				System.out.println(str);
				//if(str.equals("192.168.43.1")){
				//	startActivity(new Intent(GameOptionsActivity.this, ConnectorServerActivity.class));
				//}
				//else startActivity(new Intent(GameOptionsActivity.this, ConnectorClientActivity.class));
				startActivity(new Intent(GameOptionsActivity.this, ConnectorServerActivity.class));
			}
		});
		ImageView multirace = (ImageView) findViewById(R.id.multirace);
		multirace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaPlayer multimp = MediaPlayer.create(getApplicationContext(), R.raw.click);
				multimp.start();
				game = 2;
				String str = getLocalIpAddress();
				System.out.println(str);
				if(str.equals("192.168.43.1")){
					startActivity(new Intent(GameOptionsActivity.this, ConnectorServerActivity.class));
				}
				else startActivity(new Intent(GameOptionsActivity.this, ConnectorClientActivity.class));
				//startActivity(new Intent(GameOptionsActivity.this, MultiplayerRaceActivity.class));
			}
		});
		ImageView lasertag = (ImageView) findViewById(R.id.lasertag);
		lasertag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaPlayer lasermp = MediaPlayer.create(getApplicationContext(), R.raw.click);
				lasermp.start();
				game = 3;
				String str = getLocalIpAddress();
				System.out.println(str);
				if(str.equals("192.168.43.1")){
					startActivity(new Intent(GameOptionsActivity.this, ConnectorServerActivity.class));
				}
				else startActivity(new Intent(GameOptionsActivity.this, ConnectorClientActivity.class));
				//startActivity(new Intent(GameOptionsActivity.this, LaserTagActivity.class));
			}
		});
		

//		if(receiver==null){
//			receiver = new Connection(this);
//		}
//		registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//		wifi.startScan();
		
//		Spinner scores = (Spinner) findViewById(R.id.scores);
//		scores.setOnItemSelectedListener((OnItemSelectedListener) this);
//		
//		ArrayAdapter<String> game=new ArrayAdapter<String>(this, 
//				android.R.layout.simple_spinner_item, games);
//		game.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		scores.setAdapter(game);
	}

	
	 private String getLocalIpAddress() {
	        try {
	            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	                NetworkInterface intf = en.nextElement();
	                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                    InetAddress inetAddress = enumIpAddr.nextElement();
	                    if(!inetAddress.isLoopbackAddress()) {
	                    	if(inetAddress instanceof Inet4Address)
		                    	return inetAddress.getHostAddress().toString();	
	                    }
	                    
	                }
	            }
	        } catch (SocketException ex) {
	            Log.e("ServerActivity", ex.toString());
	        }
	        return null;
	    }
}
