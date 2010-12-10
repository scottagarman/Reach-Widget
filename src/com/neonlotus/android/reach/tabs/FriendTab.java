package com.neonlotus.android.reach.tabs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.neonlotus.android.reach.R;
import com.neonlotus.android.reach.R.id;
import com.neonlotus.android.reach.R.layout;
import com.neonlotus.android.reach.util.FriendsManager;


public class FriendTab extends Activity implements OnItemClickListener, OnItemLongClickListener {
    
	//Views
	private ListView friendsListView;
	
	//List Adapter
	private ArrayAdapter<String> mAdapter;
	
	//Instance
	private ArrayList<String> friendsList;
	private FriendsManager fc;
	
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
    	fc = new FriendsManager(this);
    	
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

	public boolean onItemLongClick(AdapterView<?> arg0, View v, final int pos, long id) {
		//remove friend
		//add confirm dialog box to this
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true)
		.setTitle("Confirm Delete Friend")
		.setMessage("Do you really want to delete " + fc.loadAll().get(pos) + "?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			//@Override
			public void onClick(DialogInterface dialog, int which) {
				fc.removeByIndex(pos);
				init();
				
				dialog.dismiss();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			//@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
		return false;
	}	
}