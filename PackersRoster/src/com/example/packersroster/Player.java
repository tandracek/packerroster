package com.example.packersroster;

import com.activeandroid.Model;
import com.activeandroid.annotation.*;

@Table(name="Player")
public class Player extends Model implements Comparable<Player>{
	public static String sortBy;
	public int id = 0;
	
	public String sortedValue;
	@Column(name="name")
	public String name;
	@Column(name="position")
	public String position;
	@Column(name="number")
	public String number;
	@Column(name="link")
	public String link;
	@Column(name="draftStr")
	public String draftStr;
	@Column(name="salary")
	public String salary;
	@Column(name="experience")
	public String experience;
	@Column(name="age")
	public String age;
	@Column(name="ht_wt")
	public String ht_wt;
	@Column(name="college")
	public String college;
	@Column(name="group")
	public String group;
	@Column(name="DraftInfo")
	public DraftInfo draftInfo;
	
	public Player() {
		super();
	}
	
	public Player(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public Player(String name, String position, String number) {
		this.name = name;
		this.position = position;
		this.number = number;
	}
	
	public String getDraftDisplay() {
		if(this.draftStr.length() > 0) this.draftInfo = new DraftInfo(draftStr);
		else return draftStr;
		
		return draftInfo.getDraftDisplay();
	}
	
	public void initDraftInfo() {
		if(draftStr.length() > 0) this.draftInfo = new DraftInfo(this.draftStr);
	}
	
	public void setDraftInfo(String draft) {
		this.draftInfo = new DraftInfo(draft);
	}
	
	public String getDraftYear() {
		return draftInfo.year;
	}
	
	public String getDraftPick() {
		return draftInfo.pick;
	}
	
	public String getDraftTeam() {
		return draftInfo.team;
	}
	
	public String getDraftRound() {
		return draftInfo.round;
	}
	
	public int getNumAsInt() {
		int c_number;
		try {
			c_number = Integer.parseInt(number);
		} catch(NumberFormatException e) {
			return 0;
		}
		return c_number;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public int compareTo(Player arg0) {
		if(Player.sortBy == null) Player.sortBy = "name";
		
		if(sortBy.equals("number")) {
			int comp = arg0.getNumAsInt();
			int curr = this.getNumAsInt();
			if(comp > curr) return -1;
			if(comp < curr) return 1;
			else return 0;
		} else {
			return this.name.compareTo(arg0.name);
		}
	}
}
