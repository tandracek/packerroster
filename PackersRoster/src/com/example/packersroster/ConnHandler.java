package com.example.packersroster;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class ConnHandler extends AsyncTask<String, String, String>{
	private ArrayList<Player> player_list;
	private String draftStr;
	private Context context;
	private DataHandler data_handler;
	private YahooSite yahoo;
	private Activity main;
	private ProgressDialog pDialog;
	
	private String link;
	private int site;
	private int returnAct;
	private int playerId;
	
	private final String YAHOO_MAIN_URL = "http://sports.yahoo.com/nfl/teams/gnb/roster/";
	private final String NFL_MAIN_URL = "http://www.nfl.com/players/search?category=team&filter=1800&playerType=current";
	
	ConnHandler(Context context, Activity main) {
		player_list = new ArrayList<Player>();
		this.context = context;
		data_handler = new DataHandler(context);
		this.main = main;
		this.playerId = 0;
	}
	
	public void setPid(int pid) {
		this.playerId = pid;
	}
	
	public void connect_init(String type, String returnAct) {
		if(type.equals("roster")) site = 1;
		if(type.equals("nfl")) site = 2;
		if(type.equals("details")) site = 3;
		
		if(returnAct.equals("main")) this.returnAct = 1;
		if(returnAct.equals("details")) this.returnAct = 2;
	}
	
	public ArrayList<Player> getRoster() {
		return player_list;
	}
	
	private boolean saveData() {
		boolean err = false;
		
		switch(site) {
		case 1:
			err = data_handler.addRoster(this.player_list);
			break;
		case 2:
			break;
		case 3:
			if(this.playerId == 0) break;
			err = data_handler.addValue("draft", draftStr, this.playerId);
			break;
		}
		return err;
	}
	
	public void returnToActivity() {

	}

	@Override
    protected void onPostExecute(String result) {
		if(result != null) {
			Toast toast = Toast.makeText(context, result, Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Log.v("on post", "trying to build adapter");
			this.saveData();
			this.returnToActivity();
		}
		pDialog.dismiss();
    }

	@Override
	protected String doInBackground(String... params) {
		String errMsg = null;
		
		return errMsg;
	}
	
	protected void onPreExecute() {
		pDialog = new ProgressDialog(context, ProgressDialog.STYLE_HORIZONTAL);
		pDialog = ProgressDialog.show(context, "Getting Roster", "Connecting to site...");
	}
	
	protected void onProgressUpdate(String... progress) {
        pDialog.setMessage(progress[0]);
    }
}
