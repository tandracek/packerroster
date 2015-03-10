package com.example.packersroster;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class NflSite extends WebSite{
	public String url;
	
	private ArrayList<Player> player_list;
	
	private final String NFL_MAIN_URL = "http://www.nfl.com/players/search?category=team&filter=1800&playerType=current";
	
	public NflSite() {
		player_list = new ArrayList<Player>();
	}
	
	public NflSite(String url) {
		this.url = url;
	}
	
	@Override
	public ArrayList<Player> getRoster() {
		// TODO Auto-generated method stub
		Document nfl_com_doc = null;
		try {
			nfl_com_doc = Jsoup.connect(NFL_MAIN_URL).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Elements result_bloc = nfl_com_doc.select("#result");
		result_bloc = result_bloc.select("tbody");
		Element tempE = result_bloc.remove(0);
		Elements playerRows = result_bloc.select("tr");
		
		for(int i = 0; i < playerRows.size(); i++) {
			String playerName = playerRows.get(i).select("a").first().html();
			player_list.add(new Player(playerName, "N", "1"));
		}
		
		return player_list;
	}

	@Override
	DraftInfo getDraftInfo(String playerUrl) {
		// TODO Auto-generated method stub
		return null;
	}

}
