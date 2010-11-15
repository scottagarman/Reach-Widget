package com.neonlotus.android.reach.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.neonlotus.android.reach.REACHCONFIG;
import com.neonlotus.android.reach.controller.JsonParserController;

public class ChallengeModel {
	private final static String DEBUG = "ReachWidget";
	private final static String API_KEY = "DANs$7-WyOGpTthopASqbsJE96sMV0mKCGv6$FDm$7k=";
	private final static String URL = "http://www.bungie.net/api/reach/reachapijson.svc/game/challenges/";
	
	//Listener
	private ChallengeDataListener cdl;
	
	//Datas
	private ArrayList<HashMap> challenges;
	
	public ChallengeModel(ChallengeDataListener listener){
		this.cdl = listener;
		this.challenges = new ArrayList<HashMap>();
		this.getChallengesFromNetwork();
	}

	public HashMap getChallengeAtIndex(int index){
		return this.challenges.get(index);
	}
	
	public int size(){
		return this.challenges.size();
	}
	
    private void getChallengesFromNetwork(){
    	Log.d(DEBUG, "Loading challenges from network.");
    	final JsonParserController jpc = new JsonParserController();
		Thread t = new Thread(){
        	public void run(){
				try {
					Message msg = Message.obtain(); 
			        final JSONObject data = jpc.parse(URL + API_KEY);
			        if(data != null){
						msg.what = REACHCONFIG.Messages.DOWNLOAD_COMPLETE;
						msg.obj = data;
						ChallengeModel.this.handler.sendMessage(msg);	
			        }else{
						msg.what = REACHCONFIG.Messages.DOWNLOAD_FAILED;
						msg.obj = null;
						ChallengeModel.this.handler.sendMessage(msg);					        	
			        }
				}catch(Exception e){
					e.printStackTrace();
					Message msg = Message.obtain(); 
					msg.what = REACHCONFIG.Messages.DOWNLOAD_FAILED;
					msg.obj = null;
					ChallengeModel.this.handler.sendMessage(msg);
				}
        	}        
        };
        t.start();    	
    }
    
    private void prepareData(JSONObject jObject){
    	HashMap hm;
    	JSONObject ch;
    	JSONArray daily = jObject.optJSONArray("Daily");
    	JSONArray weekly = jObject.optJSONArray("Weekly");
		for (int i=0; i < daily.length(); i++) {
	    	hm = new HashMap();
	    	try {
				ch = daily.getJSONObject(i);
		    	hm.put("name", ch.optString("Name"));
		    	hm.put("credits", ch.optString("Credits"));
		    	hm.put("description", ch.optString("Description"));
		    	hm.put("expDate", ch.optString("ExpirationDate"));
		    	hm.put("isWeeklyChallenge", (Boolean) ch.optBoolean("IsWeeklyChallenge"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    	
	    	this.challenges.add(hm);
		}
		for (int i=0; i < weekly.length(); i++) {
	    	hm = new HashMap();
	    	try {
				ch = weekly.getJSONObject(i);
		    	hm.put("name", ch.optString("Name"));
		    	hm.put("credits", ch.optString("Credits"));
		    	hm.put("description", ch.optString("Description"));
		    	hm.put("expDate", ch.optString("ExpirationDate"));
		    	hm.put("isWeeklyChallenge", (Boolean) ch.optBoolean("IsWeeklyChallenge"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    	
	    	this.challenges.add(hm);
		}
    }
	
	/**
	 * Thread handler
	 */
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case REACHCONFIG.Messages.DOWNLOAD_COMPLETE:
					Log.d(DEBUG, "Loading challenges succuessfull.");
					prepareData( (JSONObject) msg.obj);
					ChallengeModel.this.cdl.onDataRecieved();
					break;
				case REACHCONFIG.Messages.DOWNLOAD_FAILED:
					Log.d(DEBUG, "Loading challenges network failed.");
					ChallengeModel.this.cdl.onDataError();
					break;
			}
		}
	};
}
