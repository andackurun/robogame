package com.robotica.robogame;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WifiList extends BroadcastReceiver {
	Connection conn;

	
	public WifiList (Connection conn){
		super();
		this.conn = conn;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final String action = intent.getAction();
		if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
		
		//conn.wifiText.append("\nkapalý\n");
		List<ScanResult> allWifi = conn.wifi.getScanResults();
		//conn.wifiText.append("\nkapalýymý\n");
//		for(ScanResult i : wifiler) {
//			if(i == null)
//				break;
//			str.add(i.toString());
//		}
//		String[] wifiStr = new String[str.size()] ;
//		str.toArray(wifiStr);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(conn, android.R.layout.simple_list_item_1, android.R.id.text1, wifiStr);
//		conn.wifiList.setAdapter(adapter);
		
		for(ScanResult i : allWifi) {
			if(i == null) 
				break;
			//if(i.SSID.compareTo("ng2k") == 0)
				if(i.SSID.compareTo("ng2k") == 0)
				conn.wifiText.append("\n" + i.SSID);
			}
//		conn.wifiText.append("\n acikmis");
//		for(ScanResult i : allWifi) {
//			
//			conn.wifiText.append("\nWifiName : " + i.toString() + "\n");
//			
//		}
		
		}
		else if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
	        if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
	        	String message = String.format("asdasda");
	        	    Toast.makeText(conn, message, Toast.LENGTH_LONG).show();
	            //do stuff
	        } else {
	        	String message = String.format("lostt");
        	    Toast.makeText(conn, message, Toast.LENGTH_LONG).show();
	            // wifi connection was lost
	        }
	    }
		
		
	}

	
}
