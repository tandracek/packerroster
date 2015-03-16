package com.example.packersroster;

public enum SportStyles {

	NFL ("NFL", R.string.packer_roster, "#224728", "website_pref"),
	MLB ("MLB", R.string.brewer_roster, "#12469B", "website_mlb_pref"),
	NBA ("NBA", R.string.bucks_roster, "#00741C", "website_nba_pref");
	
	public final String sport;
	public final int displayId;
	public final String color;
	public final String sportPref;
	
	SportStyles(String sport, int displayId, String color, String sportPref) {
		this.sport = sport;
		this.displayId = displayId;
		this.color = color;
		this.sportPref = sportPref;
	}
	
}
