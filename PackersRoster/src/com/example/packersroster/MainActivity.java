package com.example.packersroster;

import java.util.ArrayList;
import java.util.List;

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
	private static final String TAG = "MainActivity";
	public DataHandler roster_handler;
	public Context main_context;
	public RosterAdapter roster_adapter;
	public ListView roster_view;
	private MainActivity main_activity;
	private ProgressDialog progress;
	private TextView helpText;
	
	public static ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		main_activity = this;
		main_context = this;
		actionBar = getActionBar();
		setContentView(R.layout.activity_main);
		
		roster_handler = new DataHandler(this);
		
		ArrayList<Player> player_list = new ArrayList<Player>();
		player_list = roster_handler.getRoster();
		
		/*player_list.add(new Player("Aaron Rodgers", "QB", "12"));
		player_list.add(new Player("Jordy Nelson", "WR", "87"));
		player_list.add(new Player("Eddie Lacy", "RB", "27"));*/
		if(savedInstanceState == null) 
		Log.v(TAG, "in oncreate");
		
		
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
				// TODO Auto-generated method stub
				Player player = roster_adapter.getItem(arg2);
				
				Intent mainIntent = new Intent(main_context, PlayerDetails.class);
				mainIntent.putExtra(PLAYER_EXTRA, player.id);
				startActivity(mainIntent);
			}
			
		});
	}
	public void buildAdapter() {
		Log.v(TAG, "in build adapter");
		roster_adapter.clear();
		ArrayList<Player> player_list_1 = new ArrayList<Player>();
		player_list_1 = roster_handler.getRoster();
		roster_adapter.addAll(player_list_1);
		roster_adapter.notifyDataSetChanged();
		
		if(player_list_1.size() > 0) helpText.setVisibility(View.GONE);
	}
	
	public void refreshRoster() {
		ConnHandler connection = new ConnHandler(main_context, main_activity);
		connection.connect_init("roster", "main");
		connection.execute(" ");
	}
	
	public int deleteRoster() {
		if(roster_handler == null) roster_handler = new DataHandler(main_context);
		return roster_handler.deleteRoster();
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
	        case R.id.menu_all:
	        	roster_adapter.setSortPos("All");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_qb:
	        	roster_adapter.setSortPos("QB");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_rb:
	        	roster_adapter.setSortPos("RB");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_wr:
	        	roster_adapter.setSortPos("WR");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_te:
	        	roster_adapter.setSortPos("TE");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_ol:
	        	roster_adapter.setSortPos("OL");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_dl:
	        	roster_adapter.setSortPos("DL");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_lb:
	        	roster_adapter.setSortPos("LB");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_cb:
	        	roster_adapter.setSortPos("CB");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_s:
	        	roster_adapter.setSortPos("S");
	        	roster_adapter.notifyDataSetChanged();
	        	return true;
	        case R.id.menu_sp:
	        	roster_adapter.setSortPos("SP");
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
	
}


