package com.packersroster.activities;


import com.example.packersroster.R;
import com.packersroster.player.Stats;
import com.packersroster.ui.SeasonStatsAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class StatsFragment extends Fragment {
	private Stats stats;
	private SeasonStatsAdapter sAdapter;
	private ListView sListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sAdapter = new SeasonStatsAdapter(getActivity(), R.layout.stats_list_item, this.stats);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	View mainView = inflater.inflate(R.layout.stats_fragment, container, false);
		sListView = (ListView) mainView.findViewById(R.id.statsFragListView);
		sListView.setAdapter(sAdapter);
        return mainView;
    }

    public void setStats(Stats stats) {
    	this.stats = stats;
    }
}
