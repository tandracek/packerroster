package com.example.packersroster;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class NflSite implements WebSite{
	private ArrayList<Player> player_list;
	
	public NflSite() {
		player_list = new ArrayList<Player>();
	}
	
	public ArrayList<Player> getRoster() {
		return player_list;
	}
	
	@Override
	public boolean connect(String URL) {
		// TODO Auto-generated method stub
		Document nfl_com_doc = null;
		try {
			nfl_com_doc = Jsoup.connect(URL).get();
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
		
		if(player_list.size() > 0) return true;
		Log.v("nfl site", "returned false");
		return false;
	}

	@Override
	public boolean getInitialData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getDetails() {
		// TODO Auto-generated method stub
		return false;
	}

}
