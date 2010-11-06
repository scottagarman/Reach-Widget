package com.neonlotus.android.reach.model;

import org.json.JSONObject;

public class Challenge {
	
	public String name = "";
	public String credits = "";
	public String description = "";
	public String expDate = "";
	public boolean isWeeklyChallenge = false;

	public Challenge(JSONObject jObject){
		this.name = jObject.optString("Name");
		this.credits = jObject.optString("Credits");
		this.description = jObject.optString("Description");
		this.expDate = jObject.optString("ExpirationDate");
		
		this.isWeeklyChallenge = jObject.optBoolean("IsWeeklyChallenge");

	}
}

/**
"Credits": 2000,
"Description": "Kill 180 enemies in any game mode in Reach.",
"ExpirationDate": "\/Date(1289127600000-0700)\/",
"IsWeeklyChallenge": false,
"Name": "Blastin' and Relaxin'"
**/