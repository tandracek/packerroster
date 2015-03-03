package com.example.packersroster;

import com.activeandroid.query.Select;

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
        currPlayer = Connection.getPlayerById(playerId);
    
        this.buildTextView(R.id.nameView, currPlayer.name);
        this.buildTextView(R.id.numberView, "   - #" + currPlayer.number);
        this.buildTextView(R.id.posView, currPlayer.position);
        this.buildTextView(R.id.ageView, "Age: " + currPlayer.age);
        this.buildTextView(R.id.expView, "Exp: " + currPlayer.experience);
        this.buildTextView(R.id.collegeView, "College: " + currPlayer.college);
        this.buildTextView(R.id.salaryView, "Salary: " + currPlayer.salary);
        
        DraftInfo d = new Connection(this).getDraftStr(false, currPlayer);
        this.buildDraftDetailsView(d);
        
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
		TextView view = (TextView) findViewById(viewId);
		view.setText(text);
	}
	
	private class GetDetails extends AsyncTask<String, String, DraftInfo> {

		ProgressDialog pDialog;
		protected void onPreExecute() {
			pDialog = new ProgressDialog(PlayerDetails.this, ProgressDialog.STYLE_HORIZONTAL);
			pDialog = ProgressDialog.show(PlayerDetails.this, "Getting Details", "Retrieving details...");		
		}
		
		@Override
		protected DraftInfo doInBackground(String... params) {
			return new Connection(PlayerDetails.this).getDraftStr(true, currPlayer);
		}
		
		@Override
	    protected void onPostExecute(DraftInfo result) {
			buildDraftDetailsView(result);
			pDialog.dismiss();
	    }
	}
}
