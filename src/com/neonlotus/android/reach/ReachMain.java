package com.neonlotus.android.reach;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONObject;

import com.neonlotus.android.reach.controller.JsonParserController;
import android.util.Log;

public class ReachMain extends TabActivity {
	private static final String DEBUG_TAG = "ReachWidget/ReachMain";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Banging comments "Bangin comments" FTFY
        Resources res = getResources(); // Resource object to get drawables
        TabHost tabHost = getTabHost(); // the activity tab host
        TabHost.TabSpec spec; // reusable tabspec for each tab
        Intent intent; // reusable intent for each tab
        
        
        //Create an Intent to launch an activity for the tab (to be reused)
        intent = new Intent().setClass(this, MainTab.class);
        //Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("main").setIndicator("Main", res.getDrawable(R.drawable.ic_tab_main)).setContent(intent);
        tabHost.addTab(spec);
        
        
        //Other tabs
        intent = new Intent().setClass(this, ChallengeTab.class);
        spec = tabHost.newTabSpec("fav").setIndicator("Challenges", res.getDrawable(R.drawable.ic_tab_challenges)).setContent(intent);
        tabHost.addTab(spec);
        
        
        intent = new Intent().setClass(this, FriendTab.class);
        spec = tabHost.newTabSpec("about").setIndicator("Friends", res.getDrawable(R.drawable.ic_tab_friends)).setContent(intent);
        tabHost.addTab(spec);
        
        
        intent = new Intent().setClass(this, SettingsTab.class);
        spec = tabHost.newTabSpec("settings").setIndicator("Settings", res.getDrawable(R.drawable.ic_tab_settings)).setContent(intent);
        tabHost.addTab(spec);
        
        
        tabHost.setCurrentTab(0);
        
        //Banging comments
        getStats();
    }
    
    private void getStats(){
        final JsonParserController jpc = new JsonParserController();
        //do this in new thread duh
		Thread t = new Thread(){
        	public void run(){
					try {
						Log.d(DEBUG_TAG, "Trying to get stats...");
						Message msg = Message.obtain(); 
				        final JSONObject stats = jpc.parse("http://www.bungie.net/api/reach/reachapijson.svc/player/details/nostats/DANs$7-WyOGpTthopASqbsJE96sMV0mKCGv6$FDm$7k=/fr0z3nph03n1x");
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
    
    private void updateUI(JSONObject stats){
    	try {
    		Log.d(DEBUG_TAG, "stats = " + stats.getJSONObject("Player").optString("gamertag"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Error getting Player:gamertag");
		}
    }
    
    
	/**
	 * Thread handler
	 */
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case REACHCONFIG.Messages.DOWNLOAD_COMPLETE:
		        	JSONObject stats = (JSONObject) msg.obj;
					Log.d(DEBUG_TAG, "Results: " + stats.toString());
					updateUI(stats);
					break;
				case REACHCONFIG.Messages.DOWNLOAD_FAILED:
		        	Log.d(DEBUG_TAG, "Failed!" );
					break;
			}
		}
	};
}