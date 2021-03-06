package com.neonlotus.android.reach.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageFetcher {
	private final static String DEBUG = "ReachWidget";
	private static final String URL_PREFIX = "http://www.bungie.net";
	
	public ImageFetcher(){
		
	}
	public Bitmap getImageFromUrl(String url){
    	Bitmap bm = null;
		try {
            /* Open a new URL and get the InputStream to load data from it. */ 
			Log.d(DEBUG, "getting img: " + URL_PREFIX+url);
            URL aURL = new URL(URL_PREFIX + url);
            URLConnection conn = aURL.openConnection(); 
            conn.connect(); 
            InputStream is = conn.getInputStream(); 
            /* Buffered is always good for a performance plus. */ 
            BufferedInputStream bis = new BufferedInputStream(is); 
            /* Decode url-data to a bitmap. */ 
            bm = BitmapFactory.decodeStream(bis);
            bis.close(); 
            is.close(); 
    	} catch (Exception e) {
    		Log.d(DEBUG, "Failed to download Image");
    		bm = null;
    	}
    	return bm;
	}
	
}
