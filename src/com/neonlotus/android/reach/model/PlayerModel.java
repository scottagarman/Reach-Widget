package com.neonlotus.android.reach.model;

import java.net.URLEncoder;

import org.json.JSONObject;


import com.neonlotus.android.reach.REACHCONFIG;
import com.neonlotus.android.reach.util.ImageFetcher;
import com.neonlotus.android.reach.util.JsonParser;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PlayerModel {
	private final static String DEBUG = "ReachWidget";
	private final static String API_KEY = "DANs$7-WyOGpTthopASqbsJE96sMV0mKCGv6$FDm$7k=/";
	private final static String URL = "http://www.bungie.net/api/reach/reachapijson.svc/player/details/nostats/";
	
	private Thread t; //Stats Thread
	private Thread t2; //Image Thread
	private ChallengeDataListener cdl;
	
	public Player player;
	public String playerTag;
	public Bitmap image;
	
	public PlayerModel(ChallengeDataListener cdl){
		this.cdl = cdl;
	}
	
	public void searchPlayer(String playerTag){
		this.playerTag = playerTag;
		this.getPlayerFromNetwork();
	}
	public Player getCurrentPlayer(){
		return this.player;
	}
	
	
	private void getPlayerFromNetwork(){
    	final JsonParser jpc = new JsonParser();
    	if(this.t != null){
    		this.t.interrupt();
    	}
    	this.t = new Thread(){
        	public void run(){
				try {
					Log.d(DEBUG, "Trying to get player stats...");
					Message msg = Message.obtain(); 
			        final JSONObject stats = jpc.parse(URL + API_KEY + URLEncoder.encode(PlayerModel.this.playerTag.trim(),"utf-8"));
			        if(stats != null){
						msg.what = REACHCONFIG.Messages.DOWNLOAD_COMPLETE;
						msg.obj = stats;
						PlayerModel.this.handler.sendMessage(msg);	
			        }else{
						msg.what = REACHCONFIG.Messages.DOWNLOAD_FAILED;
						msg.obj = null;
						PlayerModel.this.handler.sendMessage(msg);					        	
			        }
				}catch(Exception e){
					e.printStackTrace();
					Message msg = Message.obtain(); 
					msg.what = REACHCONFIG.Messages.DOWNLOAD_FAILED;
					msg.obj = null;
					PlayerModel.this.handler.sendMessage(msg);
				}
        	}        
        };
        this.t.start();			
	}
	
    private void getImageFromNetwork(final String playerModelUrl){
        final ImageFetcher ifc = new ImageFetcher();
        if(this.t2 != null){
            this.t2.interrupt();
        }

        this.t2 = new Thread(){
        	public void run(){
				try {
					Log.d(DEBUG, "Trying to get Image...");
					Message msg = Message.obtain(); 
			        final Bitmap bm = ifc.getImageFromUrl(playerModelUrl);
			        if(bm != null){
						msg.what = REACHCONFIG.Messages.IMAGE_COMPLETE;
						msg.obj = bm;
						PlayerModel.this.handler.sendMessage(msg);	
			        }else{
						msg.what = REACHCONFIG.Messages.IMAGE_FAILED;
						msg.obj = null;
						PlayerModel.this.handler.sendMessage(msg);					        	
			        }
				}catch(Exception e){
					e.printStackTrace();
					Message msg = Message.obtain(); 
					msg.what = REACHCONFIG.Messages.IMAGE_FAILED;
					msg.obj = null;
					PlayerModel.this.handler.sendMessage(msg);
				}
        	}        
        };
        this.t2.start();
    }
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case REACHCONFIG.Messages.DOWNLOAD_COMPLETE:
					PlayerModel.this.player = new Player( (JSONObject) msg.obj);
					PlayerModel.this.getImageFromNetwork(PlayerModel.this.player.playerModelUrl);
					break;
				case REACHCONFIG.Messages.DOWNLOAD_FAILED:
					PlayerModel.this.cdl.onDataError();
					break;
				case REACHCONFIG.Messages.IMAGE_COMPLETE:
					PlayerModel.this.image = (Bitmap) msg.obj;
					PlayerModel.this.cdl.onDataRecieved();
					break;
				case REACHCONFIG.Messages.IMAGE_FAILED:
					PlayerModel.this.cdl.onDataError();
					break;
			}
		}
	};
	
	
	/**
	 * 
	 * 
	 * @author NinjaPro
	 *
	 */
	public class Player {
		
		//Player
		public String name = "";
		public String serviceTag = "";
		public String dailyChallenges = "";
		public String weeklyChallenges = "";
		public String totalGames = "";
		public double armorCompletion = 0;
		
		//General
		public String playerModelUrl = "";
		public Bitmap avatar;
		
		public Player(JSONObject jObject){
			
			//player details
			JSONObject playerDetails = jObject.optJSONObject("Player");
			this.name 				= playerDetails.optString("gamertag");
			this.serviceTag 		= playerDetails.optString("service_tag");
			this.dailyChallenges 	= playerDetails.optString("daily_challenges_completed");
			this.weeklyChallenges 	= playerDetails.optString("weekly_challenges_completed");
			this.totalGames 		= playerDetails.optString("games_total");
			this.armorCompletion	= playerDetails.optDouble("armor_completion_percentage");
			
			//general details
			this.playerModelUrl 	= jObject.optString("PlayerModelUrl");
		}	
	}


}

