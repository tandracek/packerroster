package com.example.packersroster;

import com.activeandroid.Model;
import com.activeandroid.annotation.*;

@Table(name="Player")
public class Player extends Model implements Comparable<Player>{
	public static String sortBy;
	public int id = 0;
	
	public String sortedValue;
	@Column(name="sport")
	public String sport;
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
	@Column(name="group_field")
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
	
	public int getNumAsInt() {
		int c_number;
		try {
			c_number = Integer.parseInt(number);
		} catch(NumberFormatException e) {
			return 0;
		}
		return c_number;
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
