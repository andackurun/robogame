package com.robotica.robogame;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import java.lang.String;

public class Connection extends Activity implements OnClickListener {
	WifiManager wifi;
	BroadcastReceiver receiver;
	
	TextView wifiText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		

		wifiText = (TextView) findViewById(R.id.xx);
		//wifiList = (ListView) findViewById(R.id.listView1);
		
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		if(receiver == null) 
			receiver = new WifiList(this);
		registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		
			wifi.startScan();
			
		 
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
			registerReceiver(receiver, intentFilter);
		
		
//		ArrayAdapter<ScanResult> adapter = new ArrayAdapter<ScanResult>(this, R.id.listView1, wifiler);
//		wifiList.setAdapter(adapter);
		
	}
	
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		// TODO Auto-generated method stub
//		List<ScanResult> allWifi = game.wifi.getScanResults();
//		
//		activity.setContentView(R.layout.activity_connection);
//		
//		textStatus = (TextView) activity.findViewById(R.id.textView1);
//		for(ScanResult i : allWifi) {
//			if(i == null)
//				break;	
//			textStatus.append("\nWifi Name:" + i.SSID + "\n");
//		}
//		
//	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String networkSSID = "ng2k";
		String networkPass = "andac4510";
		
		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\"" + networkSSID + "\""; 
		
		//conf.wepKeys[0] = "\"" + networkPass + "\""; 
		//conf.wepTxKeyIndex = 0;
		conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		//conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		
		//conf.preSharedKey = "\""+ networkPass +"\"";
		
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
		wifiManager.addNetwork(conf);
		
		List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
		for( WifiConfiguration i : list ) {
		    if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
		         wifiManager.disconnect();
		         wifiManager.enableNetwork(i.networkId, true);
		         wifiManager.reconnect();
		         wifiText.append("\n" + networkPass + "\n");

		         break;
		    }           
		 }
		
		
	}

	
	

}
