package com.example.packersroster;

import java.util.ArrayList;


public abstract class WebSite {
	
	abstract ArrayList<Player> getRoster();
	abstract DraftInfo getDraftInfo(String playerUrl);
}
