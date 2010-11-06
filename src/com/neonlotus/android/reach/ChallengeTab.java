package com.neonlotus.android.reach;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.neonlotus.android.reach.controller.JsonParserController;
import com.neonlotus.android.reach.model.Challenge;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class ChallengeTab extends Activity {
    
	private static final String DEBUG_TAG = "ReachWidget";
	
	//Views
	private ListView challengeList;
	
	//List Adapter
	private ArrayAdapter<String> mAdapter;
	
	//member
	private ArrayList<Challenge> mChallenges;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_tab);
        
        challengeList = (ListView) findViewById(R.id.challengesList);
        
        //getchallenges!
        getChallenges();
        
    }
    
    
    public void populateChallenges(JSONObject challenges){
		JSONArray daily;
		try {
			daily = challenges.getJSONArray("Daily");
			JSONArray weekly = challenges.getJSONArray("Weekly");
			mChallenges = new ArrayList<Challenge>();
			for (int i=0; i < daily.length(); i++) {
				mChallenges.add(new Challenge(daily.getJSONObject(i)));
			}
			for (int i=0; i < weekly.length(); i++) {
				mChallenges.add(new Challenge(weekly.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> list = new ArrayList<String>();
		for(Challenge c : mChallenges){
			list.add(c.name);
		}
    	if(list != null){
    		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
    		challengeList.setAdapter(mAdapter);	
    	}
    }
    
    public void getChallenges(){
    	Toast.makeText(this, "Loading Challenges...", Toast.LENGTH_SHORT).show();
    	final JsonParserController jpc = new JsonParserController();
		Thread t = new Thread(){
        	public void run(){
				try {
					Log.d(DEBUG_TAG, "Trying to get challenges...");
					Message msg = Message.obtain(); 
			        final JSONObject stats = jpc.parse("http://www.bungie.net/api/reach/reachapijson.svc/game/challenges/DANs$7-WyOGpTthopASqbsJE96sMV0mKCGv6$FDm$7k=");
			        if(stats != null){
						msg.what = REACHCONFIG.Messages.DOWNLOAD_COMPLETE;
						msg.obj = stats;
						mHandler.sendMessage(msg);	
			        }else{
						msg.what = REACHCONFIG.Messages.DOWNLOAD_FAILED;
						msg.obj = null;
						mHandler.sendMessage(msg);					        	
			        }
				}catch(Exception e){
					e.printStackTrace();
					Message msg = Message.obtain(); 
					msg.what = REACHCONFIG.Messages.DOWNLOAD_FAILED;
					msg.obj = null;
					mHandler.sendMessage(msg);
				}
        	}        
        };
        t.start();    	
    }
    
	/**
	 * Thread handler
	 */
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case REACHCONFIG.Messages.DOWNLOAD_COMPLETE:
					populateChallenges( (JSONObject) msg.obj);
					//updateUI( (JSONObject) msg.obj);
					break;
				case REACHCONFIG.Messages.DOWNLOAD_FAILED:
		        	Log.d(DEBUG_TAG, "Failed!" );
					break;
			}
		}
	};
    
}