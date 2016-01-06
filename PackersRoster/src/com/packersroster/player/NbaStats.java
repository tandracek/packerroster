package com.packersroster.player;

import java.util.List;

import com.activeandroid.annotation.Column;

public class NbaStats extends Stats {
	@StatField(Label = "Field Goal Percentage")
	@Column
	public Double fieldGoalPercentage;
	@StatField(Label = "Field Goals Made-Attempted")
	@Column
	public String fieldGoalsMadeAttempted;
	@StatField(Label = "3 Point Made-Attempted")
	@Column
	public String threePointersMadeAttempted;
	@StatField(Label = "3 Point Percentage")
	@Column
	public Double threePointersPercentage;
	@StatField(Label = "Free Throws Made-Attempted")
	@Column
	public String freeThrowsMadeAttempted;
	@StatField(Label = "Free Throw Percentage")
	@Column
	public Double freeThrowPercentage;
	@StatField(Label = "Offensive Rebounds")
	@Column
	public Integer offRebounds;
	@StatField(Label = "Defensive Rebounds")
	@Column
	public Integer defRebounds;
	@StatField(Label = "Assists")
	@Column
	public Integer assists;
	@StatField(Label = "Blocks")
	@Column
	public Integer blocks;
	@StatField(Label = "Steals")
	@Column
	public Integer steals;
	@StatField(Label = "Personal Fouls")
	@Column
	public Integer persFouls;
	@StatField(Label = "Turnovers")
	@Column
	public Integer turnovers;
	@StatField(Label = "Total Points")
	@Column
	public Integer totalPoints;
	
	public NbaStats() {
		super();
	}
	
	@Override
	public List<StatItem> getValidFields() {
		return super.fillStatItems(this.getClass());
	}
}
