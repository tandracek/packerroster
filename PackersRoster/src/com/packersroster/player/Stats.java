package com.packersroster.player;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.annotation.Column.ForeignKeyAction;

//TODO: need to consider multiple positions in 1 season, add position to this and as a key
public abstract class Stats extends Model {
	@Column(name="Player", uniqueGroups = {"statsGroup"}, onUniqueConflicts = {ConflictAction.FAIL}, onDelete = ForeignKeyAction.CASCADE)
	public Player player;
	@Column(uniqueGroups = {"statsGroup"}, onUniqueConflicts = {ConflictAction.FAIL})
	public int season;
	@Column
	public int games;
	
	public abstract List<StatItem> getValidFields();
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface StatField {
		public String Label() default "";
	}
	
	public class StatItem {
		public String label;
		public String value;
		
		public StatItem(String label, String value) {
			this.label = label;
			this.value = value;
		}
	}
	
	Stats() {
		super();
	}
	
	protected <T extends Stats> ArrayList<StatItem> fillStatItems(Class<T> statsClass) {
		Field[] fields = statsClass.getDeclaredFields();
		ArrayList<StatItem> items = new ArrayList<StatItem>();
		for (Field currField : fields) {
			String value = null;
			try {
				Object val;
				if (currField.getType() == Integer.class) {
					val = (Integer) currField.get(this);
				} else if (currField.getType() == Double.class) {
					val = (Double) currField.get(this);
				} else {
					val = (String) currField.get(this);
				}
				if (val != null) {
					value = val.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("Stats", e.getMessage());
			}
			StatField sField = currField.getAnnotation(StatField.class);
			if (sField != null && value != null) {
				items.add(new StatItem(sField.Label(), value));
			}
		}
		return items;
	}
	
	@Override
	public String toString() {
		return String.valueOf(season);
	}
}
