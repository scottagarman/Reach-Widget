package com.neonlotus.android.reach;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neonlotus.android.reach.controller.ImageFetcherController;
import com.neonlotus.android.reach.controller.JsonParserController;
import com.neonlotus.android.reach.model.Player;


public class MainTab extends Activity implements OnClickListener {
	private static final String DEBUG_TAG = "ReachWidget/ReachMain";
	
	//Views
	private TextView gamertag;
	private ImageView avatar;
	private ImageButton searchButton;
	private EditText searchBox;
	private Button friendButton;
	
	//Instance
	Player mPlayer;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab);
        
        //get views
        gamertag = (TextView) findViewById(R.id.gamertag);
        avatar = (ImageView) findViewById(R.id.avatar);
        searchButton = (ImageButton) findViewById(R.id.sendbutton);
        searchBox = (EditText) findViewById(R.id.gamertagsearch);
        friendButton = (Button) findViewById(R.id.friendbutton);
        
        //listeners
        searchButton.setOnClickListener(this);
    }
     
    private void getStats(final String gTag){
    	Toast.makeText(this, "Loading Gamertag", Toast.LENGTH_SHORT).show();
    	final JsonParserController jpc = new JsonParserController();
		Thread t = new Thread(){
        	public void run(){
				try {
					Log.d(DEBUG_TAG, "Trying to get stats...");
					Message msg = Message.obtain(); 
			        final JSONObject stats = jpc.parse("http://www.bungie.net/api/reach/reachapijson.svc/player/details/nostats/DANs$7-WyOGpTthopASqbsJE96sMV0mKCGv6$FDm$7k=/" 
			        		+ URLEncoder.encode(gTag.trim(),"utf-8"));
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
    		mPlayer = new Player(stats);
    		Log.d(DEBUG_TAG, "gtag: " + mPlayer.name);
    		gamertag.setText(mPlayer.name);
    		friendButton.setVisibility(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Error getting Player:gamertag");
			//spawn a toast
			Toast.makeText(this, "Error loading gamertag :(", Toast.LENGTH_LONG).show();
		}
    }
    
    private void updateAvatar(Bitmap bm){
    	mPlayer.avatar = bm; 
    	avatar.setImageBitmap(mPlayer.avatar);
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

	public void onClick(View v) {
		switch(v.getId()){
			case R.id.sendbutton:
				Log.d(DEBUG_TAG, "Searching for: " + searchBox.getText().toString());
				this.getStats(searchBox.getText().toString());
				
		}
	}
	
	//ImageView img = (ImageView) findViewById(R.id.myImageId);
	//img.setOnClickListener(new OnClickListener() {
	  //  public void onClick(View v) {
	       // your code here
	    //}
	//});
}