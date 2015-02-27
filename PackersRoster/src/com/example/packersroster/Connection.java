package com.example.packersroster;

import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

/*
 * The reason that would be for this intermediate class, instead of calling the site class directly, as this 
 *  would also handle all the database stuff.
 */
public class Connection {

	private static int websiteId;
	private static WebSite website;
	public Connection() {
		website = null;
	}
	
	public static List<Player> getRoster(int websiteId) {
		Connection.deriveSite(websiteId);
		
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
	
	public static Player getPlayerById(int playerId) {
		return new Select().from(Player.class).where("Player = ?", playerId).executeSingle();
	}
	
	public static String getDraftStr(int websiteId, Player player) {
		Connection.deriveSite(websiteId);
		
		DraftInfo d;
		if (website == null) {
			d = new Select().from(DraftInfo.class).where("Player = ?", player).executeSingle();
		} else {
			d = website.getDraftInfo(player.link);
			d.save();
		}
		
		return d.getDraftDisplay();
	}
	
	private static void deriveSite(int websiteId) {
		if (Connection.websiteId == websiteId) {
			return;
		}
		Connection.websiteId = websiteId;
		//TODO: add proper ids within an xml
		switch(websiteId) {
		case 1:
			website = new YahooSite();
			break;
		default:
			website = null;
		}		
	}
}
