package com.example.packersroster;

import java.util.List;

import com.activeandroid.query.Select;
import com.example.packersroster.SettingsActivity.SettingsFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements
		PopupMenu.OnMenuItemClickListener {
	public static final String PLAYER_EXTRA = "com.example.packersroster.MainActivity";
	public DataHandler roster_handler;
	public Context main_context;
	public RosterAdapter roster_adapter;
	public ListView roster_view;

	public static String sport;
	public static String sportPref;
	public static int sportId;
	public static SportStyles activeSport;
	public static List<String> positions;

	private ActionBar aBar;
	private TextView helpText;
	private static final String TAG = "MainActivity";

	//TODO: -set sorting positions for sports, maybe just put them all in groupings in menu
	//      -change layout so header columns sit at top that user can click on to sort, can ultimately get rid of sorting/grouping in settings
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		main_context = this;
		setContentView(R.layout.activity_main);

		aBar = getActionBar();
		aBar.setDisplayOptions(8, 8);
		PreferenceManager.setDefaultValues(this, R.layout.preferences, false);
		if (MainActivity.activeSport == null) {
			MainActivity.activeSport = SportStyles.NFL;
			this.setActionBarStyle();
		}

		List<Player> player_list = new Connection(this).getRoster(false);

		roster_view = (ListView) findViewById(R.id.listView1);
		roster_adapter = new RosterAdapter(this, R.layout.roster_list,
				player_list);
		roster_view.setAdapter(roster_adapter);

		helpText = (TextView) findViewById(R.id.blankText);
		if (player_list.size() > 0) {
			helpText.setVisibility(View.GONE);
		}

		roster_view
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Player player = roster_adapter.getItem(arg2);

						Intent mainIntent = new Intent(main_context,
								PlayerDetails.class);
						mainIntent.putExtra(PLAYER_EXTRA, player.getId());
						startActivity(mainIntent);
					}
				});

		Button testBtn = (Button) findViewById(R.id.testBtn);
		testBtn.setVisibility(View.GONE);
		testBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Connection.getAndShowRoster(1, roster_adapter, roster_view,
				// main_context);
			}
		});
	}

	public void switchPopup(MenuItem item) {
		View v = findViewById(R.id.action_switch);
		PopupMenu popup = new PopupMenu(this, v);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.swithmenu, popup.getMenu());
		popup.setOnMenuItemClickListener(this);
		popup.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nfl_switch:
			MainActivity.activeSport = SportStyles.NFL;
			this.SwitchSports();
			return true;
		case R.id.mlb_switch:
			MainActivity.activeSport = SportStyles.MLB;
			this.SwitchSports();
			return true;
		case R.id.nba_switch:
			MainActivity.activeSport = SportStyles.NBA;
			this.SwitchSports();
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
		if (item.getGroupId() == R.id.position_group) {
			//roster_adapter.setSortPos(item.getTitle().toString());
			roster_adapter.getFilter().filter(item.getTitle().toString());
			roster_adapter.notifyDataSetChanged();
			return true;
		}
		switch (item.getItemId()) {
		case R.id.refresh_roster:
			if (roster_adapter.getCount() > 0)
				Connection.deleteRoster();
			new RosterDownload().execute();
			return true;
		case R.id.delete_roster:
			int rows = Connection.deleteRoster();
			roster_adapter.clear();
			roster_adapter.notifyDataSetChanged();
			helpText.setVisibility(View.VISIBLE);
			Toast toast = Toast.makeText(main_context, "Deleted " + rows
					+ " rows", Toast.LENGTH_SHORT);
			toast.show();
			return true;
		case R.id.menu_number:
			roster_adapter.sort("number", "asc");
			roster_adapter.notifyDataSetChanged();
			return true;
		case R.id.menu_name:
			roster_adapter.sort("name", "asc");
			roster_adapter.notifyDataSetChanged();
			return true;
		case R.id.settings_id:
			startActivity(new Intent(main_context, SettingsActivity.class));
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

	public void SwitchSports() {
		// TODO: check that they actually selected a new sport, also figure out if setting styles should be done here and not in async

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
			MainActivity.positions = Connection.getPositions(MainActivity.activeSport.sport);
			return new Connection(MainActivity.this).getRoster(goOnline);
		}

		@Override
		protected void onPostExecute(List<Player> result) {
			roster_adapter.clear();
			if (result.size() > 0) {
				helpText.setVisibility(View.GONE);
				roster_adapter.addAll(result);
				roster_adapter.notifyDataSetChanged();
			} else {
				helpText.setVisibility(View.VISIBLE);
				roster_adapter.notifyDataSetChanged();
			}
			setActionBarStyle();
			pDialog.dismiss();
		}

	}

}
