package com.example.packersroster;

import java.io.IOException;
import java.util.ArrayList;

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
			grouping = details.get(i).select("h3").html();
			Elements player_eles = details.get(i).select("tr");
			for(int j = 1; j < player_eles.size(); j++) {
				Element player = player_eles.get(j);
				String number = player.select(".number").html();
				String college = player.select(".college").html();
				String age = player.select(".age").html();
				String experience = player.select(".experience").html();
				String salary = player.select(".salary").html();
				if(salary.startsWith("<span")) salary = " - "; 
				
				Element playerName = player.select(".player a").get(0);
				String name = formatName(playerName.html());
				String link = playerName.attr("href");
				
				Elements positions = player.select(".position > abbr");
				String position = "";
				int posSize = positions.size();
				for(int k = 0; k < posSize; k++) {
					if(k == 0) position = positions.get(k).html();
					else position += ("\\" + positions.get(k).html());
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
		String draft = bio.select(".draft dd").html();

		if(draft.contains("Undrafted")) {
			return new DraftInfo(true);
		}
		
		String[] draftArr = draft.split(" ");
		String round = draftArr[1].substring(0, 1);
		String year = draftArr[0];
		
		String team = "";
		for(int i = 7; i < draftArr.length; i++) {
			team += draftArr[i] + " ";
		}
		
		String pick;
		String temp = draftArr[3];
		if(temp.length() >= 4) pick = temp.substring(1, 3);
		else pick = temp.substring(1, 2);
		
		return new DraftInfo(round, pick, team, year);
	}
}
