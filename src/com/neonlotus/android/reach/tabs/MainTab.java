package com.neonlotus.android.reach.tabs;

import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neonlotus.android.reach.R;
import com.neonlotus.android.reach.R.id;
import com.neonlotus.android.reach.R.layout;
import com.neonlotus.android.reach.model.ChallengeDataListener;
import com.neonlotus.android.reach.model.PlayerModel;
import com.neonlotus.android.reach.util.FriendsManager;


public class MainTab extends Activity implements OnClickListener, ChallengeDataListener {
	private static final String DEBUG_TAG = "ReachWidget/ReachMain";
	
	//Views
	private TextView gamertag;
	private TextView games;
	private TextView armor;
	private TextView daily;
	private TextView weekly;
	private ImageView avatar;
	private ImageButton searchButton;
	private EditText searchBox;
	private Button friendButton;
	
	private NumberFormat percentFormatter;
	
	//Instance
	PlayerModel mPlayer;
	FriendsManager fc;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab);
        
        //get views
        gamertag = (TextView) findViewById(R.id.gamertag);
        games = (TextView) findViewById(R.id.gamesPlayed);
        armor = (TextView) findViewById(R.id.armorComp);
        daily = (TextView) findViewById(R.id.dailyChallenges);
        weekly = (TextView) findViewById(R.id.weeklyChallenges);
        avatar = (ImageView) findViewById(R.id.avatar);
        searchButton = (ImageButton) findViewById(R.id.sendbutton);
        searchBox = (EditText) findViewById(R.id.gamertagsearch);
        friendButton = (Button) findViewById(R.id.friendbutton);
        
        //listeners
        searchButton.setOnClickListener(this);
        friendButton.setOnClickListener(this);
        
        //init
        percentFormatter = NumberFormat.getPercentInstance();
        fc = new FriendsManager(this);
        
        this.mPlayer = new PlayerModel(this);
    }
    
    private void saveFriend(String gTag){
    	if(gTag != null && !gTag.equals("")){
    		Toast.makeText(this, "Added Friend: " + gTag, Toast.LENGTH_LONG).show();
    		fc.add(gTag);
    	}
    }

	public void onClick(View v) {
		switch(v.getId()){
			case R.id.sendbutton:
				Log.d(DEBUG_TAG, "Searching for: " + searchBox.getText().toString());
				this.mPlayer.searchPlayer(searchBox.getText().toString());
				Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
				break;
			case R.id.friendbutton:
				this.saveFriend(this.mPlayer.player.name);
				break;
				
		}
	}
	public void onDataError() {
		Log.d(DEBUG_TAG, "onDataError : (");
		Toast.makeText(this, "Sorry no luck, try again!", Toast.LENGTH_SHORT).show();
	}
	public void onDataRecieved() { 
		Log.d(DEBUG_TAG, "onDataSuccess!!");
		gamertag.setText(this.mPlayer.player.name);
		games.setText(this.mPlayer.player.totalGames);
		armor.setText( percentFormatter.format(this.mPlayer.player.armorCompletion)); //percent numberformater
		daily.setText(this.mPlayer.player.dailyChallenges);
		weekly.setText(this.mPlayer.player.weeklyChallenges);
		friendButton.setVisibility(1);
		avatar.setImageBitmap(this.mPlayer.image);
	}
}