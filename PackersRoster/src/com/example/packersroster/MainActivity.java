package com.example.packersroster;

import java.util.ArrayList;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String PLAYER_EXTRA = "com.example.packersroster.MainActivity";
	public DataHandler roster_handler;
	public Context main_context;
	public RosterAdapter roster_adapter;
	public ListView roster_view;
	
	private TextView helpText;
	private static final String TAG = "MainActivity";
	
	public static ActionBar actionBar;
	
	/* New variables and all needed */
	public static Connection connect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		main_context = this;
		/* TODO: figure out what this does */
		actionBar = getActionBar();
		setContentView(R.layout.activity_main);
		
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
				mainIntent.putExtra(PLAYER_EXTRA, player.id);
				startActivity(mainIntent);
			}
		});
		
		Button testBtn = (Button) findViewById(R.id.testBtn);
		//testBtn.setVisibility(View.GONE);
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
	
	/* TODO: Change to use new connection */
	public void refreshRoster() {
		new RosterDownload().execute();
	}
	
	/* TODO: Change to use new connection */
	public int deleteRoster() {
		
		return 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		Log.v("MainActivity", "Group " + item.getGroupId());

		if (item.getGroupId() == R.id.position_group) {
        	roster_adapter.setSortPos(item.getTitle().toString());
        	roster_adapter.notifyDataSetChanged();
        	return true;
		}
	    switch (item.getItemId()) {
	        case R.id.refresh_roster:
	        	if(roster_adapter.getCount() > 0) deleteRoster();
	            refreshRoster();
	            return true;
	        case R.id.delete_roster:
	        	int rows = deleteRoster();
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
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "in on stop");
	}
	
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "in on destroy");
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
			roster_adapter.addAll(result);
			roster_adapter.notifyDataSetChanged();
			pDialog.dismiss();
	    }

	}
}


