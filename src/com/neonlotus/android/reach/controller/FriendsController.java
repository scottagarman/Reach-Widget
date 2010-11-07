package com.neonlotus.android.reach.controller;

import java.util.ArrayList;
import java.util.Arrays;

import com.neonlotus.android.reach.REACHCONFIG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class FriendsController {
	
	private Context ctx;
	
	public FriendsController(Context ctx){
		this.ctx = ctx;
	}
	
	public boolean add(String friendTag){
		SharedPreferences sp = this.ctx.getSharedPreferences(REACHCONFIG.Preferences.ALL, Context.MODE_PRIVATE);
		String friends = sp.getString(REACHCONFIG.Preferences.STORED_FRIENDS, null);
		friends = (friends == null) ? friendTag : friends + "_" + friendTag;
		
		Editor editor = sp.edit();
		editor.putString(REACHCONFIG.Preferences.STORED_FRIENDS, friends);
		editor.commit();
		
		return true;
	}
	
	public boolean removeByString(String friendTag){
		ArrayList<String> friendsList;
		SharedPreferences sp = this.ctx.getSharedPreferences(REACHCONFIG.Preferences.ALL, Context.MODE_PRIVATE);
		friendsList = new ArrayList<String>(Arrays.asList(sp.getString(REACHCONFIG.Preferences.STORED_FRIENDS, "_").split("_")));
		if(friendsList.contains(friendTag)){
			friendsList.remove(friendTag);
			Editor editor = sp.edit();
			editor.putString(REACHCONFIG.Preferences.STORED_FRIENDS, this.join(friendsList));
			editor.commit();
			return true;
		}else{
			return false;
		}
	}

	public boolean removeByIndex(int i){
		ArrayList<String> friendsList;
		SharedPreferences sp = this.ctx.getSharedPreferences(REACHCONFIG.Preferences.ALL, Context.MODE_PRIVATE);
		friendsList = new ArrayList<String>(Arrays.asList(sp.getString(REACHCONFIG.Preferences.STORED_FRIENDS, "_").split("_")));
		friendsList.remove(i);
		Editor editor = sp.edit();
		editor.putString(REACHCONFIG.Preferences.STORED_FRIENDS, this.join(friendsList));
		editor.commit();
		
		return true;
	}
	
	public ArrayList<String> loadAll(){
		ArrayList<String> friendsList = null;
		SharedPreferences sp = this.ctx.getSharedPreferences(REACHCONFIG.Preferences.ALL, Context.MODE_PRIVATE);
		String temp = sp.getString(REACHCONFIG.Preferences.STORED_FRIENDS, null);
		if(temp != null){
			friendsList = new ArrayList<String>(Arrays.asList(temp.split("_")));	
		}
		return friendsList;
	}
	
	/**
	 * <p>Joins an array of Strings using '_' as the separator.</p>
	 */
	public String join(ArrayList<String> list){
		String toRet = new String();
		int cnt = 0;
		for(String item : list){
			if(cnt == 0) toRet = item;
			else toRet += "_" + item;
			cnt++;
		}

		return toRet;
	}
}
