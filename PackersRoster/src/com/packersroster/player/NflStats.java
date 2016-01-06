package com.packersroster.player;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/* Table model for stats for a player.
 * -Each row (instance of this class) will represent 1 year of a players stats
 * -Boolean can indicate if current row is last years stats, might be hard (and pointless) to figure out based on date
 */
public class NflStats extends  Stats {
	/* Offensive stats */
	@StatField(Label = "Passing Yards")
	@Column
	public Integer passYards;
	@StatField(Label = "Rush Yards")
	@Column
	public Integer rushYards;
	@StatField(Label = "Receiving Yards")
	@Column
	public Integer recYards;
	@StatField(Label = "Completions")
	@Column
	public Integer completions;
	@StatField(Label = "Attempts")
	@Column
	public Integer attempts;
	@StatField(Label = "Rushes")
	@Column
	public Integer rushes;
	@StatField(Label = "Receptions")
	@Column
	public Integer receptions;
	@StatField(Label = "Targets")
	@Column
	public Integer targets;
	@StatField(Label = "YAC")
	@Column
	public Double yac;
	@StatField(Label = "Interceptions")
	@Column
	public Integer interceptions;
	@StatField(Label = "Passing TD")
	@Column
	public Integer pTouchdowns;
	@StatField(Label = "Rushing TD")
	@Column
	public Integer rushTouchdowns;
	@StatField(Label = "Rec TD")
	@Column
	public Integer recTouchdowns;
	@StatField(Label = "Fumbles")
	@Column
	public Integer fumbles;
	
	/* Defensive stats */
	@StatField(Label = "Solo Tackles")
	@Column
	public Integer tacklesSolo;
	@StatField(Label = "Assist Tackles")
	@Column
	public Integer tacklesAssist;
	@StatField(Label = "Sacks")
	@Column
	public Double sacks;
	@StatField(Label = "Defensive Int")
	@Column
	public Integer dInterceptions;
	@StatField(Label = "Int Yards")
	@Column
	public Integer intYards;
	@StatField(Label = "Int TD")
	@Column
	public Integer intTouchdowns;
	@StatField(Label = "Forced Fumbles")
	@Column
	public Integer forcedFumbles;
	@StatField(Label = "Passes Defended")
	@Column
	public Integer passesDefended;
	
	public NflStats() {
		super();
	}

	@Override
	public List<StatItem> getValidFields() {
		return super.fillStatItems(this.getClass());
	}

}
