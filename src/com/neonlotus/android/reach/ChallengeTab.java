package com.neonlotus.android.reach;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.neonlotus.android.reach.controller.JsonParserController;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


public class ChallengeTab extends Activity {
    
	private static final String DEBUG_TAG = "ReachWidget";
	
	//Views
	private ListView challengeList;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_tab);
        
    }
    
    
    public void populateChallenges(JSONObject challenges){
    	
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