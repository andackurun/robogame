package com.robotica.robogame;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.robotica.robogame.R;


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



@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MultiRaceClient extends Activity implements SensorEventListener {

    private EditText serverIp;
    
    private SensorManager sm =null;
	private Sensor acc;

	public static String ip = null; 
    private String serverIpAddress = "192.168.43.1";

    private boolean connected = false;
    private boolean upKey = false;
    private boolean downKey = false;
    private boolean leftKey = false;
    private boolean rightKey = false;
    private boolean leftupKey = false;
    private boolean rightupKey = false;
    private boolean leftdownKey = false;
    private boolean rightdownKey = false;
    private boolean spinkey = false;
    private boolean laserkey = false;
    private boolean leftlaser = false;
    private boolean rightlaser = false;
    private boolean breakKey = false;
    private Handler handler = new Handler();
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Socket socket;
    private TextView redLed;
    private TextView yellowLed;
    private TextView greenLed;
    private TextView blueLed;
    private TextView error;
    private float mLastTouchX;
    private float mLastTouchY;
    private long startTime;
    private long endTime;
    private long raceTime;
    private boolean redCheck = false;
    private boolean yellowCheck = false;
    private boolean greenCheck = false;
    private boolean blueCheck = false;
    private long speedIncreaseTime=0;
    private long speedDecreaseTime=0;
    private long currentTime;

    //public HighScoresActivity score;
    private int remaningAmmo = 100;
    private int remainingHealth = 100;
    private int remainingEnergy = 100;
    private int currentSpeed = 50;
    
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    float axisX;
    float axisY;
    float axisZ;
    
    private String str = "";
	private String highScores = "";
	private int[] topFive = new int[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multirace);
        
        axisX = 0;
        axisY = 0;
        axisZ = 0;
        sm=(SensorManager) getSystemService(SENSOR_SERVICE);
        acc=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_GAME);
		startTime = System.currentTimeMillis();
        
//		ImageButton left = (ImageButton) findViewById(R.id.left);
//        left.setOnTouchListener(leftListener);
//        ImageButton right = (ImageButton) findViewById(R.id.right);
//        right.setOnTouchListener(rightListener);
//        Button spin = (Button) findViewById(R.id.spin);
//        spin.setOnTouchListener(spinListener);
//        Button laser =(Button) findViewById(R.id.laser);
//        laser.setOnTouchListener(laserListener);
//        Button breaking = (Button) findViewById(R.id.breaking);
//        breaking.setOnTouchListener(breakingListener);
        redLed = (TextView) findViewById(R.id.RedCheck);
        yellowLed= (TextView) findViewById(R.id.YellowCheck);
        blueLed= (TextView) findViewById(R.id.BlueCheck);
        greenLed= (TextView) findViewById(R.id.GreenCheck);
//        error = (TextView) findViewById(R.id.energyText);
        
        if (!connected) {
          if (!serverIpAddress.equals("")) {
              Thread cThread = new Thread(new ClientThread());
              cThread.start();
          }
        }
        
        

        
    }
    
private OnTouchListener breakingListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if(event.getAction() == MotionEvent.ACTION_DOWN){
				breakKey = true;
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				breakKey = false;
			}
			
			return false;	
		}
			
	};

   	
	
	private OnTouchListener leftListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				leftlaser =true;
				
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				leftlaser = false;
				
			}
			
			return false;	
		}
	};
	
	private OnTouchListener rightListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				rightlaser = true;
				
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				rightlaser = false;
				
			}
			
			return false;	
		}
	};
	
	
private OnTouchListener spinListener = new OnTouchListener() {
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			spinkey = true;
			
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			spinkey = false;
			
		}
		
		return false;	
	}
};
private OnTouchListener laserListener = new OnTouchListener() {
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			laserkey = true;
			
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			laserkey = false;
			
		}
		
		return false;	
	}
};
		
	
public class ClientThread implements Runnable {
	
	String msg = "";
	int counter = 0;
    public void run() {
        try {
            InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
            Log.d("ClientActivity", "C: Connecting...");
//            socket = new Socket(serverAddr, 8080);
            socket = ConnectionInfos.serverSocket;
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            connected = true;
            while (connected) {
            	if(blueCheck && redCheck && yellowCheck && greenCheck) {
    				startActivity(new Intent(MultiRaceClient.this, GameOverActivity.class));
    				connected = false;
            	}
                try {
                    Log.d("ServerActivity", "C: Sending command.");
                    msg = "";
                    if(spinkey){
                    	msg += "ss";
                    }
                    else{                        	
                    	if(upKey) 
                        	msg += "u";
                        else if(downKey)
                        	msg += "d";
                        else 
                        	msg += "n";
                        
                        if(leftKey)
                        	msg += "l";
                        else if(rightKey)
                        	msg += "r";
                        else 
                        	msg += "n";
                        	
                    }
                    
//                    if(leftlaser)
//                    	msg += "l";
//                    else if(rightlaser)
//                    	msg += "r";
//                    else
                    	msg += "n";
                    		
               /*     if(leftdownKey)
                    	msg = "dl";
                    else if(leftupKey)
                    	msg = "ul";
                    else if(rightdownKey)
                    	msg = "dr";
                    else if(rightupKey)
                    	msg = "ur";
               */ 
                /*    if(remaningAmmo == 0) {
                   	 endTime = System.currentTimeMillis();
                   	 raceTime = endTime - startTime;
                   	 AssetManager ass = ConnectServer.this.getAssets();
                   	
                   	 Writer output;
                   	 output = new BufferedWriter(new FileWriter("/assets/solo.txt"));
                   	 output.append("New Line!");
                    output.close();
                   	 score.setNewHighScore(raceTime);
				
                    } */
//                    if(laserkey && (remaningAmmo > 0)){
//                    	msg +="o";
//                    	handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                            	remaningAmmo--;
//                            	ammo.setText(Integer.toString(remaningAmmo));
//                            }
//                        });
//                    	
//                    }
//                    else 
//                    	msg +="c";
                    msg += "c";
                    
                    if(breakKey)
                    	msg += "b";
                    else
                    	msg += "n";
                    
//                    if(speedIncrease)
//                    	msg += "i";
//                    else if(speedDecrease)
//                    	msg += "d";
//                    else
                    	msg += 'n';
                   
                  //  out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                  //              .getOutputStream())), true);
                        // where you issue the commands
                  //   out.println(msg);
                    	out.println(msg);
                    	out.flush();
                        System.out.println(msg);
                        Thread.sleep(30);                    
                   // System.out.println(secondCarMsg);
//                     if(secondCarMsg.charAt(0) == 's') {
//                    	 handler.post(new Runnable() {
//                             @Override
//                             public void run() {
//                                 remainingHealth -= 10;
//                                 if(remainingHealth == 0) {
//                                	 endTime = System.currentTimeMillis();
//                                	 raceTime = endTime - startTime;
//                                	 //setNewHighScore(raceTime);
//                     				 startActivity(new Intent(MultiRaceServer.this, GameOverActivity.class));
//
//                                 }
//                                 healthing.setText(Integer.toString(remainingHealth));
//                             }
//                         });
//                     }
                     
                        try {
 //                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        	 String line = in.readLine();
                        	 if(line != null)
                        		 System.out.println("line: " + line);
//                        	 if(line.charAt(0) == 's') {
//                            	 handler.post(new Runnable() {
//                                     @Override
//                                     public void run() {
//                                         remainingHealth -= 10;
//                                         if(remainingHealth == 0) {
//                                        	 endTime = System.currentTimeMillis();
//                                        	 raceTime = endTime - startTime;
//                                        	 //setNewHighScore(raceTime);
//                             				 //startActivity(new Intent(MultiRaceClient.this, GameOverActivity.class));
//
//                                         }
//                                         healthing.setText(Integer.toString(remainingHealth));
//                                     }
//                                 });
//                             }
                             
                             if(line.charAt(1) == 'h') {
                            	 handler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                        redLed.setText("+");
                                        redCheck = true;
                                     }
                                 });
                             }
                             else if(line.charAt(1) == 'a') {
                            	 handler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         blueLed.setText("+");
                                         blueCheck = true;
                                         
                                     }
                                 });
                             }
//                        	                             else if(secondCarMsg.equals("g")) {
//                        	                            	 handler.post(new Runnable() {
//                        	                                     @Override
//                        	                                     public void run() {
//                        	                                    	 remainingEnergy += 10;
//                        	                                         
//                        	                                         energy.setText(Integer.toString(remainingEnergy));
//                        	                                     }
//                        	                                 });
//                        	                             }
                             else if(line.charAt(1) == 'y') {
                            	 handler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                    	yellowLed.setText("+");
                                    	yellowCheck = true;
                                     }
                                 });
                             }
                             else if(line.charAt(1) == 'd') {
                            	 handler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                    	greenLed.setText("+");
                                    	greenCheck = true;
                                     }
                                 });
                             }
                        }
                        catch (Exception e) {
                       	 Log.e("line okumadý", "C:error", e);
                        }
                     
                 
//                     currentTime = System.currentTimeMillis();
//                     if(speedIncrease) {
//                    	 if((currentTime-speedIncreaseTime) > 10000) {
//                    		 handler.post(new Runnable() {
//                    			 @Override
//                    			public void run() {
//                    				speedIncrease = false;
//                    				currentSpeed = 50;
//                           		 speed.setText(Integer.toString(currentSpeed));
//                    			}
//                    		 });
//                    	 }
//                     }
//                     else if(speedDecrease) {
//                    	 if((currentTime-speedIncreaseTime) > 10000) {
//                    		 handler.post(new Runnable() {
//                    			 @Override
//                            	 public void run() {
//                            		 speedDecrease = false;
//                            		 currentSpeed = 50;
//                            		 speed.setText(Integer.toString(currentSpeed));
//                            	 }
//                            	 
//                             });
//                    		 
//                    	 }
//                     }
                     	 
                    
                     try {
                    	    Thread.sleep(50);
                    	} catch(InterruptedException ex) {
                    	    Thread.currentThread().interrupt();
                    	}
                        Log.d("ServerActivity", "C: Sent.");
                        
                } catch (Exception e) {
                    Log.e("ServerActivity", "S: Error", e);
                }
            }
        //    socket.close();
            Log.d("ServerActivity", "C: Closed.");
        } catch (Exception e) {
            Log.e("ServerActivity", "C: Error", e);
            connected = false;
        }

    }
    
}

    public void onSensorChanged(SensorEvent event) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
       
            
            // Axis of the rotation sample, not normalized yet.
            float alpha = (float) 0.8;
            axisX = alpha*axisX + (1-alpha)*event.values[0];
            axisY = alpha*axisY + (1-alpha)*event.values[1];
            axisZ = alpha*axisZ + (1-alpha)*event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = (float) Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

            byte x = (byte) (128*axisX/omegaMagnitude);
    	    byte y = (byte) (128*axisY/omegaMagnitude);
    	    byte z = (byte) (128*axisZ/omegaMagnitude);



    	    if(z > 90){
    	    	upKey = true;
    	    	downKey = false;
    	    }
    	    else if(z < -10) {
    	    	upKey = false;
    	    	downKey = true;
    	    }
    	    else {
    	    	upKey = false;
    	    	downKey = false;
    	    }
    	    
    	    if (y<-45){
    	    	leftKey=true;
    	    	rightKey=false;
    	    }
    	    else if (y>45){
    	    	rightKey=true;
    	    	leftKey=false;
    	    }
    	    else{
    	    	leftKey=false;
    	    	rightKey=false;
    	    }
//    	    yView.setText(Integer.toString(y));
//    	    zView.setText(Integer.toString(z));

    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	public void setNewHighScore(long ms) {
		SharedPreferences prefs = this.getSharedPreferences("RoboPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		String temp;
		int i;
		for(i=0; i<5; i++) {
			temp = "scores" + Integer.toString(i);
			str = prefs.getString(temp, "0");
			Integer tempScore = Integer.parseInt(str);
			if( tempScore != 0)
				topFive[i] = tempScore;
			else
				topFive[i] = 0;
		}
		
		topFive[i] = (int) ms;
		
		Arrays.sort(topFive);
		for(i=0; i<5; i++) {
			temp = "scores" + Integer.toString(i);
			editor.putString(temp, Integer.toString(topFive[i]));
			editor.commit();
		}		
	
	}

}

