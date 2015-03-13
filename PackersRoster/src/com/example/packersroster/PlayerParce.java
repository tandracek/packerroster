package com.example.packersroster;

import android.os.Parcel;
import android.os.Parcelable;

/* TODO: Look more into this parcel thing for potentially sending through activities */
public class PlayerParce implements Parcelable {
	private String name;
	private String position;
	private String number;
	private String link;
	private DraftInfo draftInfo;
	private String draftStr;

	public PlayerParce(String name, String position, String number) {
		this.name = name;
		this.position = position;
		this.number = number;
	}

	public void setDraftStr(String draftStr) {
		this.draftStr = draftStr;
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

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}

	public static final Parcelable.Creator<PlayerParce> CREATOR = new Parcelable.Creator<PlayerParce>() {
		public PlayerParce createFromParcel(Parcel in) {
			return new PlayerParce(in);
		}

		public PlayerParce[] newArray(int size) {
			return new PlayerParce[size];
		}
	};

	private PlayerParce(Parcel in) {
		name = in.readString();
	}

}
