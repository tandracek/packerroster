package com.example.packersroster;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DataHandler {
	private static RosterHelper db_connection = null;
	private ArrayList<Player> player_list = null;
	private Context context;
	
	DataHandler(Context context) {
		player_list = new ArrayList<Player>();
		this.context = context;
	}
	
	DataHandler(Context context, ArrayList<Player> player_list) {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");
		
		boolean err = db_connection.addRoster(player_list);
		if(!err) Log.v("Data handler", "Error inserting roster");
		this.player_list = player_list;
		this.context = context;
	}
	
	public boolean addRoster(ArrayList<Player> roster) {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");
		
		boolean err = db_connection.addRoster(roster);
		return err;
	}
	
	public boolean addPlayer(Player player) {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");

		boolean err = db_connection.addPlayer(player);
		return err;
	}
	
	public boolean addValue(String field, String value, int id) {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");
		
		boolean err = db_connection.addValue(field, value, id);
		return err;
	}
	
	public String getValue(String field, String table, int id) {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");
		
		String returnStr = db_connection.getValue(field, table, id);
		
		return returnStr;
	}
	
	public int deleteRoster() {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");

		int rows = db_connection.deleteAll();
		return rows;
	}
	
	public ArrayList<Player> getRoster() {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");

		player_list = db_connection.getPlayers();
		return player_list;
	}
	
	public ArrayList<Player> getSortedRoster(String value, String filter) {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");

		player_list = db_connection.getSorted(value, filter);
		return player_list;
	}
	
	public Cursor getRosterCursor() {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");
		
		return db_connection.getRosterCursor();
	}
	
	public Player getPlayer(String name) {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");
		
		return db_connection.getPlayer(name);
	}
	
	public Player getPlayer(int id) {
		if(db_connection == null) db_connection = new RosterHelper(context, "roster_basic");
		
		return db_connection.getPlayer(id);
	}
	
}
