package com.neonlotus.android.reach;

import org.json.JSONObject;

import com.neonlotus.android.reach.controller.JsonParserController;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ReachMain extends Activity {
    private static final String DEBUG_TAG = "ReachWidget/ReachMain";

    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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