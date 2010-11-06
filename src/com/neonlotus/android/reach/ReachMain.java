package com.neonlotus.android.reach;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
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
        
        //Create an Intent to launch an activity for th etab (to be reused)
        intent = new Intent().setClass(this, ChallengeTab.class);
        
        //Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("main").setIndicator("Main", res.getDrawable(R.drawable.ic_tab_main)).setContent(intent);
        tabHost.addTab(spec);
        
        //Other tabs
        intent = new Intent().setClass(this, ChallengeTab.class);
        spec = tabHost.newTabSpec("fav").setIndicator("Challenges", res.getDrawable(R.drawable.ic_tab_fav)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ChallengeTab.class);
        spec = tabHost.newTabSpec("about").setIndicator("Friends", res.getDrawable(R.drawable.ic_tab_about)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ChallengeTab.class);
        spec = tabHost.newTabSpec("settings").setIndicator("Settings", res.getDrawable(R.drawable.ic_tab_about)).setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0);

        //Banging comments
        
        JSONObject stats;
        
        JsonParserController jpc = new JsonParserController();
        //do this in new thread duh
        stats = jpc.parse("http://www.bungie.net/api/reach/reachapijson.svc/player/details/nostats/DANs$7-WyOGpTthopASqbsJE96sMV0mKCGv6$FDm$7k=/fr0z3nph03n1x");
        
        if(stats != null){
        	Log.d(DEBUG_TAG, "Results!: " + stats.toString());
        }else{
        	Log.d(DEBUG_TAG, "Failed!" );
        }

    }
}