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

public class YahooSite extends WebSite {
	private ArrayList<Player> player_list;
	private Document doc;
	
	private final String YAHOO_MAIN_URL = "http://sports.yahoo.com/nfl/teams/gnb/roster/";
	
	public YahooSite() {
		player_list = new ArrayList<Player>();
	}
	
	public ArrayList<Player> getRoster() {
		this.connect(YAHOO_MAIN_URL);
		this.parseRoster();
		return player_list;
	}
	
	public void setDraftInfo() {
		String currLink;
		for(int i =  0; i < player_list.size(); i++) {
			currLink = player_list.get(i).link;
			player_list.get(i).draftInfo = this.getDraftInfo(currLink);
		}
	}
	
	private boolean connect(String URL) {
		try {
			doc = Jsoup
					.connect(
							URL).header("Accept-Encoding", "gzip, deflate").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0")
					.get();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 
	}

	private void parseRoster() {
		Elements details = doc.select("#mediasportsteamroster > .bd > .position-group");
		if(details.size() == 0) return;
				
		for(int i = 0; i < details.size(); i++) {
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
				String groupPos = getGrouping(position);
				
				Player tempPlayer = new Player(name, position, number);
				tempPlayer.age = age;
				tempPlayer.college = college;
				tempPlayer.experience = experience;
				tempPlayer.salary = salary;
				tempPlayer.group = groupPos;
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
	
	public static String getGrouping(String pos) {
		if(pos.contains("QB")) return "QB";
		if(pos.contains("RB") || pos.contains("FB")) return "RB";
		if(pos.contains("WR")) return "WR";
		if(pos.contains("TE")) return "TE";
		if(pos.contains("OT") || pos.contains("OG") || pos.equals("C")) return "OL";
		if(pos.contains("NT") || pos.contains("DE")) return "DL";
		if(pos.contains("LB")) return "LB";
		if(pos.contains("CB")) return "CB";
		if(pos.contains("FS") || pos.contains("SS")) return "S";
		else return "SP";
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
