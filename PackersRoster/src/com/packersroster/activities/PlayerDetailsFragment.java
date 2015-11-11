package com.packersroster.activities;


import java.util.ArrayList;
import java.util.List;

import com.example.packersroster.R;
import com.packersroster.connection.SportDataUtils;
import com.packersroster.player.DraftInfo;
import com.packersroster.player.Player;
import com.packersroster.player.Stats;
import com.packersroster.ui.SeasonStatsAdapter;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PlayerDetailsFragment extends Fragment {
	private Player currPlayer;
	private Context context;
	private View mainView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getActivity();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	mainView = inflater.inflate(R.layout.details_fragment, container, false);
    	this.buildPlayerDetails();
        return mainView;
    }

    public void setPlayer(Player player) {
    	this.currPlayer = player;
    }
    
	private void buildDraftDetailsView(DraftInfo dInfo) {
		if(dInfo == null) return;
		
		String dText;
		if (dInfo.isUndrafted) {
			dText = this.getResources().getString(R.string.undrafted);
		}
		else dText = "Round: " + dInfo.round + " Pick: " + dInfo.pick + " Year: " + dInfo.year + " by the " + dInfo.team;
		this.buildTextView(R.id.draftInfoView, dText);
	}
    
	private void buildTextView(int viewId, String text) {
		if (text == null) return;
		TextView view = (TextView) mainView.findViewById(viewId);
		view.setText(text);
	}
	
	public void refreshPlayerDetails(Player player) {
		this.currPlayer = player;
		this.buildPlayerDetails();
	}
	
	public void buildPlayerDetails() {
        this.buildTextView(R.id.nameView, currPlayer.name);
        this.buildTextView(R.id.numberView, "   - #" + currPlayer.number);
        this.buildTextView(R.id.posView, currPlayer.position);
        this.buildTextView(R.id.ageViewText, currPlayer.age);
        this.buildTextView(R.id.expViewText, currPlayer.experience);
        this.buildTextView(R.id.collegeViewText, currPlayer.college);
        this.buildTextView(R.id.salaryViewText, currPlayer.salary);
        this.buildTextView(R.id.weightViewText, currPlayer.weight);
        this.buildTextView(R.id.heightViewText, currPlayer.height);
        this.buildTextView(R.id.bornDateViewText, currPlayer.bornDate);
        this.buildTextView(R.id.bornPlaceViewText, currPlayer.bornPlace);
        this.buildDraftDetailsView(currPlayer.draftInfo);
	}
}
