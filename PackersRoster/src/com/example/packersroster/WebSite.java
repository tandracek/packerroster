package com.example.packersroster;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public abstract class WebSite {
	protected String url;
	protected String sport;
	protected Document doc;
	protected ArrayList<Player> player_list;
	
	WebSite(String Url, String sport) {
		this.url = Url;
		this.sport = sport;
		player_list = new ArrayList<Player>();
	}
	
	abstract ArrayList<Player> getRoster();
	abstract DraftInfo getDraftInfo(String playerUrl);
	
	protected boolean connect(String Url) {
		String connectUrl = Url;
		if(Url == null) connectUrl = this.url; 
		try {
			doc = Jsoup
					.connect(
							connectUrl).header("Accept-Encoding", "gzip, deflate").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0")
					.get();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 
	}
}
