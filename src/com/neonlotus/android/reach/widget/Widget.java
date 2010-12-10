package com.neonlotus.android.reach.widget;


import com.neonlotus.android.reach.REACHCONFIG;
import com.neonlotus.android.reach.util.ImageFetcher;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class Widget	extends AppWidgetProvider {
	private final static String DEBUG = "ReachWidget";
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(DEBUG, "WIDGET: onUpdate()");

		// Create update intent
		Intent updateIntent = new Intent(context, UpdateService.class);
		updateIntent.putExtra(REACHCONFIG.Intents.EXTRA_IDS, appWidgetIds);
		
		// Start update server
		context.startService(updateIntent);		
	} 
	
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.d(DEBUG, "WIDGET: onDelete()");
		SharedPreferences sp = context.getSharedPreferences(REACHCONFIG.Preferences.ALL, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		
		for(int appWidgetId : appWidgetIds){
			Log.d(DEBUG, "Deleteing: " + appWidgetId);
			editor.remove(REACHCONFIG.Preferences.TAG_KEY + appWidgetId);
		}
		editor.commit();
	}
	
	/**
	 * Service to handle spawning threads to download images
	 * 
	 * @author NinjaPro
	 *
	 */
    public static class UpdateService extends Service {
        public void onStart(Intent intent, int startId) {
            Log.d(DEBUG, "WIDGET: onStartService()");
            
			// Get widget IDs passed from 'intent'
			int[] appWidgetIds = intent.getIntArrayExtra(REACHCONFIG.Intents.EXTRA_IDS);
			
			// Open shared preferences and grab a few things
			SharedPreferences sp = getSharedPreferences(REACHCONFIG.Preferences.ALL, MODE_PRIVATE);
			
			String tempTag;
			for(int appWidgetId : appWidgetIds){
				tempTag = sp.getString(REACHCONFIG.Preferences.TAG_KEY + appWidgetId, null);
				if(tempTag != null){
					Log.d(DEBUG, "READING ID: " + appWidgetId + " Tag: " + tempTag);
					new UpdateThread(this, appWidgetId, tempTag).start();
				}
			}			
            
            
        }
		public IBinder onBind(Intent arg0) {
			return null;
		}
		
		//this is the thread yalllllll
	    private class UpdateThread extends Thread{
	    	private Context ctx;
	    	private int appWidgetId;
	    	private String tag;
	    	private Bitmap image;
	    	private ImageFetcher ifc;
	    	
	    	public UpdateThread(Context context, int appWidgetId, String tag){
	    		this.ctx = context;
	    		this.appWidgetId = appWidgetId;
	    		this.tag = tag;
	    		
	    		this.ifc = new ImageFetcher();
	    		this.image = null;
	    	}
	    	public void run(){
	    		Log.d(DEBUG, "RunUpdate Service for: " + tag);
	    		this.image = this.ifc.getImageFromUrl(tag);
	    		if(this.image != null){
	    			//build view
	    			Log.d(DEBUG, "Image is not null, building view");
	        		RemoteViews updateViews = REACHCONFIG.Widget.buildRemoteView(this.ctx, this.appWidgetId, this.image);
					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.ctx);
					appWidgetManager.updateAppWidget(appWidgetId, updateViews);
	    		}else{
	    			//no image show stock image
	    		}
	    	}    	
	    }
    }
}