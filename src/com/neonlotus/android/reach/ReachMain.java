package com.neonlotus.android.reach;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class ReachMain extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Banging comments "Bangin comments" FTFY
        Resources res = getResources(); // Resource object to get drawables
        TabHost tabHost = getTabHost(); // the activity tab host
        TabHost.TabSpec spec; // reusable tabspec for each tab
        Intent intent; // reusable intent for each tab
        
        //Create an Intent to launch an activity for th etab (to be reused)
        intent = new Intent().setClass(this, ChallengeTab.class);
        
        //Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("main").setIndicator("Main", res.getDrawable(R.drawable.ic_tab_main)).setContent(intent);
        tabHost.addTab(spec);
        
        //Other tabs
        intent = new Intent().setClass(this, ChallengeTab.class);
        spec = tabHost.newTabSpec("fav").setIndicator("Challenges", res.getDrawable(R.drawable.ic_tab_fav)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ChallengeTab.class);
        spec = tabHost.newTabSpec("about").setIndicator("Friends", res.getDrawable(R.drawable.ic_tab_about)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ChallengeTab.class);
        spec = tabHost.newTabSpec("settings").setIndicator("Settings", res.getDrawable(R.drawable.ic_tab_about)).setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0);
    }
}