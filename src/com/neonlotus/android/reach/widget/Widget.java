package com.neonlotus.android.reach.widget;

import java.util.Timer;

import com.neonlotus.android.reach.REACHCONFIG;
import com.neonlotus.android.reach.controller.ImageFetcherController;


import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
		
		/*
		final int N = appWidgetIds.length;
		
		    // Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];
			
			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, ExampleActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			
			// Get the layout for the App Widget and attach an on-click listener to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_layout);
			views.setOnClickPendingIntent(R.id.button, pendingIntent);
			
			// Tell the AppWidgetManager to perform an update on the current App Widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		*/
		
	} 
	
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		
		//remove pref
	}
	
	/**
	 * Service to handle spawning threads to download images
	 * 
	 * @author NinjaPro
	 *
	 */
    public static class UpdateService extends Service {
        public void onStart(Intent intent, int startId) {
            Log.d(DEBUG, "WIDGET: onStart()");
            
			// Get widget IDs passed from 'intent'
			int[] appWidgetIds = intent.getIntArrayExtra(REACHCONFIG.Intents.EXTRA_IDS);
			
			// Open shared preferences and grab a few things
			SharedPreferences sp = getSharedPreferences(REACHCONFIG.Preferences.ALL, MODE_PRIVATE);
			
			String tempTag;
			for(int appWidgetId : appWidgetIds){
				tempTag = sp.getString(TAG_KEY + appWidgetId, null);
				if(tempTag != null){
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
	    	private ImageFetcherController ifc;
	    	
	    	public UpdateThread(Context context, int appWidgetId, String tag){
	    		this.ctx = context;
	    		this.appWidgetId = appWidgetId;
	    		this.tag = tag;
	    		
	    		this.ifc = new ImageFetcherController();
	    		this.image = null;
	    	}
	    	public void run(){
	    		this.image = this.ifc.getImageFromUrl("http://www.bungie.net/" + tag);
	    		if(this.image != null){
	    			//build view
	        		RemoteViews updateViews = REACHCONFIG.Widget.buildUpdate(this.ctx, this.appWidgetId, this.image);
					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
					appWidgetManager.updateAppWidget(appWidgetId, updateViews);
	    		}else{
	    			//no image show stock image
	    		}
	    	}    	
	    }
    }
}