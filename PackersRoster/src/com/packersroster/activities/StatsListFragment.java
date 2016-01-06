package com.packersroster.activities;

import java.util.List;

import com.example.packersroster.R;
import com.packersroster.player.Stats;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StatsListFragment extends ListFragment {
	private OnSeasonListClick click;
	private List<Stats> statList;
	private Context context;
	private ArrayAdapter<Stats> adapter;
	
	public interface OnSeasonListClick {
		void onClick(Stats stats);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		adapter = new ArrayAdapter<Stats>(context,
				R.layout.stats_list_item, R.id.statsLabelTextView, statList);
		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.setEmptyText("No stats found.  Select the button to download the list of stats.");
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (click != null) {
			this.click.onClick(statList.get(position));
		}
	}

	public void setStatList(List<Stats> statList) {
		this.statList = statList;
	}
	
	public void setOnItemClickListener(OnSeasonListClick click) {
		this.click = click;
	}
	
	public void refreshList(List<Stats> statList) {
		this.adapter.addAll(statList);
		this.adapter.notifyDataSetChanged();
	}
}
