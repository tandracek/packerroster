package com.example.packersroster;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * TODO: build config page
 */
public class MainActivity extends Activity {
	public static final String PLAYER_EXTRA = "com.example.packersroster.MainActivity";
	public DataHandler roster_handler;
	public Context main_context;
	public RosterAdapter roster_adapter;
	public ListView roster_view;
	
	private TextView helpText;
	private static final String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		main_context = this;
		setContentView(R.layout.activity_main);
		
		PreferenceManager.setDefaultValues(this, R.layout.preferences, false);
		
		List<Player> player_list = Connection.getRoster(0);
		
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
	public void buildAdapter() {
		roster_adapter.clear();
		List<Player> player_list_1 = Connection.getRoster(0);
		roster_adapter.addAll(player_list_1);
		roster_adapter.notifyDataSetChanged();
		
		if(player_list_1.size() > 0) helpText.setVisibility(View.GONE);
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
			return Connection.getRoster(1);
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


