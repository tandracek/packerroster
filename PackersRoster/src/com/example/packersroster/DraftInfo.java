package com.example.packersroster;

import android.util.Log;

public class DraftInfo {
	public String round;
	public String pick;
	public String year;
	public String team;
	private boolean isUndrafted;
	
	public DraftInfo(String draft) {
		if(!draft.contains("Undrafted")) {
			this.format(draft);
			isUndrafted = false;
		}
		else isUndrafted = true;
	}
	
	private void format(String draft) {
		Log.v("DInfo", draft);
		String[] draftArr = draft.split(" ");
		round = draftArr[1].substring(0, 1);
		year = draftArr[0];
		team = buildTeam(draftArr);
		
		String temp = draftArr[3];
		if(temp.length() >= 4) pick = temp.substring(1, 3);
		else pick = temp.substring(1, 2);
	}
	
	private String buildTeam(String[] draft) {
		String team = "";
		for(int i = 7; i < draft.length; i++) {
			team += draft[i] + " ";
		}
		return team;
	}
	
	public String getDraftDisplay() {
		String returnStr;
		if(isUndrafted) returnStr = "Undrafted";
		else returnStr = this.year + " Round: " + this.round + " Pick: " + this.pick + " by the " + this.team;
		return returnStr;
	}
}
