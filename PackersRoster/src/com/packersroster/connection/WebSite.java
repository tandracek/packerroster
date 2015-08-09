package com.packersroster.connection;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public abstract class WebSite {
	protected Document doc;
	
	protected WebSite() {

	}
	
	protected boolean connect(String Url) {
		String connectUrl = Url;
		try {
			doc = Jsoup
					.connect(
							connectUrl).header("Accept-Encoding", "gzip, deflate").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0")
					.get();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	protected String itemSelect(Element item, String selector) {
		String ret = "-";
		Elements eles = item.select(selector);
		if (eles.size() > 0) ret = eles.get(0).ownText();
		
		return ret;
	}
	
	protected String formatName(String name) {
		String[] names = name.split(" ");
		
		if(names.length > 1) {
			String returnStr = names[1] + ", " + names[0];
			return returnStr;
		}
		return name;
	}
}
