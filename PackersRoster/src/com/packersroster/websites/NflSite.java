package com.packersroster.websites;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.packersroster.connection.WebSite;
import com.packersroster.player.DraftInfo;
import com.packersroster.player.Player;
import com.packersroster.player.Stats;

import android.util.Log;

public class NflSite extends WebSite{
	public String url;
	
	private ArrayList<Player> player_list;
	
	private final static String NFL_MAIN_URL = "http://www.nfl.com/players/search?category=team&filter=1800&playerType=current";
	
	public NflSite(String url) {
		super(null);
		player_list = new ArrayList<Player>();
	}
	
	public ArrayList<Player> getRoster() {
		if(!this.connect(null)) return player_list;
		
		Elements result_bloc = doc.select("#result");
		result_bloc = result_bloc.select("tbody");
		Element tempE = result_bloc.remove(0);
		Elements playerRows = result_bloc.select("tr");
		
		for(int i = 0; i < playerRows.size(); i++) {
			String playerName = playerRows.get(i).select("a").first().html();
			player_list.add(new Player(playerName, "N", "1"));
		}
		
		return player_list;
	}

	public DraftInfo getDraftInfo(String playerUrl) {
		return null;
	}

	public <T extends Stats> ArrayList<T> getSeasonStats(String playerUrl) {
		return null;
	}

}
