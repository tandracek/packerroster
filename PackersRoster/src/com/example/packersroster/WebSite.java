package com.example.packersroster;


public interface WebSite {

	boolean connect(String URL);
	boolean getInitialData();
	boolean getDetails();
}
