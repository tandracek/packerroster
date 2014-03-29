package com.example.packersroster;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RosterHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 5;
	private static final String DATABASE_NAME = "main_db";
	private static String CREATE_TABLE = null;
	private static String CREATE_DETAILS_TBL = null;
	private String player_table = null;
	private final String details_table = "player_details";
	
	private String id_field = "id";
	private String name_field = "name";
	private String num_field = "number";
	private String pos_field = "position";
	private String link_field = "link";
	
	private String draft_field = "draft";
	private String salary_field = "salary";
	private String school_field = "school";
	private String exp_field = "experience";
	private String age_field = "age";
	
	private static int id;
	
	RosterHelper(Context context, String table_name) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		CREATE_TABLE = "CREATE TABLE " + table_name + 
				"( " + id_field + " INT PRIMARY KEY NOT NULL, " + name_field + " TEXT NOT NULL, "
						+ num_field + " CHAR(2) NOT NULL, " + 
				pos_field + " CHAR(2), " + link_field + " TEXT, " + school_field + " VARCHAR(50), " + 
						salary_field + " VARCHAR(20), " + exp_field + " VARCHAR(2), " + age_field + " VARCHAR(2) );";
		CREATE_DETAILS_TBL = "CREATE TABLE " + details_table + " (" + id_field + " INT PRIMARY KEY NOT NULL, " 
				+ draft_field + " VARCHAR(100) " + " );";
		player_table = table_name;
		RosterHelper.id = 1;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
		db.execSQL(CREATE_DETAILS_TBL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		String drop_player = "DROP TABLE IF EXISTS " + player_table;
		String drop_details = "DROP TABLE IF EXISTS " + details_table;
		db.execSQL(drop_player);
		db.execSQL(drop_details);
		onCreate(db);
	}
	
	public int deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		int rows = db.delete(player_table, "1", null);
		int row_det = db.delete(details_table, null, null);
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
		values.put(name_field, player.getName());
		values.put(num_field, player.getNumber());
		values.put(pos_field, player.getPosition());
		values.put(link_field, player.getLink());
		values.put(age_field, player.age);
		values.put(exp_field, player.experience);
		values.put(salary_field, player.salary);
		values.put(school_field, player.college);
		
		ContentValues details_values = new ContentValues();
		details_values.put(id_field, id);
		details_values.put(draft_field, "");
		
		long err = db.insert(player_table, null, values);
		
		if(err < 0) {
			return false;
		} 
		err = db.insert(details_table, null, details_values);
		db.close();
		if(err < 0) {
			return false;
		}
		RosterHelper.id++;
		return true;
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
		
		Cursor cursor = db.query(player_table, new String[] {name_field, num_field, pos_field, link_field, age_field, exp_field, salary_field, school_field},
				id_field + "=" + playerid, null, null, null, null);
		while(cursor.moveToNext()) {
			returnPlayer = new Player(cursor.getString(0), cursor.getString(2), cursor.getString(1));
			returnPlayer.id = playerid;
			returnPlayer.link = cursor.getString(3);
			returnPlayer.age = cursor.getString(4);
			returnPlayer.experience = cursor.getString(5);
			returnPlayer.salary = cursor.getString(6);
			returnPlayer.college = cursor.getString(7);
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
		
		Cursor cursor = db.query(player_table, new String[] {id_field, name_field, num_field, pos_field, link_field},
				null, null, null, null, null);
		
		while(cursor.moveToNext()) {
			Player temp_pl = new Player(cursor.getString(1), cursor.getString(3), cursor.getString(2));
			temp_pl.setLink(cursor.getString(4));
			temp_pl.id = cursor.getInt(0);
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
			temp_pl.setSortValue(cursor.getString(2));
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
