package com.packersroster.websites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.packersroster.connection.WebSite;
import com.packersroster.player.DraftInfo;
import com.packersroster.player.NflStats;
import com.packersroster.player.Player;
import com.packersroster.player.Stats;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;


public class YahooSite extends WebSite {
	private final static String YAHOO_MAIN_URL = "http://sports.yahoo.com/nfl/teams/gnb/roster/";
	public ArrayList<Player> player_list;
	
	public YahooSite(String url, String sport) {
		super(null);
		player_list = new ArrayList<Player>();
	}
	
	public ArrayList<Player> getRoster() {
		if(this.connect(null)) {
			this.parseRoster();
		}
		return player_list;
	}
	
	public void setDraftInfo() {
		String currLink;
		for(int i =  0; i < player_list.size(); i++) {
			currLink = player_list.get(i).link;
			player_list.get(i).draftInfo = this.getDraftInfo(currLink);
		}
	}

	private void parseRoster() {
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
				String experience = this.itemSelect(player, ".age");
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
				player_list.add(tempPlayer);
			}
		}
	}

	public DraftInfo getDraftInfo(String playerUrl) {
		this.connect(playerUrl);
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
	
	public NflStats getNflStats() {
		return null;
	}

	public <T extends Stats> ArrayList<T> getSeasonStats(String playerUrl) {
		this.connect(playerUrl);
		
		ArrayList<T> returnStats = new ArrayList<T>();
		Element careerStats = doc.getElementsByClass("yom-sports-career-stats").get(0);
		Elements conts = careerStats.getElementsByClass("data-container");
		for(int i = 0; i < conts.size(); i++) {
			Elements table = conts.get(i).getElementsByTag("table");
			String statGroup = table.attr("summary");
			
			Elements tr = table.get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
			for(int j = (tr.size() - 2); j < (tr.size() - 1); j++) {
				String cellClass;
				Elements td = tr.get(j).getElementsByTag("td");
				//returnStats.add((T)getNflStats());
			}
		}
		
		return returnStats;
	}
}
