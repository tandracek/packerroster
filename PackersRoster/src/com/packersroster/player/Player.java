package com.packersroster.player;

import java.util.Date;

import com.activeandroid.Model;
import com.activeandroid.annotation.*;

@Table(name="Player")
public class Player extends Model {
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
}
