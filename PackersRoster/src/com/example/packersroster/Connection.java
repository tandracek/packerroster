package com.example.packersroster;

import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/*
 * The reason that would be for this intermediate class, instead of calling the site class directly, as this 
 *  would also handle all the database stuff.
 */
public class Connection {
	private static int websiteId;
	private static WebSite website;
	private Context context;
	public Connection(Context context) {
		website = null;
		this.context = context;
	}
	
	public List<Player> getRoster(boolean goOnline) {
		if (!goOnline) { 
			return new Select().from(Player.class).execute();
		}
		
		this.deriveSite();
		
		List<Player> roster;
		roster = website.getRoster();
		ActiveAndroid.beginTransaction();
		try {
			String sport = MainActivity.sport;
			for(Player p : roster) {
				p.sport = sport;
				p.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}

		return roster;
	}
	
	public static Player getPlayerById(Long playerId) {
		return Model.load(Player.class, playerId);
	}
	
	public DraftInfo getDraftStr(boolean goOnline, Player player) {
		if(!goOnline) {
			return new Select().from(DraftInfo.class).where("Player = ?", player.getId()).executeSingle();
		}
		
		this.deriveSite();
		
		DraftInfo d = website.getDraftInfo(player.link);
		d.player = player;
		d.save();

		return d;
	}
	
	public static int deleteRoster() {
		From f = new Delete().from(Player.class);
		f.execute();
		return f.count();
	}
	
	private void deriveSite() {
		if (Connection.websiteId == 0) {
			website = null;
			return;
		}
		
		/* TODO: error check the parsing of the string
		 *       also figure out how to select the right preference (mlb or nfl?)
		 *        -have a custom preference tied to the sport (have a class in charge of it), then
		 *         have the preference values be the string keys to the url  */
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String idArr[] = prefs.getString(MainActivity.sportPref, null).split(",");
		int id = Integer.parseInt(idArr[0]);
		String url = idArr[1];
		
		if (Connection.websiteId == id) {
			return;
		}
		Connection.websiteId = id;
		switch(websiteId) {
		case 1:
			website = new YahooSite(url);
			break;
		case 2:
			website = new NflSite(url);
			break;
		default:
			website = null;
		}		
	}
}
