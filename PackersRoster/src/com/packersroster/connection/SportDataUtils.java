package com.packersroster.connection;

import java.util.ArrayList;
import java.util.List;

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
	
	public static DraftInfo getDraftInfo(boolean goOnline, Player player) {
		if(!goOnline) {
			return new Select(new String[]{"DraftInfo"}).from(Player.class).where("Player = ?", player.getId()).executeSingle();
		}

		PlayerInfoRetrieval pInfoRet = new PlayerInfoRetrieval();
		DraftInfo d = pInfoRet.getDraftInfo(player.link);
		player.draftInfo = d;
		player.save();

		return d;
	}
	
	// TODO: actually build this
	public static <T extends Stats> List<T> getStats(boolean goOnline, Class<? extends Model> table) {
		if (!goOnline) {
			return new Select().from(table).execute();
		}
		ArrayList<T> stats = null; //website.getSeasonStats("");
		
		ActiveAndroid.beginTransaction();
		try {
			for(T t: stats) {
				t.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
		
		return stats;
	}
	
	public static int deleteRoster(String sport) {
		From f = new Delete().from(Player.class).where("sport=?", sport);
		int numRows = f.count();
		f.execute();
		return numRows;
	}
}
