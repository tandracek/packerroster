package com.example.packersroster;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RosterHelper extends SQLiteOpenHelper{
	
	/* Todo:
	 * 
	 * Build out a database class, and table class.  Those map the tables and fields.
	 * Build out methods on those classes that this class can use to insert/update tables.
	 * Build base table class, then the individual tables will extend that (pass, rush, etc)
	 *  -the base table class will have an insert() and array of fields and their values that gets
	 *   set by the extended class, the insert will use that array and build out the content values
	 *   that can be passed back to the database class
	 *  
	 */

	private static final int DATABASE_VERSION = 7;
	private static final String DATABASE_NAME = "main_db";
	private static String CREATE_TABLE = null;
	private static String CREATE_DETAILS_TBL = null;
	private static String CREATE_PASS_STATS_TBL = null;
	private static String CREATE_RUSH_STATS_TBL = null;
	private static String CREATE_REC_STATS_TBL = null;
	private static String CREATE_DEF_STATS_TBL = null;
	private String player_table = null;
	private final String details_table = "player_details";
	private final String pass_stats = "pass_stats_table";
	private final String rush_stats = "rush_stats_table";
	private final String rec_stats = "rec_stats_table";
	private final String def_stats = "def_stats_table";
	
	private String id_field = "id";
	private String name_field = "name";
	private String num_field = "number";
	private String pos_field = "position";
	private String link_field = "link";
	private String group_field = "group_pos";
	private String year_field = "year";
	
	private String draft_field = "draft";
	private String salary_field = "salary";
	private String school_field = "school";
	private String exp_field = "experience";
	private String age_field = "age";
	
	private String yards_field = "yards";
	private String fumble_field = "fumbles";
	private String td_field = "td";
	private String games_field = "games";
	private String long_field = "long";
	
	//QB stats
	private String comp_pct_field = "comp_pct";
	private String int_field = "inter";
	
	//Rush stats
	private String rushes_field = "rush_atmpts";
	
	//Receiving stats
	private String recepts_field = "receptions";
	private String target_field = "targets";
	private String yac_field = "yac";
	
	//Defense stats
	private String sack_field = "sacks";
	private String tackle_field = "tackles";
	
	private static int id;
	
	/* 6/15 Todo:
	 *  Build separate tables for each major stat group (pass, rush, def, etc)
	 *  Tables are child of player table, can pull based on id of player.
	 */
	
	RosterHelper(Context context, String table_name) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		CREATE_TABLE = "CREATE TABLE " + table_name + 
				"( " + id_field + " INT PRIMARY KEY NOT NULL, " + name_field + " TEXT NOT NULL, "
						+ num_field + " CHAR(2) NOT NULL, " + 
				pos_field + " CHAR(2), " + link_field + " TEXT, " + school_field + " VARCHAR(50), " + 
						salary_field + " VARCHAR(20), " + exp_field + " VARCHAR(2), " + age_field + " VARCHAR(2), " +
				group_field + " CHAR(3) );";
		CREATE_DETAILS_TBL = "CREATE TABLE " + details_table + " (" + id_field + " INT PRIMARY KEY NOT NULL, " 
				+ draft_field + " VARCHAR(100), " + games_field + " VARCHAR(2), " + fumble_field + " VARCHAR(3) );";
		
		CREATE_PASS_STATS_TBL = "CREATE TABLE " + pass_stats + " (" + id_field + " INT PRIMARY KEY NOT NULL, "
				+ comp_pct_field + " VARCHAR(8), " + td_field + " VARCHAR(4), " + int_field + " VARCHAR(3), "
				+ yards_field + " VARCHAR(8), " + long_field + " VARCHAR(3) " + year_field + " VARCHAR(4) );";
		
		CREATE_RUSH_STATS_TBL = "CREATE TABLE " + rush_stats + " (" + id_field + " INT PRIMARY KEY NOT NULL, "
				+ rushes_field + " VARCHAR(8), " + td_field + " VARCHAR(4), " + fumble_field + " VARCHAR(3), "
				+ yards_field + " VARCHAR(8), " + long_field + " VARCHAR(3) " + year_field + " VARCHAR(4) );";
		
		CREATE_REC_STATS_TBL = "CREATE TABLE " + rec_stats + " (" + id_field + " INT PRIMARY KEY NOT NULL, "
				+ recepts_field + " VARCHAR(8), " + td_field + " VARCHAR(4), " + target_field + " VARCHAR(8), "
				+ yards_field + " VARCHAR(8), " + long_field + " VARCHAR(3) " + yac_field + " VARCHAR(8), "
				+ year_field + " VARCHAR(4) );";
		
		CREATE_DEF_STATS_TBL = "CREATE TABLE " + def_stats + " (" + id_field + " INT PRIMARY KEY NOT NULL, "
				+ tackle_field + " VARCHAR(8), " + sack_field + " VARCHAR(4), " + int_field + " VARCHAR(3), "
				+ year_field + " VARCHAR(4) );";
		
		player_table = table_name;
		RosterHelper.id = 1;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
		db.execSQL(CREATE_DETAILS_TBL);
		db.execSQL(CREATE_PASS_STATS_TBL);
		db.execSQL(CREATE_RUSH_STATS_TBL);
		db.execSQL(CREATE_REC_STATS_TBL);
		db.execSQL(CREATE_DEF_STATS_TBL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		String drop_player = "DROP TABLE IF EXISTS " + player_table;
		String drop_details = "DROP TABLE IF EXISTS " + details_table;
		String drop_pass_stats = "DROP TABLE IF EXISTS " + pass_stats;
		String drop_rush_stats = "DROP TABLE IF EXISTS " + rush_stats;
		String drop_rec_stats = "DROP TABLE IF EXISTS " + rec_stats;
		String drop_def_stats = "DROP TABLE IF EXISTS " + def_stats;
		
		db.execSQL(drop_player);
		db.execSQL(drop_details);
		db.execSQL(drop_pass_stats);
		db.execSQL(drop_rush_stats);
		db.execSQL(drop_rec_stats);
		db.execSQL(drop_def_stats);
		onCreate(db);
	}
	
	public int deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		int rows = db.delete(player_table, "1", null);
		int row_det = db.delete(details_table, null, null);
		int row_pass = db.delete(pass_stats, null, null);
		int row_rush = db.delete(rush_stats, null, null);
		int row_rec = db.delete(rec_stats, null, null);
		int row_def = db.delete(def_stats, null, null);
		RosterHelper.id = 1;
		
		return rows;
	}
	
	public boolean addPlayerDetails(Player player) {
		if(player.id == 0) return false;
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(id_field, player.id);
		values.put(draft_field, player.draftStr);
		values.put(salary_field, player.salary);
		values.put(school_field, player.college);
		
		long err = db.insert(details_table, null, values);
		db.close();
		if(err < 0) {
			return false;
		} else {
			RosterHelper.id++;
			return true;
		}
	}
	
	public boolean addValue(String field, String value, int p_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(field, value);
		int err = db.update(details_table, values, id_field + "=" + p_id, null);
		
		if(err < 0) return false;
		
		return true;
	}
	
	public boolean addPlayer(Player player) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(id_field, id);
		values.put(name_field, player.name);
		values.put(num_field, player.number);
		values.put(pos_field, player.position);
		values.put(link_field, player.link);
		values.put(age_field, player.age);
		values.put(exp_field, player.experience);
		values.put(salary_field, player.salary);
		values.put(school_field, player.college);
		values.put(group_field, player.group);
		
		ContentValues details_values = new ContentValues();
		details_values.put(id_field, id);
		details_values.put(draft_field, "");
		
		ContentValues id_values = new ContentValues();
		id_values.put(id_field, id);
		
		if(!this.insertContentValues(player_table, values, db)) return false;;
		
		if(!this.insertContentValues(details_table, id_values, db)) return false;
		if(!this.insertContentValues(pass_stats, id_values, db)) return false;
		if(!this.insertContentValues(rush_stats, id_values, db)) return false;
		if(!this.insertContentValues(rec_stats, id_values, db)) return false;
		if(!this.insertContentValues(def_stats, id_values, db)) return false; 
		
		RosterHelper.id++;
		return true;
	}
	
	private boolean insertContentValues(String table, ContentValues id_values, SQLiteDatabase db) {
		long err;
		err = db.insert(table, null, id_values);
		
		if(err < 0) {
			db.close();
			return false;
		} else return true;
	}
	
	public boolean addRoster(ArrayList<Player> player_list) {
		int rowsDeleted = this.deleteAll();
		for(int i = 0; i < player_list.size(); i++) {
			if (!this.addPlayer(player_list.get(i))) return false;
		}
		return true;
	}
	
	public Player getPlayer(String name) {
		Player returnPlayer = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(player_table, new String[] {name_field, num_field, pos_field, link_field, age_field, exp_field, salary_field, school_field},
				name_field + "=?", new String[] { name }, null, null, null);
		while(cursor.moveToNext()) {
			returnPlayer = new Player(cursor.getString(0), cursor.getString(2), cursor.getString(1));
			returnPlayer.link = cursor.getString(3);
			returnPlayer.age = cursor.getString(4);
			returnPlayer.experience = cursor.getString(5);
			returnPlayer.salary = cursor.getString(6);
			returnPlayer.college = cursor.getString(7);
		}
		cursor.close();
		return returnPlayer;
	}
	
	public Player getPlayer(int playerid) {
		Player returnPlayer = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(player_table, new String[] {name_field, num_field, pos_field, link_field, age_field, exp_field, salary_field, school_field, group_field},
				id_field + "=" + playerid, null, null, null, null);
		while(cursor.moveToNext()) {
			returnPlayer = new Player(cursor.getString(0), cursor.getString(2), cursor.getString(1));
			returnPlayer.id = playerid;
			returnPlayer.link = cursor.getString(3);
			returnPlayer.age = cursor.getString(4);
			returnPlayer.experience = cursor.getString(5);
			returnPlayer.salary = cursor.getString(6);
			returnPlayer.college = cursor.getString(7);
			returnPlayer.group = cursor.getString(8);
		}
		
		cursor = db.query(details_table, new String[] {draft_field },
				id_field + "=" + playerid, null, null, null, null);
		
		while(cursor.moveToNext()) {
			returnPlayer.draftStr = cursor.getString(0);
		}
		cursor.close();
		return returnPlayer;
	}
	
	public ArrayList<Player> getPlayers() {
		ArrayList<Player> player_list = new ArrayList<Player>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(player_table, new String[] {id_field, name_field, num_field, pos_field, link_field, group_field},
				null, null, null, null, null);
		
		while(cursor.moveToNext()) {
			Player temp_pl = new Player(cursor.getString(1), cursor.getString(3), cursor.getString(2));
			temp_pl.id = cursor.getInt(0);
			temp_pl.link = cursor.getString(4);
			temp_pl.group = cursor.getString(5);
			player_list.add(temp_pl);
		}
		cursor.close();
		
		return player_list;
	}
	
	public String getValue(String field, String table, int p_id) {
		String returnStr = "";
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(table, new String[] {field},
				id_field + "=" + p_id, null, null, null, null);
		while(cursor.moveToNext()) {
			returnStr = cursor.getString(0);
		}
		return returnStr;
	}
	
	public ArrayList<Player> getSorted(String value, String filter) {
		ArrayList<Player> player_list = new ArrayList<Player>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(player_table, new String[] {id_field, name_field, value}, null, null, null, null, value + " " + filter);
		while(cursor.moveToNext()) {
			Player temp_pl = new Player(cursor.getString(0), Integer.parseInt(cursor.getString(1)));
			temp_pl.sortedValue = cursor.getString(2);
			player_list.add(temp_pl);
		}
		
		return player_list;
	}
	
	public Cursor getRosterCursor() {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(player_table, new String[] {name_field, num_field, pos_field, link_field},
				null, null, null, null, null);
		
		return cursor;
	}
}
