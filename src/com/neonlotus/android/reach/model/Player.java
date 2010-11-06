package com.neonlotus.android.reach.model;

import org.json.JSONObject;

import android.graphics.Bitmap;

public class Player {
	
	//Player
	public String name = "";
	public String serviceTag = "";
	public String dailyChallenges = "";
	public String weeklyChallenges = "";
	public int totalGames = 0;
	
	//General
	public String playerModelUrl = "";
	public Bitmap avatar;
	
	public Player(JSONObject jObject){
		
		//player details
		JSONObject playerDetails = jObject.optJSONObject("Player");
		this.name 				= playerDetails.optString("gamertag");
		this.serviceTag 		= playerDetails.optString("service_tag");
		this.dailyChallenges 	= playerDetails.optString("daily_challenges_completed");
		this.weeklyChallenges 	= playerDetails.optString("weekly_challenges_completed");
		this.totalGames 		= playerDetails.optInt("games_total");
		
		//general details
		this.playerModelUrl 	= jObject.optString("PlayerModelUrl");
	}	
}
/**
{
"reason": "Okay",
"status": 0,
"AiStatistics": null,
"CurrentRankIndex": "14",
"CurrentSeasonArenaStatistics": null,
"Player": {
    "CampaignProgressCoop": "CompletedHeroic",
    "CampaignProgressSp": "PartialLegendary",
    "Initialized": true,
    "IsGuest": false,
    "LastGameVariantClassPlayed": "Competitive",
    "armor_completion_percentage": 0.143790856,
    "daily_challenges_completed": 36,
    "first_active": "\/Date(1284441420000-0700)\/",
    "gamertag": "Fr0z3nPh03n1x",
    "games_total": 453,
    "last_active": "\/Date(1289072100000-0700)\/",
    "service_tag": "R18",
    "weekly_challenges_completed": 2,
    "commendation_completion_percentage": 0.164444447
},
"PlayerModelUrl": "\/stats\/reach\/playermodel.ashx?rpis=pQ9gK9Gh4TGLdiN9GeLR5VB99N2WC3XUzpumzx04q0M%3d",
"PlayerModelUrlHiRes": "\/stats\/reach\/playermodel.ashx?rpis=I3no5XJHnLITzn6i%2fjturg%3d%3d",
"PriorSeasonArenaStatistics": null,
"StatisticsByMap": null,
"StatisticsByPlaylist": null
}

*/