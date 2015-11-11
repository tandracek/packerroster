package com.packersroster.websites;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.packersroster.connection.WebSite;
import com.packersroster.player.DraftInfo;
import com.packersroster.player.Player;
import com.packersroster.player.Stats;

public class ESPNSite extends WebSite {
	public ArrayList<Player> player_list;

	public ESPNSite(String Url, String sport) {
		super(null);
		player_list = new ArrayList<Player>();
	}

	public ArrayList<Player> getRoster() {
		if(!this.connect(null)) return player_list;

		Element cont = doc.getElementById("my-players-table");
		Elements tables = cont.getElementsByTag("table");
		Elements rows = tables.get(0).getElementsByTag("tr");

		Elements cells, link;
		for (int i = 2; i < rows.size(); i++) {
			cells = rows.get(i).getElementsByTag("td");
			Player tempPlayer = new Player();

			tempPlayer.number = cells.get(0).ownText();
			link = cells.get(1).getElementsByTag("a");
			if (link.size() > 0) {
				tempPlayer.link = link.get(0).attr("href");
				tempPlayer.name = link.get(0).ownText();
			}
			tempPlayer.position = cells.get(2).ownText();
			tempPlayer.group = tempPlayer.position;
			tempPlayer.age = cells.get(3).ownText();
			
			String height, weight;
			height = cells.get(4).ownText();
			weight = cells.get(5).ownText();
			tempPlayer.height = height;
			tempPlayer.weight = weight;
			tempPlayer.college = cells.get(6).ownText();
			tempPlayer.salary = cells.get(7).ownText();
			
			player_list.add(tempPlayer);
		}

		return player_list;
	}

	public DraftInfo getDraftInfo(String playerUrl) {
		this.connect(playerUrl);
		
		Element data = doc.getElementsByClass("player-metadata").get(0);
		String draftStr = data.getElementsByTag("li").get(1).ownText();
		
		String year, team, pick, round;
		String[] yearSpl = draftStr.split(":");
		year = yearSpl[0];
		
		String[] pickTeamSpl = yearSpl[1].split(",");
		String[] pickSpl = pickTeamSpl[0].split(" ");
		round = pickSpl[0];
		
		String[] teamSpl = pickTeamSpl[1].split(" ");
		pick = teamSpl[0];
		team = teamSpl[teamSpl.length];
		
		return new DraftInfo(round, pick, team, year);
	}

	public <T extends Stats> ArrayList<T> getSeasonStats(String playerUrl) {
		// TODO Auto-generated method stub
		return null;
	}

}
