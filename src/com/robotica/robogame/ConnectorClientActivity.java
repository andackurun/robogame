package com.robotica.robogame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ConnectorClientActivity extends Activity {
	
	public static String SERVERIP = "10.0.2.15";
    public static final int SERVERPORT = 8080;
	private boolean connected;
	private String myLine = null;
	private TextView clientIpadd;
    private Handler handler = new Handler();

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_laser_tag_connector_client);
		
		clientIpadd = (TextView) findViewById(R.id.clientAddr);
		clientIpadd.setText(getLocalIpAddress());
		
		Thread fst = new Thread(new ClientThread());
        fst.start();
		
	}
	
	public class ClientThread implements Runnable {
		 
        public void run() {
        	while(true)
        	{
        		try
				{
					Thread.sleep(100);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		try {
	            	
	            	InetAddress serverAddr = InetAddress.getByName("192.168.43.1");
	            	Log.d("ClientActivity", "C: Connecting...");
	            	Socket socket = new Socket(serverAddr, ConnectorClientActivity.SERVERPORT);
	            	ConnectionInfos.serverSocket = socket;
	            	
	            	connected = true;
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	            	while(connected)
	            	{

	            		try {
//	            			Log.d("ClientActivity", "C: Sending command.");
//	            			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
//	                                .getOutputStream())), true);
//	            			// WHERE YOU ISSUE THE COMMANDS
//	            			out.println("geldim");
//	            			out.flush();
		                    System.out.println("naber");
		                    System.out.println("naber2");
		                    myLine = in.readLine();
		                    System.out.println("naber" + myLine);
		                    if(myLine != null) {
		                    	if(myLine.equals("basla")) {
		                    		connected=false;
		                    		//in.close();
		                    		break;
		                    	}
		                    }

                    		
	            		} catch (Exception e) {
	            			Log.e("ClientActivity", "S: Error", e);
	            		}
	            	}
//	                while(true)
//	                {
//	                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//	                	myLine = new StringBuffer(in.readLine());
//	                	if(myLine != null)
//	                		break;
//	                }

//	                socket.close();
	            	Log.d("ClientActivity", "C: Closed.");
	            } catch (Exception e) {
	            	Log.e("ClientActivity", "C: Error", e);
	            	connected = false;
	            }
        		
        		break;
        	}
        	if(GameOptionsActivity.game == 2) {
				startActivity(new Intent(ConnectorClientActivity.this, MultiRaceClient.class));
        	}
        	else if(GameOptionsActivity.game == 3) {
        		startActivity(new Intent(ConnectorClientActivity.this, LaserTagClient.class));	
        	}
//        	else {
//        		System.out.println("44444");
//        		startActivity(new Intent(ConnectorClientActivity.this, LaserTagClient.class));
//        	}
			
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_laser_tag_connector_client,
				menu);
		return true;
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
