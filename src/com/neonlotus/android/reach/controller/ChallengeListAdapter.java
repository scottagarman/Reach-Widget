package com.neonlotus.android.reach.controller;

import java.util.ArrayList;

import com.neonlotus.android.reach.R;
import com.neonlotus.android.reach.model.Challenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChallengeListAdapter extends BaseAdapter {
	private LayoutInflater inflator;
	private ArrayList<Challenge> challengeList;
	
	public ChallengeListAdapter(Context context, ArrayList<Challenge> objects) {
		this.challengeList = objects;
		
		this.inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = this.inflator.inflate(R.layout.challenge_row, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.challengeName);
			holder.description = (TextView) convertView.findViewById(R.id.challengeDescription);
			holder.credits = (TextView) convertView.findViewById(R.id.challengeCredits);
			holder.typeImage = (ImageView) convertView.findViewById(R.id.challengeTypeImage);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(this.challengeList.get(position).name);
		holder.description.setText(this.challengeList.get(position).description);
		holder.credits.setText(this.challengeList.get(position).credits + "CR");
		holder.typeImage.setImageBitmap(this.challengeList.get(position).isWeeklyChallenge ? null : null); //put image types here
		
		return convertView;
	}
	
	static class ViewHolder { 
		TextView name;
		TextView description;
		TextView credits;
		ImageView typeImage;
	}

	public int getCount() {
		return this.challengeList.size();
	}
	public Object getItem(int position) {
		return this.challengeList.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
}
