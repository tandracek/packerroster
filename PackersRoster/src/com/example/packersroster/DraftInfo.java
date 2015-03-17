package com.example.packersroster;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="Draft")
public class DraftInfo extends Model {
	@Column(name="round")
	public String round;
	@Column(name="pick")
	public String pick;
	@Column(name="year")
	public String year;
	@Column(name="team")
	public String team;
	public boolean isUndrafted;
	
	public DraftInfo() {
		super();
	}
	
	public DraftInfo(String round, String pick, String team, String year) {
		this.round = round;
		this.pick = pick;
		this.team = team;
		this.year = year;
	}
	
	public DraftInfo(boolean undrafted) {
		this.isUndrafted = undrafted;
	}
}
