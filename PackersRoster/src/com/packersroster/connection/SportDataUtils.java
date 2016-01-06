package com.packersroster.connection;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.packersroster.player.DraftInfo;
import com.packersroster.player.Player;
import com.packersroster.player.Stats;
import com.packersroster.ui.SportStyles;

public class SportDataUtils {
	public static final String TAG = "SportDataUtils";
	public static WebSite currWebsite;

	// TODO: fix sql errors inserting same players because of injury section
	public static List<Player> getRoster(boolean goOnline, SportStyles sport) {
		if (!goOnline) { 
			return new Select().from(Player.class).where("sport=?", sport.sport).execute();
		}
		
		RosterRetrieval rostRet = new RosterRetrieval(sport);
		List<Player> roster = rostRet.getRoster();
		ActiveAndroid.beginTransaction();
		try {
			for(Player p : roster) {
				p.sport = sport.sport;
				p.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}

		return roster;
	}
	
	@SuppressWarnings("unchecked")
	public static Player getPlayerById(Long playerId) {
		return Model.load(Player.class, playerId);
	}
	
	public static List<Player> filterPlayers(String sport, String filter) {
		List<Player> players;
		if(filter.equals("All")) {
			players = new Select().from(Player.class).where("sport=?", sport).execute();
		} else {
			players = new Select().from(Player.class).where("sport=?", sport).and("group_field=?", filter).execute();
		}
		return players;
	}
	
	public static List<String> getPositions(String sport) {		
		List<String> pos = new ArrayList<String>();
		pos.add("All");
		
		List<Player> players = new Select(new String[]{"group_field"}).distinct().from(Player.class).where("sport=?", sport).execute();
		for(Player p : players) {
			pos.add(p.group);
		}
		return pos;
	}
	
	public static Player getDetailsAndStats(boolean goOnline, Player player) {
		player = SportDataUtils.getDetails(goOnline, player, null);
		player.stats = SportDataUtils.getStats(goOnline, player, currWebsite);
		currWebsite = null;
		return player;
	}
	
	public static Player getDetails(boolean goOnline, Player player, WebSite website) {
		if(!goOnline) {
			return new Select().from(Player.class).where("Player = ?", player.getId()).executeSingle();
		}
		PlayerInfoRetrieval pInfoRet = new PlayerInfoRetrieval();
		if (website != null) {
			player = pInfoRet.getDetails(player, player.link, website.doc);
		} else {
			player = pInfoRet.getDetails(player, player.link, null);
		}
		
		player.save();
		currWebsite = pInfoRet;
		return player;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Stats> getStats(boolean goOnline, Player player, WebSite website) {
		SportStyles sport = SportStyles.sportFromString(player.sport);
		Class<? extends Model> statClass = (Class<? extends Model>)sport.statClass;
		if (statClass == null) {
			Log.e(TAG, "Stat class is null");
			return null;
		}
		
		if (!goOnline) {
			return new Select().from(statClass).where("Player = ?", player.getId()).execute();
		}
		SportDataUtils.deleteStats(player);
		StatsRetrieval statsUtil = new StatsRetrieval(sport);
		List<Stats> stats = null;
		try {
			if (website != null) {
				stats = statsUtil.retrieveStats(player, website.doc);
			} else {
				stats = statsUtil.retrieveStats(player, null);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		
		ActiveAndroid.beginTransaction();
		try {
			for(Stats stat: stats) {
				stat.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
		currWebsite = statsUtil;
		return stats;
	}
	
	@SuppressWarnings("unchecked")
	public static int deleteStats(Player player) {
		SportStyles sport = SportStyles.sportFromString(player.sport);
		if(sport == null) return 0;
		
		Class<? extends Model> statModel = (Class<? extends Model>)sport.statClass;
		From f = new Delete().from(statModel).where("Player=?", player);
		int numRows = f.count();
		f.execute();
		return numRows;
	}
	
	// TODO: can probably delete this method, not sure why i would delete the player
	public static int deleteDetails(Player player) {
		From f = new Delete().from(Player.class).where("Player=?", player.getId());
		int numRows = f.count();
		f.execute();
		return numRows;
	}
	
	public static int deleteRoster(String sport) {
		From f = new Delete().from(Player.class).where("sport=?", sport);
		int numRows = f.count();
		f.execute();
		return numRows;
	}
}
