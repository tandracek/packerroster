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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
	public static final String STATS_FRAG = "statsFragment";
	public static final String LIST_FRAG = "sListFragment";

	public Player player;
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
	        List<Stats> statList = SportDataUtils.getStats(false, player, null);
	        if(statList == null) {
	        	statList = new ArrayList<Stats>();
	        }
	        player.stats = statList;
			this.buildFragments();
		}
		context = this;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.details, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void buildFragments() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		playerDetailsFrag = new PlayerDetailsFragment();
		playerDetailsFrag.setPlayer(player);
		ft.add(R.id.playerDetailsLayout, playerDetailsFrag, PlayerDetails.DETAILS_FRAG);
		sListFrag = new StatsListFragment();
		sListFrag.setStatList(player.stats);
		sListFrag.setOnItemClickListener(this);
		ft.add(R.id.seasonListLayout, sListFrag, PlayerDetails.STATS_FRAG);
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		ft.commit();
		
	}

	@Override
	public void onClick(Stats stats) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		StatsFragment statsFrag = new StatsFragment();
		statsFrag.setStats(stats);
		ft.replace(R.id.playerDetailsLayout, statsFrag, PlayerDetails.STATS_FRAG);
		ft.commit();
	}
	
	public void backToBio(MenuItem item) {
		if (playerDetailsFrag.isVisible()) return;
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.playerDetailsLayout, playerDetailsFrag, PlayerDetails.DETAILS_FRAG);
		ft.commit();
	}
	
	public void refreshDetails(MenuItem item) {
		new GetDetails().execute(new String[] {player.link});
	}
	
	private class GetDetails extends AsyncTask<String, String, Void> {

		ProgressDialog pDialog;
		protected void onPreExecute() {
			pDialog = new ProgressDialog(context, ProgressDialog.STYLE_HORIZONTAL);
			pDialog = ProgressDialog.show(context, "Getting Details", "Retrieving details...");		
		}
		
		@Override
		protected Void doInBackground(String... params) {
			player = SportDataUtils.getDetailsAndStats(true, player);
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void v) {
			if (player != null) {
				playerDetailsFrag.refreshPlayerDetails(player);
			}
			if (sListFrag != null) {
				sListFrag.refreshList(player.stats);
			}
			pDialog.dismiss();
	    }
	}
}
