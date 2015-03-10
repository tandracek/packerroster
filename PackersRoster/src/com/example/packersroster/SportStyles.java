package com.example.packersroster;

public enum SportStyles {

	NFL ("NFL", R.string.packer_roster, "#224728", "website_pref", R.id.position_group),
	MLB ("MLB", R.string.brewer_roster, "#12469B", "website_mlb_pref", R.id.position_mlb_group),
	NBA ("NBA", R.string.bucks_roster, "#00741C", "website_nba_pref", R.id.position_nba_group);
	
	public final String sport;
	public final int displayId;
	public final String color;
	public final String sportPref;
	public final int groupId;
	
	SportStyles(String sport, int displayId, String color, String sportPref, int groupId) {
		this.sport = sport;
		this.displayId = displayId;
		this.color = color;
		this.sportPref = sportPref;
		this.groupId = groupId;
	}
	
}
