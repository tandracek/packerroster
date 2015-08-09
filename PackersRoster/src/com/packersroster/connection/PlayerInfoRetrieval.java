package com.packersroster.connection;

import org.jsoup.nodes.Element;

import com.packersroster.player.DraftInfo;
import com.packersroster.ui.SportStyles;

public class PlayerInfoRetrieval extends WebSite {
	SportStyles sport;
	
	public PlayerInfoRetrieval() {
		super();
	}

	public PlayerInfoRetrieval(SportStyles sport) {
		super();
		this.sport = sport;
	}
	
	public DraftInfo getDraftInfo(String url) {
		this.connect(url);
		Element bio = doc.select(".bio").get(0);
		String draft = bio.select(".draft dd").get(0).ownText();

		if(draft.contains("Undrafted")) {
			return new DraftInfo(true);
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
		
		return new DraftInfo(round, pick, team, year);
	}
}
