package com.neonlotus.android.reach.widget;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.neonlotus.android.reach.R;
import com.neonlotus.android.reach.REACHCONFIG;
import com.neonlotus.android.reach.controller.ImageFetcherController;
import com.neonlotus.android.reach.controller.JsonParserController;
import com.neonlotus.android.reach.model.Player;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

public class WidgetConfigure extends Activity implements OnClickListener{
	private EditText editText;
	private Button button;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_layout_stub);
        
        editText = (EditText) findViewById(R.id.configureEditText);
        button = (Button) findViewById(R.id.configureButton);
        
        button.setOnClickListener(this);
    }

    public void setWidget(){
    	String value;
    	JSONObject stats = null;
    	value = editText.getText().toString();
    	
    	JsonParserController jpc = new JsonParserController();
    	try {
    		stats = jpc.parse("http://www.bungie.net/api/reach/reachapijson.svc/player/details/nostats/DANs$7-WyOGpTthopASqbsJE96sMV0mKCGv6$FDm$7k=/" 
					+ URLEncoder.encode(value.trim(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(stats != null){
			Player p = new Player(stats);
			ImageFetcherController ifc = new ImageFetcherController();
			Bitmap image = ifc.getImageFromUrl(p.playerModelUrl);
			if(image != null){
				updateWidget(this, p.playerModelUrl, image);
			}
		}
		
    	
    }
    
	public void updateWidget(Context context, String url, Bitmap image){
		int appWidgetId = 0;
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		SharedPreferences sp = getSharedPreferences(REACHCONFIG.Preferences.ALL, MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(REACHCONFIG.Preferences.TAG_KEY+appWidgetId, url);
		editor.commit();
		
		//tempTag = sp.getString(REACHCONFIG.Preferences.TAG_KEY + appWidgetId, null);
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.w_main);
		views.setImageViewBitmap(R.id.widgetavatar, image);

		appWidgetManager.updateAppWidget(appWidgetId, views);
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();			
	}
    
    
    
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.configureButton:
				setWidget();
				break;
		}
		
	}
}
