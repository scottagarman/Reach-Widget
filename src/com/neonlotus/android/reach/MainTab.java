package com.neonlotus.android.reach;

import org.json.JSONObject;

import com.neonlotus.android.reach.controller.ImageFetcherController;
import com.neonlotus.android.reach.controller.JsonParserController;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainTab extends Activity {
	private static final String DEBUG_TAG = "ReachWidget/ReachMain";
	
	//change gamer tag here!
	private static final String GAMER_TAG = "fr0z3nph03n1x";
	
	//Views
	TextView gamertag;
	ImageView avatar;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab);
        
        //get views
        gamertag = (TextView) findViewById(R.id.gamertag);
        avatar = (ImageView) findViewById(R.id.avatar);
        
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
				        final JSONObject stats = jpc.parse("http://www.bungie.net/api/reach/reachapijson.svc/player/details/nostats/DANs$7-WyOGpTthopASqbsJE96sMV0mKCGv6$FDm$7k=/" + GAMER_TAG);
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
    
    private void getImage(final JSONObject jObject){
        final ImageFetcherController ifc = new ImageFetcherController();
    	Thread t2 = new Thread(){
        	public void run(){
					try {
						Log.d(DEBUG_TAG, "Trying to get Image...");
						Message msg = Message.obtain(); 
				        final Bitmap bm = ifc.getImageFromUrl("http://www.bungie.net/" + jObject.getString("PlayerModelUrl"));
				        if(bm != null){
							msg.what = REACHCONFIG.Messages.IMAGE_COMPLETE;
							msg.obj = bm;
							mHandler.sendMessage(msg);	
				        }else{
							msg.what = REACHCONFIG.Messages.IMAGE_FAILED;
							msg.obj = null;
							mHandler.sendMessage(msg);					        	
				        }
					}catch(Exception e){
						e.printStackTrace();
						Message msg = Message.obtain(); 
						msg.what = REACHCONFIG.Messages.IMAGE_FAILED;
						msg.obj = null;
						mHandler.sendMessage(msg);
					}
        	}        
        };
        t2.start();      	
    }
    
    private void updateUI(JSONObject stats){
    	try {
    		Log.d(DEBUG_TAG, "stats = " + stats.getJSONObject("Player").optString("gamertag"));
    		gamertag.setText(stats.getJSONObject("Player").optString("gamertag"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Error getting Player:gamertag");
			//spawn a toast
			Toast.makeText(this, "Error loading gamertag :(", Toast.LENGTH_LONG).show();
		}
    }
    
    private void updateAvatar(Bitmap bm){
    	 avatar.setImageBitmap(bm);
    }
    
	/**
	 * Thread handler
	 */
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case REACHCONFIG.Messages.DOWNLOAD_COMPLETE:
					getImage( (JSONObject) msg.obj);
					updateUI( (JSONObject) msg.obj);
					break;
				case REACHCONFIG.Messages.DOWNLOAD_FAILED:
		        	Log.d(DEBUG_TAG, "Failed!" );
					break;
				case REACHCONFIG.Messages.IMAGE_COMPLETE:
					Log.d(DEBUG_TAG, "Image Download" );
					updateAvatar((Bitmap) msg.obj);
					break;
				case REACHCONFIG.Messages.IMAGE_FAILED:
					Log.d(DEBUG_TAG, "Image DL Failed" );
					break;
			}
		}
	};
}