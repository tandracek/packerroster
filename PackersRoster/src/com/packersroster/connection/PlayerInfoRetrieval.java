package com.packersroster.connection;

import java.text.DateFormat;
import java.text.ParseException;

import org.jsoup.nodes.Element;

import com.packersroster.player.DraftInfo;
import com.packersroster.player.Player;
import com.packersroster.ui.SportStyles;

public class PlayerInfoRetrieval extends WebSite {
	String url;
	SportStyles sport;
	
	public PlayerInfoRetrieval() {
		super();
	}

	public PlayerInfoRetrieval(SportStyles sport) {
		super();
		this.sport = sport;
	}
	
	public Player getDetails(Player player, String playerUrl) {
		SportStyles sp = SportStyles.sportFromString(player.sport);

		switch(sp) {
		case NFL:
		case MLB:
			this.connect(playerUrl);
			this.getYahooDetails(player);
			break;
		case NBA:
		}
		return player;
	}
	
	// Uses yahoo
	public Player getYahooDetails(Player player) {
		Element bio = doc.select(".bio").get(0);
		String height = bio.select(".height dd").get(0).ownText();
		String weight = bio.select(".weight dd").get(0).ownText();
		String bornDateStr = bio.select(".born dd").get(0).ownText();
		String bornPlace = bio.select(".birthplace dd").get(0).ownText();
		String draft = bio.select(".draft dd").get(0).ownText();
		
		player.height = height;
		player.weight = weight;
		player.bornPlace = bornPlace;
		player.bornDate = bornDateStr;

		if(draft.contains("Undrafted")) {
			player.draftInfo = new DraftInfo(true);
			return player;
		}
		
		String[] draftArr = draft.split(" ");
		String round = draftArr[1];
		String year = draftArr[0];
		
		String team = "";
		for(int i = (draftArr.length - 2); i < draftArr.length; i++) {
			team += draftArr[i] + " ";
		}
		
		String pick;
		String temp = draftArr[3];
		if(temp.length() >= 4) pick = temp.substring(1, 3);
		else pick = temp.substring(1, 2);
		
		player.draftInfo = new DraftInfo(round, pick, team, year);
		return player;
	}
}
