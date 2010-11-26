package com.neonlotus.android.reach.tabs;

import com.neonlotus.android.reach.R;
import com.neonlotus.android.reach.R.id;
import com.neonlotus.android.reach.R.layout;
import com.neonlotus.android.reach.controller.ChallengeListAdapter;
import com.neonlotus.android.reach.model.ChallengeDataListener;
import com.neonlotus.android.reach.model.ChallengeModel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;


public class ChallengeTab extends Activity implements ChallengeDataListener {
    
	private static final String DEBUG = "ReachWidget";
	
	//Views
	private ListView challengeList;
	
	//List Adapter
	private ChallengeListAdapter mAdapter;
	
	//Model
	ChallengeModel challengeModel;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_tab);
        
        challengeList = (ListView) findViewById(R.id.challengesList);
        challengeModel = new ChallengeModel(this);
        
    	mAdapter = new ChallengeListAdapter(this, challengeModel);
    	challengeList.setAdapter(mAdapter);	
    }

	public void onDataError() {
	}

	public void onDataRecieved() {
		mAdapter.notifyDataSetChanged();
	}
}