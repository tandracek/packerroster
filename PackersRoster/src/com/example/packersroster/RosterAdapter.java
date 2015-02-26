package com.example.packersroster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RosterAdapter extends ArrayAdapter<Player> {

	static class ViewHolder {
		TextView textView1;
	}
	
	private ArrayList<Player> roster_list;
	private ArrayList<Player> hidden_list;
	private Context context;
	private String sortPos;
	private boolean useHidden;
	private int layout;

	public RosterAdapter(Context context, int resource,
			ArrayList<Player> roster_list) {
		super(context, resource, roster_list);
		this.roster_list = roster_list;
		this.context = context;
		layout = resource;
		this.hidden_list = new ArrayList<Player>();
		this.useHidden = false;
	}

	public Player getItem(int index) {
		if(useHidden) return hidden_list.get(index);
		else return roster_list.get(index);
	}

	
	public void setSortPos(String pos) {
		if(pos.equals(sortPos)) return;

		if(pos.equals("All")) {
			if(useHidden) addAll(hidden_list, roster_list);
			else addAll(roster_list, hidden_list);
			sortPos = pos;
			return;
		}
		
		if((sortPos != null) && (!pos.equals(sortPos))) useHidden = !useHidden;
		
		if(useHidden) switchLists(hidden_list, roster_list, pos);
		else switchLists(roster_list, hidden_list, pos);
		
		sortPos = pos;
	}
	
	private void addAll(ArrayList<Player> source, ArrayList<Player> hidden) {
		Player tempPlayer;
		for(int i = 0; i < hidden.size(); i++) {
			tempPlayer = hidden.get(i);
			source.add(tempPlayer);
		}
	}
	
	private void switchLists(ArrayList<Player> source, ArrayList<Player> hidden, String pos) {
		Player tempPlayer;
		for(int i = (source.size() - 1); i >= 0; i--) {
			tempPlayer = source.get(i);
			if(!tempPlayer.group.equals(pos)) {
				source.remove(i);
				hidden.add(tempPlayer);
			}
		}
	}
	
	public void sort(String value, String order) {
		ArrayList<Player> toSort;
		if(useHidden) toSort = hidden_list;
		else toSort = roster_list;
		Player.sortBy = value;

		if(order.equals("asc")) Collections.sort(toSort);
		else Collections.reverse(toSort);
	}
	
	@Override
	public int getCount() {
		if(useHidden) return hidden_list.size();
		else return roster_list.size();
	}
	
	/* Look at this more closely, don't i want to clear both? */
	@Override
	public void clear() {
		super.clear();
		
		if(useHidden) roster_list.clear();
		else hidden_list.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		TextView textView;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, parent, false);
			textView = (TextView) convertView.findViewById(R.id.textView1);

			viewHolder = new ViewHolder();
			viewHolder.textView1 = textView;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String displayText;
		if(useHidden) displayText = (hidden_list.get(position).number + " - " + hidden_list.get(position).name);
		else displayText = (roster_list.get(position).number + " - " + roster_list.get(position).name);
		viewHolder.textView1.setText(displayText);

		return convertView;
	}

}