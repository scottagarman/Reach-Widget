package com.neonlotus.android.reach;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

public class REACHCONFIG {

	public static class Messages{
		public static final int DOWNLOAD_COMPLETE 	= 200;
		public static final int DOWNLOAD_FAILED		= 100;
		
		public static final int IMAGE_COMPLETE		= 201;
		public static final int IMAGE_FAILED		= 101;
	}
	
	public static class Preferences{
		public static final String ALL = "_ReachPreferences";
		public static final String STORED_FRIENDS = "_Friends";
		public static final String TAG_KEY = "_REACHTAG";
	}
	
	public static class Intents{
		public static final String EXTRA_IDS = "_EXTRAIDSBRO";
	}
	
	public static class Widget{
		 public static RemoteViews buildRemoteView(Context context, int appWidgetId, Bitmap image){
			 RemoteViews toRet = new RemoteViews(context.getPackageName(), R.layout.w_main);
	         toRet.setImageViewBitmap(R.id.widgetavatar, image);
	         
	         return toRet;
		 }
	}
}
