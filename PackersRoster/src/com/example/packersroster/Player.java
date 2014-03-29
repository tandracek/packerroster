package com.example.packersroster;

public class Player {
	public int id = 0;
	
	public String sortedValue;
	public String name;
	public String position;
	public String number;
	public String link;
	public String draftStr;
	public String salary;
	public String experience;
	public String age;
	public String ht_wt;
	public String college;
	
	public DraftInfo draftInfo;
	
	public Player(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public Player(String name, String position, String number) {
		this.name = name;
		this.position = position;
		this.number = number;
	}
	
	public void setSortValue(String value) {
		this.sortedValue = value;
	}
	
	public String getSortedValue() {
		return sortedValue;
	}
	
	public void setDraftStr(String draftStr) {
		this.draftStr = draftStr;
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
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPosition() {
		return position;
	}
	
	public String getNumber() {
		return number;
	}
	
	public int getNumAsInt() {
		return Integer.parseInt(number);
	}

	public void setPosition(String position) {
		this.position = position;
	}
}
