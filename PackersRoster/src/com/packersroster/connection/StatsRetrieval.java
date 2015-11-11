package com.packersroster.connection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.packersroster.player.NflStats;
import com.packersroster.player.Player;
import com.packersroster.player.Stats;
import com.packersroster.ui.SportStyles;
import com.activeandroid.Model;

public class StatsRetrieval extends WebSite {
	private HashMap<String, String[]> nflHtml;

	public StatsRetrieval(SportStyles sport) {
		super(sport);
		this.fillNflHtml();
	}

	public List<Stats> retrieveStats(Player player) throws InstantiationException, IllegalAccessException {
		this.connect(player.link);
		return this.fillYahooStats(player, this.nflHtml, NflStats.class);
	}

	private void fillNflHtml() {
		nflHtml = new HashMap<String, String[]>();
		String[] nflPassTitleArr = new String[] { 
				"Completions,completions", "Attempts,attempts",
				"Yards,passYards", "Touchdowns,pTouchdowns",
				"Interceptions,interceptions", "Fumbles,fumbles" };
		String[] nflRushTitleArr = new String[] {
				"Yards,rushYards", "Rushes,rushes", "Touchdowns,rushTouchdowns", "Fumbles,fumbles" };
		String[] nflRecTitleArr = new String[] {
				"Yards,recYards", "Targets,targets", "Touchdowns,recTouchdowns", "Receptions,receptions", 
				"Yards After Catch,yac", "Fumbles,fumbles" };
		String[] nflDefTitleArr = new String[] { 
				"Solo Tackles,tacklesSolo", "Tackle Assists,tacklesAssist", "Sacks,sacks", "Interceptions,dInterceptions",
				"Yards,intYards", "Interception Touchdowns,intTouchdowns", "Forced Fumbles,forcedFumbles",
				"Passes Defended,passesDefended"};
		nflHtml.put("Passing", nflPassTitleArr);
		nflHtml.put("Rusing", nflRushTitleArr);
		nflHtml.put("Receiving", nflRecTitleArr);
		nflHtml.put("Defense", nflDefTitleArr);
	}

	public <T extends Stats> List<Stats> fillYahooStats(Player player, HashMap<String, String[]> htmlFields, Class<T> statsClass) 
			throws InstantiationException, IllegalAccessException {
		Element main = doc.getElementById("mediasportsplayercareerstats");
		Elements containers = main.getElementsByClass("data-container");

		HashMap<String, Stats> statsHash = new HashMap<String, Stats>();
		for (int i = 0; i < containers.size(); i++) {
			Element table = containers.get(i).select("table").get(0);
			Elements rows = table.select("tbody tr");
			// This should pull the stat type (passing, rushing) from the hashmap based on the summary attr of the table
			String[] fields = htmlFields.get(table.attr("summary"));

			// Go through all the rows in the stat table
			if (fields != null) {
				for (int j = 0; j < rows.size() - 1; j++) {
					Element row = rows.get(j);
					String season = row.getElementsByClass("season").get(0).ownText();
					Stats statsObj;
					if(statsHash.containsKey(season)) {
						statsObj = statsHash.get(season);
					} else {
						statsObj = statsClass.newInstance();
						
						int seasonInt = Integer.parseInt(season);
						statsObj.season = seasonInt;
						String games = row.getElementsByAttributeValue("title", "Games").get(0).ownText();
						int gamesInt = Integer.parseInt(games);
						statsObj.games = gamesInt;
						statsObj.player = player;
					}
					for (String s : fields) {
						String[] item = s.split(",");
						String val = row
								.getElementsByAttributeValue("title", item[0])
								.get(0).ownText();
						try {
							Field f = statsClass.getField(item[1]);
							if (f.getType() == Integer.class) {
								Integer intVal = Integer.parseInt(val);
								f.set(statsObj, intVal);
							} else if (f.getType() == Short.class){
								Short shortVal = Short.valueOf(val);
								f.set(statsObj, shortVal);
							} else if (f.getType() == Double.class) {
								Double doubleVal = Double.valueOf(val);
								f.set(statsObj, doubleVal);
							} else {
								f.set(statsObj, val);
							}
						} catch (NoSuchFieldException e) {
							e.printStackTrace();
							Log.d("Stats", "error with field");
						}
					}
					if(!statsHash.containsKey(season)) {
						statsHash.put(season, statsObj);
					}
				}
			}
		}
		return new ArrayList<Stats>(statsHash.values());
	}
}
