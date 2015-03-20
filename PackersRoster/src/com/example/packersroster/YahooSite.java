package com.example.packersroster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/*
 * TODO: move the connection method to super website class, put document and list in there as well
 */
public class YahooSite extends WebSite {
	private final static String YAHOO_MAIN_URL = "http://sports.yahoo.com/nfl/teams/gnb/roster/";
	
	public YahooSite() {
		super(YAHOO_MAIN_URL, "NFL");
	}
	
	public YahooSite(String url, String sport) {
		super(url, sport);
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
				String number = player.select(".number").get(0).ownText();
				String college = player.select(".college").get(0).ownText();
				String age = player.select(".age").get(0).ownText();
				String experience = player.select(".experience").get(0).ownText();
				String salary = player.select(".salary").get(0).ownText();
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
	
	private String formatName(String name) {
		String[] names = name.split(" ");
		
		if(names.length > 1) {
			String returnStr = names[1] + ", " + names[0];
			return returnStr;
		}
		return name;
	}

	@Override
	DraftInfo getDraftInfo(String playerUrl) {
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
	
	public ArrayList<NflStats> getSeasonNflStats(String playerUrl) {
		this.connect(playerUrl);
		Element careerStats = doc.getElementsByClass("yom-sports-career-stats").get(0);
		Elements conts = careerStats.getElementsByClass("data-container");
		for(int i = 0; i < conts.size(); i++) {
			Elements table = conts.get(i).getElementsByTag("table");
			String statGroup = table.attr("summary");
			
			Elements tr = table.get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
			//TODO: once this can be tested, can remove the limits so it can grab all seasons
			for(int j = (tr.size() - 2); j < (tr.size() - 1); j++) {
				Elements td = tr.get(j).getElementsByTag("td");
				for(int k = 0; k < td.size(); k++) {
					//Dont really need a lot, can figure out a lot (compl pct, yds/game, ect) by doig math on my own
				}
			}
		}
		
		return null;
	}
}
