package com.example.packersroster;

import java.util.ArrayList;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
	
	public List<Player> getRoster() {
		this.deriveSite();
		
		List<Player> roster;
		if (website == null) { /* Just get from database, no online connection */
			return new Select().from(Player.class).execute();
		}
		
		roster = website.getRoster();
		ActiveAndroid.beginTransaction();
		try {
			for(Player p : roster) {
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
	
	public DraftInfo getDraftStr(int websiteId, Player player) {
		this.deriveSite();
		
		DraftInfo d;
		if (website == null) {
			d = new Select().from(DraftInfo.class).where("Player = ?", player.getId()).executeSingle();
		} else {
			d = website.getDraftInfo(player.link);
			d.player = player;
			d.save();
		}
		
		return d;
	}
	
	public static int deleteRoster() {
		From f = new Delete().from(Player.class);
		f.execute();
		return f.count();
	}
	
	private void deriveSite() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		int id = prefs.getInt("website_pref", 1);
		if (Connection.websiteId == id) {
			return;
		}
		Connection.websiteId = id;
		
		// Ids of website based on array resource
		switch(websiteId) {
		case 1:
			website = new YahooSite();
			break;
		case 2:
			website = new NflSite();
			break;
		default:
			website = null;
		}		
	}
}
