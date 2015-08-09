package com.packersroster.ui;

import java.util.List;

import com.example.packersroster.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PositionAdapter extends ArrayAdapter<String>{

	static class ViewHolder {
		TextView textView1;
	}
	
	List<String> positions;
	private Context context;
	private final int layout;
	
	public PositionAdapter(Context context, int resource,
			List<String> objects) {
		super(context, resource, objects);
		this.positions = objects;
		this.context = context;
		this.layout = resource;
	}

	public String getItem(int index) {
		return positions.get(index);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		TextView textView;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, parent, false);
			textView = (TextView) convertView.findViewById(R.id.action_group);

			viewHolder = new ViewHolder();
			viewHolder.textView1 = textView;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String displayText = positions.get(position);
		viewHolder.textView1.setText(displayText);

		return convertView;
	}
}
