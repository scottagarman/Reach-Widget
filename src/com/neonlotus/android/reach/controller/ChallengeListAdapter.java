package com.neonlotus.android.reach.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.neonlotus.android.reach.R;
import com.neonlotus.android.reach.model.ChallengeModel;

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
	private ChallengeModel challengeList;
	
	public ChallengeListAdapter(Context context, ChallengeModel challengeList) {
		this.challengeList = challengeList;
		
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
		
		holder.name.setText( (CharSequence) this.challengeList.getChallengeAtIndex(position).get("name"));
		holder.description.setText( (CharSequence) this.challengeList.getChallengeAtIndex(position).get("description"));
		holder.credits.setText( (CharSequence) this.challengeList.getChallengeAtIndex(position).get("credits") + "CR");
		holder.typeImage.setImageBitmap((Boolean) this.challengeList.getChallengeAtIndex(position).get("isWeeklyChallenge") ? null : null); //put image types here
		
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
		return this.challengeList.getChallengeAtIndex(position);
	}
	public long getItemId(int position) {
		return position;
	}
}
