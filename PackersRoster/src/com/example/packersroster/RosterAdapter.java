package com.example.packersroster;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class RosterAdapter extends ArrayAdapter<Player> implements Filterable {

	static class ViewHolder {
		TextView column1;
		TextView column2;
	}
	
	private class playerNameCompare implements Comparator<Player> {
		@Override
		public int compare(Player arg0, Player arg1) {
			return arg0.name.compareTo(arg1.name);
		}
	}
	
	private class playerNumCompare implements Comparator<Player> {
		@Override
		public int compare(Player lhs, Player rhs) {
			int comp = lhs.getNumAsInt();
			int curr = rhs.getNumAsInt();
			if(comp < curr) return -1;
			if(comp > curr) return 1;
			else return 0;
		}
	}
	
	private List<Player> roster_list;
	private Context context;
	private int layout;
	private boolean[] sortCols;

	public RosterAdapter(Context context, int resource,
			List<Player> roster_list, int cols) {
		super(context, resource, roster_list);
		this.roster_list = roster_list;
		this.context = context;
		layout = resource;
		
		sortCols = new boolean[cols];
	}
	
	public void resetSort() {
		for(int i = 0; i < sortCols.length; i++) {
			sortCols[i] = false;
		}
	}
	
	public void sortRoster(int col, int viewId) {
		Comparator<Player> comp = null;
		switch(viewId) {
		case R.id.header_number:
			comp = new playerNumCompare();
			break;
		case R.id.header_name:
			comp = new playerNameCompare();
			break;
		}
		
		if(!sortCols[col]) {
			Collections.sort(roster_list, comp);
			
			for(int i = 0; i < sortCols.length; i++) {
				if(i != col) sortCols[i] = false;
			}
			sortCols[col] = true;
		}
		else Collections.reverse(roster_list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		TextView textView1, textView2;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, parent, false);
			textView1 = (TextView) convertView.findViewById(R.id.column1);
			textView2 = (TextView) convertView.findViewById(R.id.column2);

			viewHolder = new ViewHolder();
			viewHolder.column1 = textView1;
			viewHolder.column2 = textView2;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(position < roster_list.size()) {
			viewHolder.column1.setText(roster_list.get(position).number);
			viewHolder.column2.setText(roster_list.get(position).name);
		} else {
			Log.d("adapter", "Tried to build view index" + position);
		}

		return convertView;
	}

	@Override
    public Filter getFilter() {
		
		return new RosterFilter();
	}
	
	private class RosterFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence arg0) {
			for(int i = 0; i < sortCols.length; i++) {
				sortCols[i] = false;
			}
			
			FilterResults results = new FilterResults();			
			List<Player> players = Connection.filterPlayers(MainActivity.activeSport.sport, arg0.toString());

			results.count = players.size();
			results.values = players;
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence arg0, FilterResults arg1) {
			List<Player> filtered = (List<Player>)arg1.values;
			notifyDataSetChanged();
			clear();
			
			for(int i = 0; i < filtered.size(); i++) {
				add(filtered.get(i));
			}
			notifyDataSetInvalidated();
		}
		
	}
}
