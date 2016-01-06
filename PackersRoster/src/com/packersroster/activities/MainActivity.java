package com.packersroster.activities;

import java.util.List;

import com.example.packersroster.R;
import com.example.packersroster.R.id;
import com.example.packersroster.R.layout;
import com.example.packersroster.R.menu;
import com.packersroster.connection.SportDataUtils;
import com.packersroster.player.Player;
import com.packersroster.ui.PosSortDialog;
import com.packersroster.ui.RosterAdapter;
import com.packersroster.ui.SportStyles;
import com.packersroster.ui.PosSortDialog.PosSortInterface;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

// TODO: change to odd/even row styling
public class MainActivity extends Activity implements
		PopupMenu.OnMenuItemClickListener, PosSortDialog.PosSortInterface {
	public static final String PLAYER_EXTRA = "com.example.packersroster.MainActivity";
	public Context mainContext;
	public RosterAdapter rosterAdapter;
	public ListView rosterView;

	public static String sport;
	public static String sportPref;
	public static int sportId;
	public static SportStyles activeSport;
	public static List<String> positions;

	private ActionBar aBar;
	private TextView helpText;
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mainContext = this;
		setContentView(R.layout.activity_main);

		aBar = getActionBar();
		aBar.setDisplayOptions(8, 8);
		PreferenceManager.setDefaultValues(this, R.layout.preferences, false);
		if (MainActivity.activeSport == null) {
			MainActivity.activeSport = SportStyles.NFL;
			MainActivity.positions = SportDataUtils.getPositions(MainActivity.activeSport.sport);
			this.setActionBarStyle();
		}

		List<Player> player_list = SportDataUtils.getRoster(false, MainActivity.activeSport);

		rosterView = (ListView) findViewById(R.id.listView1);
		rosterAdapter = new RosterAdapter(this, R.layout.roster_list,
				player_list, 2);
		rosterView.setAdapter(rosterAdapter);

		helpText = (TextView) findViewById(R.id.blankText);
		if (player_list.size() > 0) {
			helpText.setVisibility(View.GONE);
		}

		rosterView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Player player = rosterAdapter.getItem(arg2);

						Intent mainIntent = new Intent(mainContext,
								PlayerDetails.class);
						mainIntent.putExtra(PLAYER_EXTRA, player.getId());
						startActivity(mainIntent);
					}
				});
	}
	
	public void posPopup(MenuItem item) {
		PosSortDialog popup = new PosSortDialog();
		popup.setPositions(MainActivity.positions);
		popup.show(getFragmentManager(), "posWin");
	}

	public void switchPopup(MenuItem item) {
		View v = findViewById(R.id.action_switch);
		PopupMenu popup = new PopupMenu(this, v);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.swithmenu, popup.getMenu());
		popup.setOnMenuItemClickListener(this);
		popup.show();
	}
	
	public void onHeaderClick(View v) {
		int index = ((ViewGroup) v.getParent()).indexOfChild(v);
		rosterAdapter.sortRoster(index, v.getId());
		rosterAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onPosClickListener(CharSequence position) {
		rosterAdapter.getFilter().filter(position);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nfl_switch:
			if(!MainActivity.activeSport.sport.equals(SportStyles.NFL.sport)) {
				MainActivity.activeSport = SportStyles.NFL;
				this.SwitchSports();
			}
			return true;
		case R.id.mlb_switch:
			if(!MainActivity.activeSport.sport.equals(SportStyles.MLB.sport)) {
				MainActivity.activeSport = SportStyles.MLB;
				this.SwitchSports();
			}
			return true;
		case R.id.nba_switch:
			if(!MainActivity.activeSport.sport.equals(SportStyles.NBA.sport)) {
				MainActivity.activeSport = SportStyles.NBA;
				this.SwitchSports();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh_roster:
			if (rosterAdapter.getCount() > 0)
				SportDataUtils.deleteRoster(MainActivity.activeSport.sport);
			new RosterDownload().execute();
			return true;
		case R.id.delete_roster:
			int rows = SportDataUtils.deleteRoster(MainActivity.activeSport.sport);
			rosterAdapter.clear();
			rosterAdapter.notifyDataSetChanged();
			helpText.setVisibility(View.VISIBLE);
			Toast toast = Toast.makeText(mainContext, "Deleted " + rows
					+ " rows", Toast.LENGTH_SHORT);
			toast.show();
			return true;
		case R.id.settings_id:
			startActivity(new Intent(mainContext, SettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onStop() {
		super.onStop();
	}

	protected void onDestroy() {
		super.onDestroy();
	}
	
	protected void onResume() {
		super.onResume();
		setActionBarStyle();
	}

	public void SwitchSports() {
		// TODO: check that they actually selected a new sport, also figure out if setting styles should be done here and not in async
		rosterAdapter.resetSort();
		new RosterDownload().execute(new Boolean[] { false });
	}
	
	public void setActionBarStyle() {
		if (aBar == null) aBar = getActionBar();
		aBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(MainActivity.activeSport.color)));
		aBar.setTitle(getResources().getString(MainActivity.activeSport.displayId));
	}

	private class RosterDownload extends
			AsyncTask<Boolean, String, List<Player>> {

		ProgressDialog pDialog;

		protected void onPreExecute() {
			pDialog = new ProgressDialog(MainActivity.this,
					ProgressDialog.STYLE_HORIZONTAL);
			pDialog = ProgressDialog.show(MainActivity.this, "Getting Roster",
					"Retrieving roster...");
		}

		@Override
		protected List<Player> doInBackground(Boolean... params) {
			boolean goOnline = true;
			if (params.length > 0) {
				goOnline = params[0].booleanValue();
			}
			List<Player> newRoster = SportDataUtils.getRoster(goOnline, MainActivity.activeSport);
			MainActivity.positions = SportDataUtils.getPositions(MainActivity.activeSport.sport);
			return newRoster;
		}

		@Override
		protected void onPostExecute(List<Player> result) {
			pDialog.dismiss();
			rosterAdapter.clear();
			if (result.size() > 0) {
				helpText.setVisibility(View.GONE);
				rosterAdapter.addAll(result);
			} else {
				helpText.setVisibility(View.VISIBLE);
			}
			rosterAdapter.notifyDataSetChanged();
			setActionBarStyle();
		}

	}


}
