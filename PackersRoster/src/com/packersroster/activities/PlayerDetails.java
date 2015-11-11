package com.packersroster.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.packersroster.R;
import com.packersroster.connection.SportDataUtils;
import com.packersroster.player.DraftInfo;
import com.packersroster.player.Player;
import com.packersroster.player.Stats;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PlayerDetails extends Activity implements StatsListFragment.OnSeasonListClick {
	public static final String DETAILS_FRAG = "detailsFragment";
	public static final String LIST_FRAG = "sListFragment";

	public Player player;
	public List<Stats> statList;
	public Context context;
	public PlayerDetailsFragment playerDetailsFrag;
	public StatsListFragment sListFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_layout);
		Intent intent = getIntent();
		Long pId = intent.getLongExtra(MainActivity.PLAYER_EXTRA, -1);
		if (pId > -1) {
			player = SportDataUtils.getPlayerById(pId);
	        statList = SportDataUtils.getStats(false, player);
	        if(statList == null) {
	        	statList = new ArrayList<Stats>();
	        }
			this.buildFragments();
		}
		context = this;
		
        Button detailsBtn = (Button) findViewById(R.id.detailsBtn);
        detailsBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        		new GetDetails().execute(new String[] {player.link});
        	}
        });
        
        Button statsBtn = (Button) findViewById(R.id.statsBtn);
        statsBtn.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		new GetStats().execute(new String[] {}); 
        	}
        });
	}

	private void buildFragments() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		playerDetailsFrag = new PlayerDetailsFragment();
		playerDetailsFrag.setPlayer(player);
		ft.add(R.id.playerDetailsLayout, playerDetailsFrag, PlayerDetails.DETAILS_FRAG);
		sListFrag = new StatsListFragment();
		sListFrag.setStatList(statList);
		sListFrag.setOnItemClickListener(this);
		ft.add(R.id.seasonListLayout, sListFrag, PlayerDetails.LIST_FRAG);
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		ft.commit();
		
	}

	@Override
	public void onClick(Stats stats) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		StatsFragment statsFrag = new StatsFragment();
		statsFrag.setStats(stats);
		ft.replace(R.id.playerDetailsLayout, statsFrag, PlayerDetails.DETAILS_FRAG);
		ft.commit();
	}
	
	private class GetDetails extends AsyncTask<String, String, Player> {

		ProgressDialog pDialog;
		protected void onPreExecute() {
			pDialog = new ProgressDialog(context, ProgressDialog.STYLE_HORIZONTAL);
			pDialog = ProgressDialog.show(context, "Getting Details", "Retrieving details...");		
		}
		
		@Override
		protected Player doInBackground(String... params) {
			return SportDataUtils.getDetails(true, player);
		}
		
		@Override
	    protected void onPostExecute(Player result) {
			player = result;
			playerDetailsFrag.refreshPlayerDetails(player);
			pDialog.dismiss();
	    }
	}
	
	private class GetStats extends AsyncTask<String, String, List<Stats>> {

		ProgressDialog pDialog;
		protected void onPreExecute() {
			pDialog = new ProgressDialog(context, ProgressDialog.STYLE_HORIZONTAL);
			pDialog = ProgressDialog.show(context, "Getting Stats", "Retrieving stats...");		
		}
		
		@Override
		protected List<Stats> doInBackground(String... params) {
			return SportDataUtils.getStats(true, player);
		}
		
		@Override
	    protected void onPostExecute(List<Stats> result) {
			if (result == null) {
				Log.e("PlayerDetails", "Stats is null");
				pDialog.dismiss();
				//throw error
			} else {
				statList = result;
				sListFrag.refreshList(statList);
				pDialog.dismiss();
			}
	    }
	}
}
