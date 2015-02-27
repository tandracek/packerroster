package com.example.packersroster;

import java.util.ArrayList;


public abstract class WebSite {

	abstract boolean connect(String URL);
	abstract ArrayList<Player> getRoster();
	abstract String getDraftStr(String playerURL);
	abstract DraftInfo getDraftInfo(String playerUrl);
	abstract boolean getInitialData();
	abstract boolean getDetails();
}
