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
import android.util.Log;

/*
 * The reason that would be for this intermediate class, instead of calling the site class directly, as this 
 *  would also handle all the database stuff.
 */
public class Connection {
	public static String sportsPref;
	
	private static int websiteId;
	private static WebSite website;
	private Context context;
	
	private final String TAG = "Connection";
	public Connection(Context context) {
		website = null;
		this.context = context;
	}
	
	public List<Player> getRoster(boolean goOnline) {
		if (!goOnline) { 
			//TODO: select based on sport
			return new Select().from(Player.class).where("sport=?", MainActivity.activeSport.sport).execute();
		}
		
		this.deriveSite();
		
		List<Player> roster;
		roster = website.getRoster();
		ActiveAndroid.beginTransaction();
		try {
			String sport = MainActivity.activeSport.sport;
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
		/* TODO: error check the parsing of the string */
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		String sPref = prefs.getString(MainActivity.activeSport.sportPref, null);
		Log.d(TAG, sPref);
		String idArr[] = sPref.split(",");
		
		int id = Integer.parseInt(idArr[0]);
		String url = idArr[1];

		Connection.websiteId = id;
		switch(websiteId) {
		case 1:
			website = new YahooSite(url);
			break;
		case 2:
			website = new NflSite(url);
			break;
		default:
			Log.d(TAG, "website id is not found");
			website = null;
		}		
	}
}
