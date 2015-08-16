package com.packersroster.activities;

import com.example.packersroster.R;
import com.packersroster.connection.SportDataUtils;
import com.packersroster.player.DraftInfo;
import com.packersroster.player.Player;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerDetails extends Activity {
	private Player currPlayer;
	private ActionBar aBar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        
        aBar = getActionBar();
        aBar.setDisplayHomeAsUpEnabled(true);
        
        Intent intent = getIntent();
        Long playerId = intent.getLongExtra(MainActivity.PLAYER_EXTRA, 0);
        currPlayer = SportDataUtils.getPlayerById(playerId);
        this.buildPlayerDetails();
        
        Button detailsBtn = (Button) findViewById(R.id.detailsBtn);
        detailsBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        		new GetDetails().execute(new String[] {currPlayer.link});
        	}
        });
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
		TextView view = (TextView) findViewById(viewId);
		view.setText(text);
	}
	
	private void buildPlayerDetails() {
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
	
	private class GetDetails extends AsyncTask<String, String, Player> {

		ProgressDialog pDialog;
		protected void onPreExecute() {
			pDialog = new ProgressDialog(PlayerDetails.this, ProgressDialog.STYLE_HORIZONTAL);
			pDialog = ProgressDialog.show(PlayerDetails.this, "Getting Details", "Retrieving details...");		
		}
		
		@Override
		protected Player doInBackground(String... params) {
			return SportDataUtils.getDetails(true, currPlayer);
		}
		
		@Override
	    protected void onPostExecute(Player result) {
			currPlayer = result;
			buildPlayerDetails();
			pDialog.dismiss();
	    }
	}
	
}
