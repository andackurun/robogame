package com.robotica.robogame;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.view.ViewGroup.LayoutParams;
import java.net.ServerSocket;


public class ConnectorServerActivity extends Activity {
    private Handler handler = new Handler();
    public static String SERVERIP = "192.168.43.1";
    private TextView otherPlayer;
    private TextView gameArea;
    private TextView carOne;
    private TextView carTwo;
    private TextView serverStatus;
    private TextView hostText;
    private String line;
    private StringBuffer myLine;
    private ServerSocket serverSocket;
    private int SERVERPORT = 8080;
    private Thread fsm;
    private int clientNo = 0;
    private boolean startGame = true;




	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lasertagconnector);
		
        otherPlayer = (TextView) findViewById(R.id.otherPlayerText);
        gameArea = (TextView) findViewById(R.id.gameAreaText);
        carOne = (TextView) findViewById(R.id.carOneText);
        carTwo = (TextView) findViewById(R.id.carTwoText);
        serverStatus = (TextView) findViewById(R.id.server_status);
        hostText = (TextView) findViewById(R.id.hostText);
        
        hostText.setText(getLocalIpAddress());


		Thread fst = new Thread(new firstThread());
		fst.start();
		
		

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public class firstThread implements Runnable {
    	int counter=0;
    	public void run() {
            try
    		{
                serverSocket = new ServerSocket(SERVERPORT);

            	while(true)
            	{
            		if(GameOptionsActivity.game == 1) {
            			if(clientNo == 1)
            				break;
            		}
            		else {
            			if(clientNo == 3)
                			break;
            		}
                    Socket client = serverSocket.accept();
                    ServerThread st = new ServerThread(client, clientNo);
                    //MAC KISMI                    
                    fsm = new Thread(st);
                    st.setThread(fsm);
                    fsm.start();
                    clientNo++;
            		//System.out.println("sayii = " + Integer.toString(i));

            	}
            	if(GameOptionsActivity.game == 1) {
    				startActivity(new Intent(ConnectorServerActivity.this, SoloRaceActivity.class));
            	}
            	else if(GameOptionsActivity.game == 2) {
	    				startActivity(new Intent(ConnectorServerActivity.this, MultiRaceServer.class));
	            	}
	            else if(GameOptionsActivity.game == 3) {
	            		//System.out.println("666666");
	    				startActivity(new Intent(ConnectorServerActivity.this, LaserTagServer.class));
	            }
            	
    		} 
            catch (Exception e)
    		{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
//		private Boolean checkWifly(byte[] mac)
//		{
//			if(mac[0] == (byte)0x00 && mac[1] == (byte)0x06 && mac[2] == (byte)0x66)
//				return true;
//			else
//				return false;
//		}
    	
    }
	
    public class ServerThread implements Runnable {
    	Socket socket = null;
    	Thread st = null;
    	int counter;
    	public void setThread(Thread fsm)
		{
    		st = fsm;
		}
    	
    	public ServerThread(Socket socket, int counter)
		{
    		this.socket = socket;
    		this.counter = counter;
		}
    	
        public void run() {
            try {
                if (SERVERIP != null) {
                    
                    while (startGame) {
                    	Thread.sleep(50);
                        try {
//                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                            while(true)
//                            {
//                            	myLine = new StringBuffer(in.readLine());
//                            	if(myLine != null)
//                            		break;
//                            }
                                //Log.d("ServerActivity", line.toString());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                    	
	                                    	if(socket.getInetAddress().getHostAddress().equals("192.168.43.153")) {
	                                    		
	                                			carOne.setText(socket.getInetAddress().getHostAddress());
	                                			ConnectionInfos.firstCarIP = socket.getInetAddress().getHostAddress();
	                                			ConnectionInfos.firstCarSocket = socket;
	                                			if(GameOptionsActivity.game == 1) {
	                                				ConnectionInfos.secondCarSocket = socket;
	                                				ConnectionInfos.secondCarIP = socket.getInetAddress().getHostAddress();
	                                				startGame = false;
	                                			}
	                                		}
	                                    	else if(socket.getInetAddress().getHostAddress().equals("192.168.43.200")) {
	                                    		if(GameOptionsActivity.game == 1) {
	                                    			startGame = false;
	                                    		}
                                    			carTwo.setText(socket.getInetAddress().getHostAddress());
                                    			ConnectionInfos.secondCarIP = socket.getInetAddress().getHostAddress();
                                    			ConnectionInfos.secondCarSocket = socket;
                                    		}
	                                    	else {
                                    			otherPlayer.setText(socket.getInetAddress().getHostAddress());
                                    			ConnectionInfos.otherPhoneIP = socket.getInetAddress().getHostAddress();
                                    			ConnectionInfos.otherPhoneSocket = socket;

                                    			if(clientNo == 3) {
        	                                    	try {
        	                                    		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
        		                                    	out.println("basla");
        		                                    	out.flush();
        		                                    //	out.close();
        		                                    	startGame = false;
        	                                    	} catch (IOException e1) {
        												// TODO Auto-generated catch block
        												e1.printStackTrace();
        											}
                                            	}
                                    		}
                                    		
                                    		
//                                    		if(counter == 3)
//                                    			gameArea.setText(socket.getInetAddress().getHostAddress());
                                    	
	                                    	
                                    }
                                });
                                
                        } catch (final Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                	e.printStackTrace();
                                    serverStatus.setText("Oops. Connection interrupted. Please reconnect your phones.");
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            serverStatus.setText("Couldn't detect internet connection.");
                        }
                    });
                }
            } catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        serverStatus.setText("Error");
                    }
                });
                e.printStackTrace();
            }
        }
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
