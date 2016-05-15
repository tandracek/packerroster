package com.packersroster.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.*;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.annotation.Column.ForeignKeyAction;

@Table(name="Player")
public class Player extends Model {
	public int id = 0;
	
	public String sortedValue;
	@Column(name="sport", uniqueGroups = {"playerGroup"}, onUniqueConflicts = {ConflictAction.FAIL})
	public String sport;
	@Column(name="name", uniqueGroups = {"playerGroup"}, onUniqueConflicts = {ConflictAction.FAIL})
	public String name;
	@Column(name="position")
	public String position;
	@Column(name="number", uniqueGroups = {"playerGroup"}, onUniqueConflicts = {ConflictAction.FAIL})
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
	@Column(name="height")
	public String height;
	@Column(name="weight")
	public String weight;
	@Column(name="bornDate")
	public String bornDate;
	@Column(name="bornPlace")
	public String bornPlace;
	@Column(name="college")
	public String college;
	@Column(name="group_field")
	public String group;
	@Column(name="DraftInfo", onDelete = ForeignKeyAction.CASCADE)
	public DraftInfo draftInfo;
	
	public List<Stats> stats;
	
	public Player() {
		super();
	}
	
	public Player(String name) {
		this.stats = new ArrayList<Stats>();
		this.name = name;
	}
	
	public Player(String name, int id) {
		this(name);
		this.id = id;
	}
	
	public Player(String name, String position, String number) {
		this(name);
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
	
	//Below method not used
	@SuppressWarnings("unchecked")
	public <T extends Stats> List<T> stats() {
		return (List<T>) getMany(NflStats.class, "Stats");
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Stats> ArrayList<T> testStats() {
		List<NflStats> stats = new ArrayList<NflStats>();
		NflStats nflStat = new NflStats();
		nflStat.season = 1;
		stats.add(nflStat);
		return (ArrayList<T>) stats;
	}
}
