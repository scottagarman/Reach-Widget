package com.neonlotus.android.reach.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParserController {

	private static final String DEBUG_TAG = "ReachWidget/JsonParserController";
	
	public JsonParserController(){
		
	}
	
	public JSONObject parse(String url){
		String feed = null;

		URL mUrl;
		try {
			mUrl = new URL(url);			
			Log.d(DEBUG_TAG, "URL:: " + mUrl );
			URLConnection conn = mUrl.openConnection(); 
			conn.connect(); 
			InputStream is = conn.getInputStream(); 
			feed = convertStreamToString(is);
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		// ...send message to handler to populate view.
		return convertToJson(feed);
	}
	
	private JSONObject convertToJson(String jString){
		JSONObject jObject;
		try {
			jObject = new JSONObject(jString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 

		return jObject;
	}
	
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}
