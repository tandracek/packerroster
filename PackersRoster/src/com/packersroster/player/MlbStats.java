package com.packersroster.player;

import java.util.List;

import com.activeandroid.annotation.Column;
import com.packersroster.player.Stats.StatField;

public class MlbStats extends Stats {
	//Pitching
	@StatField(Label = "Wins")
	@Column
	public Integer wins;
	@StatField(Label = "Losses")
	@Column
	public Integer losses;
	@StatField(Label = "Saves")
	@Column
	public Integer saves;
	@StatField(Label = "Holds")
	@Column
	public Integer holds;
	@StatField(Label = "Complete Games")
	@Column
	public Integer completeGames;
	@StatField(Label = "Innings Pitched")
	@Column
	public Double inningsPitched;
	@StatField(Label = "Pitched Hits")
	@Column
	public Integer hitsPitched;
	@StatField(Label = "Pitched Runs")
	@Column
	public Integer runsPitched;
	@StatField(Label = "Pitched Home Runs")
	@Column
	public Integer homeRunsPitched;
	@StatField(Label = "Pitced Basses on Balls")
	@Column
	public Integer basesOnBalls;
	@StatField(Label = "Pitched Strikeouts")
	@Column
	public Integer strikeoutsPitched;
	@StatField(Label = "ERA")
	@Column
	public Double era;
	@StatField(Label = "WHIP")
	@Column
	public Double whip;
	@StatField(Label = "Batting Average Against")
	@Column
	public Double battingAvgAgainst;
	
	//Batting
	@StatField(Label = "At Bats")
	@Column
	public Integer atBats;
	@StatField(Label = "Runs")
	@Column
	public Double runsBatted;
	@StatField(Label = "Hits")
	@Column
	public Double hitsBatted;
	@StatField(Label = "Doubles")
	@Column
	public Double doublesBatted;
	@StatField(Label = "Triples")
	@Column
	public Double triplesBatted;
	@StatField(Label = "Home Runs")
	@Column
	public Double homeRunsBatted;
	@StatField(Label = "RBIs")
	@Column
	public Double rbis;
	@StatField(Label = "Bases on Balls")
	@Column
	public Double bassesOnBallsBatted;
	@StatField(Label = "Strikeouts")
	@Column
	public Double strikeoutsBatted;
	@StatField(Label = "Stolen Bases")
	@Column
	public Double stolenBases;
	@StatField(Label = "Batting Average")
	@Column
	public Double battingAvg;
	@StatField(Label = "OBP")
	@Column
	public Double obp;
	@StatField(Label = "Slugging")
	@Column
	public Double slugging;
	@StatField(Label = "OPS")
	@Column
	public Double ops;
	
	//Fielding
	@StatField(Label = "Putouts")
	@Column
	public Double putouts;
	@StatField(Label = "Total Chances")
	@Column
	public Double totalChances;
	@StatField(Label = "Assists")
	@Column
	public Double assists;
	@StatField(Label = "Errors")
	@Column
	public Double errors;
	@StatField(Label = "Double Plays")
	@Column
	public Double doublePlays;
	@StatField(Label = "Fielding %")
	@Column
	public Double fieldingPerct;
	
	public MlbStats() {
		super();
	}

	@Override
	public List<StatItem> getValidFields() {
		return super.fillStatItems(this.getClass());
	}

}
