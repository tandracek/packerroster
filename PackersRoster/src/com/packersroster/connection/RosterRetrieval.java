package com.packersroster.connection;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.packersroster.player.Player;
import com.packersroster.ui.SportStyles;

public class RosterRetrieval extends WebSite {
	private final static String YAHOO_NFL_URL = "http://sports.yahoo.com/nfl/teams/gnb/roster/";
	private final static String ESPN_NBA_URL = "http://espn.go.com/nba/team/roster/_/name/mil/milwaukee-bucks";
	private final static String YAHOO_MLB_URL = "http://sports.yahoo.com/mlb/teams/mil/roster/";
	private ArrayList<Player> playerList;
	
	public RosterRetrieval(SportStyles sport) {
		super(sport);
		this.playerList = new ArrayList<Player>();
	}
	
	public ArrayList<Player> getRoster() {
		switch(this.sport) {
		case NFL:
			this.connect(YAHOO_NFL_URL);
			this.getYahooRoster();
			break;
		case MLB:
			this.connect(YAHOO_MLB_URL);
			this.getYahooRoster();
			break;
		case NBA:
			this.connect(ESPN_NBA_URL);
			this.getEspnRoster();
			break;
		}
		return this.playerList;
	}
	
	public void getEspnRoster() {
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
			
			playerList.add(tempPlayer);
		}
	}
	
	public void getYahooRoster() {
		Elements details = doc.select("#mediasportsteamroster > .bd > .position-group");
		if(details.size() == 0) return;
				
		String grouping;
		for(int i = 0; i < details.size(); i++) {
			grouping = details.get(i).select("h3").get(0).ownText();
			Elements player_eles = details.get(i).select("tr");
			for(int j = 1; j < player_eles.size(); j++) {
				Element player = player_eles.get(j);
				String number = this.itemSelect(player, ".number");
				String college = this.itemSelect(player, ".college");
				String age = this.itemSelect(player, ".age");
				String experience = this.itemSelect(player, ".experience");
				String salary = this.itemSelect(player, ".salary");
				if(salary.startsWith("<span")) salary = " - "; 
				
				Element playerName = player.select(".player a").get(0);
				String name = formatName(playerName.ownText());
				String link = playerName.attr("href");
				
				Elements positions = player.select(".position > abbr");
				String position = "";
				int posSize = positions.size();
				for(int k = 0; k < posSize; k++) {
					if(k == 0) position = positions.get(k).ownText();
					else position += ("\\" + positions.get(k).ownText());
				}
				
				Player tempPlayer = new Player(name, position, number);
				tempPlayer.age = age;
				tempPlayer.college = college;
				tempPlayer.experience = experience;
				tempPlayer.salary = salary;
				tempPlayer.group = grouping;
				tempPlayer.position = position;
				tempPlayer.link = "http://sports.yahoo.com" + link;
				playerList.add(tempPlayer);
			}
		}
	}
	
}
