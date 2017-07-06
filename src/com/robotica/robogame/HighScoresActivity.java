package com.robotica.robogame;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
//import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;




public class HighScoresActivity extends Activity {
	String[] games={"Solo Race", "Multiplayer Race", "Lazer Tag"};
	private TextView solo_score;
	public List<Long> soloValues = new LinkedList<Long>();
	private String str = "";
	private String highScores = "";
	private int[] topFive = new int[6];
	public static Context ctx;
 	int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ctx = this;
		setContentView(R.layout.activity_high_scores);
		
		Spinner scores = (Spinner) findViewById(R.id.scores);
		//scores.setOnItemSelectedListener((OnItemSelectedListener) this);
		//selected item listener ile ilgili bilgileri cekip alttaki text box a yazdir
		ArrayAdapter<String> game=new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, games);
		game.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scores.setAdapter(game);
		
		solo_score = (TextView)findViewById(R.id.solo_scores);
		SharedPreferences prefs = this.getSharedPreferences("RoboPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		for(i=0; i<5; i++) {
			String temp = "scores" + Integer.toString(i);
			str = prefs.getString(temp, "0");
			if(!str.equals("0")) {
				highScores += str;
				highScores += "\n";
			}
			else
				highScores += "0";
				highScores += "\n";
		}
		//highScores = "1kjjk";
		solo_score.setText(highScores);
		

	}
	
	public void setNewHighScore(long ms) {
		int k;
		SharedPreferences prefs = this.getSharedPreferences("RoboPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		String temp = "";
		for(i=0; i<5; i++) {
			temp = "scores" + Integer.toString(i);
			System.out.println(temp);
			str = prefs.getString(temp, "0");
			int tempScore = Integer.parseInt(str);
			if( tempScore != 0)
				topFive[i] = tempScore;
			else
				topFive[i] = 0;
		}
		
		topFive[i] = (int) ms;
		
		Arrays.sort(topFive);
//		for(i=0; i<6; i++)
//			System.out.println(topFive[i]);
		for(i=0, k=5; i<5; i++, k--) {
			temp = "scores" + Integer.toString(i);
			editor.putString(temp, Integer.toString(topFive[k]));
			editor.commit();
		}		
	
	}
	
	public static Context getContext(){
		return ctx;
	}
	
	
	

}
