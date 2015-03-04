package com.example.packersroster;

import java.util.List;

import com.activeandroid.query.Select;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * TODO: build out other sports
 *       -put new icon in actionbar, clicking will produce a dialog that will allow you to switch teams
 *       -switching teams could just reset the player_list and then call to update the adapter and change color scheme, and update menu
 *       -color scheme could just be a small header bar at top of list (always at top?) that shows text of team and color
 */
public class MainActivity extends Activity {
	public static final String PLAYER_EXTRA = "com.example.packersroster.MainActivity";
	public DataHandler roster_handler;
	public Context main_context;
	public RosterAdapter roster_adapter;
	public ListView roster_view;
	
	public static String sport;
	public static int sportId;
	
	private TextView helpText;
	private static final String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		main_context = this;
		setContentView(R.layout.activity_main);
		
		PreferenceManager.setDefaultValues(this, R.layout.preferences, false);
		if (MainActivity.sport == null) {
			MainActivity.sportId = R.string.NFL;
			MainActivity.sport = "NFL";
		}
		
		List<Player> player_list = new Connection(this).getRoster(false);
		
		roster_view = (ListView) findViewById(R.id.listView1);
		roster_adapter = new RosterAdapter(this, R.layout.roster_list, player_list); 
		roster_view.setAdapter(roster_adapter);
		
		helpText = (TextView) findViewById(R.id.blankText);
		if(player_list.size() > 0) {
			helpText.setVisibility(View.GONE);
		}
		
		roster_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Player player = roster_adapter.getItem(arg2);
				
				Intent mainIntent = new Intent(main_context, PlayerDetails.class);
				mainIntent.putExtra(PLAYER_EXTRA, player.getId());
				startActivity(mainIntent);
			}
		});
		
		Button testBtn = (Button) findViewById(R.id.testBtn);
		testBtn.setVisibility(View.GONE);
		testBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Connection.getAndShowRoster(1, roster_adapter, roster_view, main_context);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getGroupId() == R.id.position_group) {
        	roster_adapter.setSortPos(item.getTitle().toString());
        	roster_adapter.notifyDataSetChanged();
        	return true;
		}
	    switch (item.getItemId()) {
	        case R.id.refresh_roster:
	        	if(roster_adapter.getCount() > 0) Connection.deleteRoster();
	        	new RosterDownload().execute();
	            return true;
	        case R.id.delete_roster:
	        	int rows = Connection.deleteRoster();
	        	roster_adapter.clear();
	        	roster_adapter.notifyDataSetChanged();
	        	helpText.setVisibility(View.VISIBLE);
	        	Toast toast = Toast.makeText(main_context, "Deleted " + rows + " rows", Toast.LENGTH_SHORT);
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
	
	private class RosterDownload extends AsyncTask<String, String, List<Player>> {

		ProgressDialog pDialog;
		protected void onPreExecute() {
			pDialog = new ProgressDialog(MainActivity.this, ProgressDialog.STYLE_HORIZONTAL);
			pDialog = ProgressDialog.show(MainActivity.this, "Getting Roster", "Retrieving roster...");		
		}

		@Override
		protected List<Player> doInBackground(String... params) {
			return new Connection(MainActivity.this).getRoster(true);
		}
		
		@Override
	    protected void onPostExecute(List<Player> result) {
			if(result.size() > 0) {
				helpText.setVisibility(View.GONE);
				roster_adapter.addAll(result);
				roster_adapter.notifyDataSetChanged();
			}
			pDialog.dismiss();
	    }

	}
}


