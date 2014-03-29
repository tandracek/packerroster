package com.example.packersroster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerDetails extends Activity {
	private Player currPlayer;
	private ConnHandler connection;
	private DataHandler dataHandler;
	private TextView draftView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        
        Intent intent = getIntent();
        int playerID = intent.getIntExtra(MainActivity.PLAYER_EXTRA, 0);
        
        connection = new ConnHandler(this, this);
        connection.connect_init("details", "details");
        connection.setPid(playerID);
        
        dataHandler = new DataHandler(this);
   
        DataHandler dataHand = new DataHandler(this);
        currPlayer = dataHand.getPlayer(playerID);
    
        TextView nameHeader = (TextView) findViewById(R.id.nameView);
        nameHeader.setText(currPlayer.getName());
        
        TextView numberHeader = (TextView) findViewById(R.id.numberView);
        numberHeader.setText("   - #" + currPlayer.getNumber());
        
        TextView posHeader = (TextView) findViewById(R.id.posView);
        posHeader.setText(currPlayer.getPosition());
        
        TextView ageHeader = (TextView) findViewById(R.id.ageView);
        ageHeader.setText("Age: " + currPlayer.age);
        
        TextView expHeader = (TextView) findViewById(R.id.expView);
        expHeader.setText("Exp: " + currPlayer.experience);
        
        TextView collegeHeader = (TextView) findViewById(R.id.collegeView);
        collegeHeader.setText("College: " + currPlayer.college);
        
        draftView = (TextView) findViewById(R.id.draftInfoView);
        draftView.setText(currPlayer.draftStr);
        
        TextView salaryHeader = (TextView) findViewById(R.id.salaryView);
        salaryHeader.setText("Salary: " + currPlayer.salary);
        
        Button detailsBtn = (Button) findViewById(R.id.detailsBtn);
        detailsBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        		connection.execute(currPlayer.link);
        	}
        });
    }
	public void buildAdapter() {
		String draftStr = dataHandler.getValue("draft", "player_details", currPlayer.id);
		DraftInfo dInfo = new DraftInfo(draftStr);
		
		draftView.setText(dInfo.getDraftDisplay());
	}

}
