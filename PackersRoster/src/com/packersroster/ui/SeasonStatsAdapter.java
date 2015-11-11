package com.packersroster.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.packersroster.R;
import com.packersroster.player.Stats;
import com.packersroster.player.Stats.StatField;
import com.packersroster.player.Stats.StatItem;
import com.packersroster.ui.RosterAdapter.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SeasonStatsAdapter extends ArrayAdapter<StatItem> {
	private Stats stats;
	private Context context;
	private int layout;
	private List<StatItem> items;

	static class ViewHolder {
		TextView label;
		TextView value;
	}

	public SeasonStatsAdapter(Context context, int resource, Stats stats) {
		super(context, resource);
		this.items = stats.getValidFields();
		this.addAll(items);
		this.stats = stats;
		this.layout = resource;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		TextView textView1, textView2;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, parent, false);
			textView1 = (TextView) convertView
					.findViewById(R.id.statsLabelTextView);
			textView2 = (TextView) convertView
					.findViewById(R.id.statsValueTextView);

			viewHolder = new ViewHolder();
			viewHolder.label = textView1;
			viewHolder.value = textView2;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		StatItem item = items.get(position);
		viewHolder.label.setText(item.label);
		viewHolder.value.setText(item.value);

		return convertView;
	}
}
