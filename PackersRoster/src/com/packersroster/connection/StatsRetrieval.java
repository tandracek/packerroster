package com.packersroster.connection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;
import android.util.SparseArray;

import com.packersroster.player.MlbStats;
import com.packersroster.player.NbaStats;
import com.packersroster.player.NflStats;
import com.packersroster.player.Player;
import com.packersroster.player.Stats;
import com.packersroster.ui.SportStyles;
import com.activeandroid.Model;

public class StatsRetrieval extends WebSite {
	private HashMap<String, String[]> nflYahooHtml;
	private SparseArray<String[]> nbaEspnHtml;
	private HashMap<String, String[]> mlbYahooHtml;
	private HashMap<String, Stats> statsHash;

	public StatsRetrieval(SportStyles sport) {
		super(sport);
		this.fillHtmlData();
	}

	public List<Stats> retrieveStats(Player player, Document doc)
			throws InstantiationException, IllegalAccessException {
		statsHash = new HashMap<String, Stats>();
		if (doc == null) {
			this.connect(player.link);
		} else {
			this.doc = doc;
		}
		switch(this.sport) {
		case NFL:
			this.fillYahooStats(player, this.nflYahooHtml, NflStats.class);
			break;
		case MLB:
			this.fillYahooStats(player, this.mlbYahooHtml, MlbStats.class);
			break;
		case NBA:
			this.fillEspnStats(player, this.nbaEspnHtml, NbaStats.class);
			break;
		}

		return new ArrayList<Stats>(statsHash.values());
	}
	
	private void fillHtmlData() {
		nbaEspnHtml = new SparseArray<String[]>(1);
		String[] table2 = new String[] { "fieldGoalPercentage,3",
				"fieldGoalsMadeAttempted,2", "threePointersMadeAttempted,4",
				"threePointersPercentage,5", "freeThrowsMadeAttempted,6",
				"freeThrowPercentage,7", "offRebounds,8", "defRebounds,9",
				"assists,11", "blocks,12", "steals,13", "persFouls,14",
				"turnovers,15", "totalPoints,16" };
		nbaEspnHtml.put(1, table2);
		
		nflYahooHtml = new HashMap<String, String[]>();
		String[] nflPassTitleArr = new String[] { "Completions,completions",
				"Attempts,attempts", "Yards,passYards",
				"Touchdowns,pTouchdowns", "Interceptions,interceptions",
				"Fumbles,fumbles" };
		String[] nflRushTitleArr = new String[] { "Yards,rushYards",
				"Rushes,rushes", "Touchdowns,rushTouchdowns", "Fumbles,fumbles" };
		String[] nflRecTitleArr = new String[] { "Yards,recYards",
				"Targets,targets", "Touchdowns,recTouchdowns",
				"Receptions,receptions", "Yards After Catch,yac",
				"Fumbles,fumbles" };
		String[] nflDefTitleArr = new String[] { "Solo Tackles,tacklesSolo",
				"Tackle Assists,tacklesAssist", "Sacks,sacks",
				"Interceptions,dInterceptions", "Yards,intYards",
				"Interception Touchdowns,intTouchdowns",
				"Forced Fumbles,forcedFumbles",
				"Passes Defended,passesDefended" };
		nflYahooHtml.put("Passing", nflPassTitleArr);
		nflYahooHtml.put("Rusing", nflRushTitleArr);
		nflYahooHtml.put("Receiving", nflRecTitleArr);
		nflYahooHtml.put("Defense", nflDefTitleArr);
		
		mlbYahooHtml = new HashMap<String, String[]>();
		String[] mlbPitchingArr = new String[] { "Wins,wins", "Losses,losses", "Saves,saves", "Holds,holds", 
				"Complete Games,completeGames", "Innings Pitched,inningsPitched", "Hits,hitsPitched","Runs,runsPitched",
				"Home Runs,homeRunsPitched", "Bases on Balls,basesOnBalls","Strikeouts,strikeoutsPitched",
				"Earned Run Average,era", "Walks plus Hits per Inning Pitched,whip","Batting Average Against,battingAvgAgainst"
		};
		String[] mlbBattingArr = new String[] { "At Bats,atBats", "Runs,runsBatted", "Hits,hitsBatted", "Doubles,doublesBatted", 
				"Triples,triplesBatted", "Home Runs,homeRunsBatted", "Runs Batted In,rbis","Bases on Balls,bassesOnBallsBatted",
				"Strikeouts,strikeoutsBatted", "Stolen Bases,stolenBases","Batting Average,battingAvg",
				"On Base %,obp", "Slugging %,slugging","On Base Plus Slugging,ops"
		};
		String[] mlbFieldingArr = new String[] { "Putouts,putouts", "Total Chances,totalChances", "Assists,assists", "Errors,errors", 
				"Double Plays,doublePlays", "Fielding %,fieldingPerct"
		};
		mlbYahooHtml.put("Pitching", mlbPitchingArr);
		mlbYahooHtml.put("Batting", mlbBattingArr);
		mlbYahooHtml.put("Fielding", mlbFieldingArr);
	}

	public <T extends Stats> void fillEspnStats(Player player,
			SparseArray<String[]> htmlFields, Class<T> statsClass)
			throws InstantiationException, IllegalAccessException {
		Element table = null;
		Elements tables = doc.getElementsByClass("mod-table");
		for (int i = 0; i < tables.size(); i++) {
			table = tables.get(i);
			if (table.hasClass("mod-player-stats")) {
				break;
			}
		}
		if (table == null)
			return;

		Elements contentTables = table.getElementsByClass("mod-content");
		if (contentTables.size() == 0)
			return;

		Element avgContentTable = contentTables.get(0);
		Elements avgStatRows = avgContentTable.getElementsByTag("tr");
		
		boolean dual = false;
		for (int j = 0; j < htmlFields.size(); j++) {
			int key = htmlFields.keyAt(j);
			String[] fields = htmlFields.get(key);
			Element contentTable = contentTables.get(key);
			Elements statRows = contentTable.getElementsByTag("tr");
			for (int i = 2; i < statRows.size() - 1; i++) {
				Element row = statRows.get(i);
				Elements statCells = row.getElementsByTag("td");
				if (statCells.size() >= fields.length) {
					String season = statCells.get(0).ownText();
					Stats statsObj;
					if (statsHash.containsKey(season)) {
						statsObj = statsHash.get(season);
					} else {
						statsObj = statsClass.newInstance();
						if (!dual) {
							dual = season.contains("-");
						}
						if (dual) {
							String[] spl1 = season.split("-");
							season = "20" + spl1[0].substring(1);
						}
						int seasonInt = Integer.parseInt(season);
						statsObj.season = seasonInt;
						String games = avgStatRows.get(i)
								.getElementsByTag("td").get(2).ownText();
						int gamesInt = Integer.parseInt(games);
						statsObj.games = gamesInt;
						statsObj.player = player;
					}
					for (String s : fields) {
						String[] item = s.split(",");
						int cellIndex = Integer.parseInt(item[1]);
						if (cellIndex >= 0) {
							String val = statCells
									.get(cellIndex).ownText();
							statsObj = setStatsField(item[0], val, statsObj);
						}
					}
					if (!statsHash.containsKey(season)) {
						statsHash.put(season, statsObj);
					}
				}
			}
		}

	}

	public <T extends Stats> void fillYahooStats(Player player,
			HashMap<String, String[]> htmlFields, Class<T> statsClass)
			throws InstantiationException, IllegalAccessException {
		Element main = doc.getElementById("mediasportsplayercareerstats");
		Elements containers = main.getElementsByClass("data-container");

		for (int i = 0; i < containers.size(); i++) {
			Element table = containers.get(i).select("table").get(0);
			Elements rows = table.select("tbody tr");
			// This should pull the stat type (passing, rushing) from the
			// hashmap based on the summary attr of the table
			String[] fields = htmlFields.get(table.attr("summary"));

			// Go through all the rows in the stat table
			if (fields != null) {
				for (int j = 0; j < rows.size() - 1; j++) {
					Element row = rows.get(j);
					String season = row.getElementsByClass("season").get(0)
							.ownText();
					Stats statsObj;
					if (statsHash.containsKey(season)) {
						statsObj = statsHash.get(season);
					} else {
						statsObj = statsClass.newInstance();

						int seasonInt = Integer.parseInt(season);
						statsObj.season = seasonInt;
						String games = row
								.getElementsByAttributeValue("title", "Games")
								.get(0).ownText();
						int gamesInt = Integer.parseInt(games);
						statsObj.games = gamesInt;
						statsObj.player = player;
					}
					for (String s : fields) {
						String[] item = s.split(",");
						String val = row
								.getElementsByAttributeValue("title", item[0])
								.get(0).ownText();
						statsObj = setStatsField(item[1], val, statsObj);
					}
					if (!statsHash.containsKey(season)) {
						statsHash.put(season, statsObj);
					}
				}
			}
		}
	}

	private Stats setStatsField(String field, String val, Stats stats) {
		try {
			Field f = stats.getClass().getField(field);
			if (f.getType() == Integer.class) {
				Integer intVal = Integer.parseInt(val);
				f.set(stats, intVal);
			} else if (f.getType() == Short.class) {
				Short shortVal = Short.valueOf(val);
				f.set(stats, shortVal);
			} else if (f.getType() == Double.class) {
				Double doubleVal = Double.valueOf(val);
				f.set(stats, doubleVal);
			} else {
				f.set(stats, val);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("Stats", "error with field");
		}
		return stats;
	}
}
