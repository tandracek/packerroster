package com.packersroster.connection;

import java.text.DateFormat;
import java.text.ParseException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.packersroster.player.DraftInfo;
import com.packersroster.player.Player;
import com.packersroster.ui.SportStyles;

public class PlayerInfoRetrieval extends WebSite {
	String url;
	
	public PlayerInfoRetrieval() {
		super(null);
	}

	public PlayerInfoRetrieval(SportStyles sport) {
		super(sport);
	}
	
	public Player getDetails(Player player, String playerUrl) {
		SportStyles sp = SportStyles.sportFromString(player.sport);

		this.connect(playerUrl);
		switch(sp) {
		case NFL:
		case MLB:
			this.getYahooDetails(player);
			break;
		case NBA:
			this.getEspnDetails(player);
			break;
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
	
	public Player getEspnDetails(Player player) {
		Element data = doc.getElementsByClass("player-bio").get(0);
		Element genInfo = data.getElementsByClass("general-info").get(0);
		Element heightWeight = genInfo.getElementsByTag("li").get(1);
		
		String heightWeightStr = null;
		if (heightWeight != null) heightWeightStr = heightWeight.ownText();
		String height, weight;
		if (heightWeightStr != null) {
			String[] hwArr = heightWeightStr.split(",");
			if (hwArr.length > 1) {
				height = hwArr[0];
				weight = hwArr[1];
				player.height = height;
				player.weight = weight;
			}
		}
		
		Elements pGenLi = data.select(".player-metadata li");
		String bornPlace, bornDate;
		if (pGenLi.size() > 0) {
			String bornInfo = pGenLi.get(0).ownText();
			String[] bornInfoArr = bornInfo.split("in");
			bornDate = bornInfoArr[0].trim();
			bornPlace = bornInfoArr[1].split("(")[0].trim();
			player.bornDate = bornDate;
			player.bornPlace = bornPlace;
			
			String draftStr = pGenLi.get(1).ownText();
			
			String year, team, pick, round;
			String[] yearSpl = draftStr.split(":");
			year = yearSpl[0];
			
			String[] pickTeamSpl = yearSpl[1].split(",");
			String[] pickSpl = pickTeamSpl[0].split(" ");
			round = pickSpl[0];
			
			String[] teamSpl = pickTeamSpl[1].split(" ");
			pick = teamSpl[0];
			team = teamSpl[teamSpl.length];
			
			player.draftInfo = new DraftInfo(round, pick, team, year);
		}
		
		return player;
	}
}
