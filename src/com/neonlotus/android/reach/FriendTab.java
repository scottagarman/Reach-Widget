package com.neonlotus.android.reach;

import java.util.ArrayList;

import com.neonlotus.android.reach.controller.FriendsController;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


public class FriendTab extends Activity implements OnItemClickListener, OnItemLongClickListener {
    
	//Views
	private ListView friendsListView;
	
	//List Adapter
	private ArrayAdapter<String> mAdapter;
	
	//Instance
	private ArrayList<String> friendsList;
	private FriendsController fc;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_tab);
        
        //Set up views
        friendsListView = (ListView) findViewById(R.id.friendsList);
        friendsListView.setOnItemClickListener(this);
        friendsListView.setOnItemLongClickListener(this);
        
        //instance
    	fc = new FriendsController(this);
    	
        //load list
        init();
    }
    
    
    
    @Override
	protected void onResume() {
		super.onResume();
		init();
	}



	public void init(){
    	friendsList = fc.loadAll(); 
    	if(friendsList != null){
        	mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendsList);
        	friendsListView.setAdapter(mAdapter);		
    	}else{
    		Toast.makeText(this, "No friends noob!", Toast.LENGTH_SHORT).show();
    	}
    }

	public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		//switch tabs to main and pass in friendtag
		
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View v, int pos, long id) {
		//remove friend
		//add confirm dialog box to this
		fc.removeByIndex(pos);
		init();
		return false;
	}
}