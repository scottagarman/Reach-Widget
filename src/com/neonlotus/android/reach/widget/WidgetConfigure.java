package com.neonlotus.android.reach.widget;

import com.neonlotus.android.reach.R;
import com.neonlotus.android.reach.REACHCONFIG;
import com.neonlotus.android.reach.model.ChallengeDataListener;
import com.neonlotus.android.reach.model.PlayerModel;

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
import android.widget.Toast;

public class WidgetConfigure extends Activity implements OnClickListener, ChallengeDataListener{
	private EditText editText;
	private Button button;
	private PlayerModel player;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_layout_stub);
        
        editText = (EditText) findViewById(R.id.configureEditText);
        button = (Button) findViewById(R.id.configureButton);
        
        button.setOnClickListener(this);
        
    	this.player = new PlayerModel(this);
    }

    public void setWidget(){
    	Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
    	String value = editText.getText().toString();
    	this.player.searchPlayer(value);
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

	public void onDataError() {
		Toast.makeText(this, "Sorry no luck, try again!", Toast.LENGTH_SHORT).show();
	}

	public void onDataRecieved() {
		this.updateWidget(this, this.player.player.name, this.player.image);
	}
}
