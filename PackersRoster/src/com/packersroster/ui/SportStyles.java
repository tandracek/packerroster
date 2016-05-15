package com.packersroster.ui;

import com.example.packersroster.R;
import com.packersroster.player.MlbStats;
import com.packersroster.player.NbaStats;
import com.packersroster.player.NflStats;

public enum SportStyles {

	NFL ("NFL", R.string.packer_roster, "#224728", "website_pref", NflStats.class),
	MLB ("MLB", R.string.brewer_roster, "#12469B", "website_mlb_pref", MlbStats.class),
	NBA ("NBA", R.string.bucks_roster, "#00741C", "website_nba_pref", NbaStats.class);
	
	public final String sport;
	public final int displayId;
	public final String color;
	public final String sportPref;
	public final Class statClass;
	
	SportStyles(String sport, int displayId, String color, String sportPref, Class statClass) {
		this.sport = sport;
		this.displayId = displayId;
		this.color = color;
		this.sportPref = sportPref;
		this.statClass = statClass;
	}
	
	public static SportStyles sportFromString(String sport) {
		for (SportStyles s : SportStyles.values()) {
			if (s.sport.equals(sport)) return s;
		}
		return null;
	}
}
