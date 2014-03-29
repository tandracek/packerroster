package com.example.packersroster;

import java.util.ArrayList;

import android.content.Context;
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
	private Context context;
	private int layout;

	public RosterAdapter(Context context, int resource,
			ArrayList<Player> roster_list) {
		super(context, resource, roster_list);
		this.roster_list = roster_list;
		this.context = context;
		layout = resource;
	}

	public Player getItem(int index) {

		return roster_list.get(index);
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

		viewHolder.textView1.setText(roster_list.get(position).getName());

		return convertView;
	}

}
