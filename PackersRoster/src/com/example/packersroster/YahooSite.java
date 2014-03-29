package com.example.packersroster;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;



public class YahooSite implements WebSite {
	private ArrayList<Player> player_list;
	private Context context;
	private Document doc;
	private Elements details;
	ProgressDialog progress;
	
	public YahooSite(Context context) {
		player_list = new ArrayList<Player>();
		this.context = context;
	}
	
	public ArrayList<Player> getRoster() {
		return player_list;
	}
	
	public void setDraftInfo() {
		String currLink;
		for(int i =  0; i < player_list.size(); i++) {
			currLink = player_list.get(i).getLink();
			player_list.get(i).setDraftStr(getFromLink(currLink));
		}
	}
	
	public String getFromLink(String link) {
		Document doc = null;
		try {
			doc = Jsoup
					.connect(
							link)
					.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Element bio = doc.select(".bio").get(0);
		String draftInfo = bio.select(".draft dd").html();
		
		return draftInfo;
	}
	
	public String getDraftDetails() {
		Element bio = doc.select(".bio").get(0);
		String draftInfo = bio.select(".draft dd").html();
		
		return draftInfo;
	}
	
	@Override
	public boolean connect(String URL) {
		try {
			doc = Jsoup
					.connect(
							URL).header("Accept-Encoding", "gzip, deflate").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0")
					.get();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}

	@Override
	public boolean getInitialData() {
		details = doc.select("#mediasportsteamroster > .bd > .position-group");
		if(details.size() > 0) return true;
		Log.v("Initial data", "Returned false");
		return false;
	}

	@Override
	public boolean getDetails() {
		// TODO Auto-generated method stub
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
				String name = playerName.html();
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
				tempPlayer.setPosition(position);
				tempPlayer.setLink("http://sports.yahoo.com" + link);
				player_list.add(tempPlayer);
			}
		}
		if(player_list.size() > 0) return true;
		return false;
	}


}
