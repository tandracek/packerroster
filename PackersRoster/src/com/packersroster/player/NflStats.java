package com.packersroster.player;

import com.activeandroid.Model;

/* Table model for stats for a player.
 * -Each row (instance of this class) will represent 1 year of a players stats
 * -Boolean can indicate if current row is last years stats, might be hard (and pointless) to figure out based on date
 */
public class NflStats extends Stats {

	public int season;
	public int games;
	
	/* Offensive stats */
	public int passYards;
	public int rushYards;
	public int recYards;
	
	/* Defensive stats */
	
	public NflStats() {
		//super();
	}
}
