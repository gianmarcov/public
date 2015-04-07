/* Copyright (C) Vitelli Gianmarco - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Vitelli Gianmarco <gianmarco.vitelli@gmail.com>, 21:05 19 Februar 2015
 * 
 * File:
 * /EventMatchingCalculator/src/ch/datahouse/HockeyMatch.java
 * 
 * Description:
 * POJO (Plain Old Java Object), Containerklasse für ein Hockey Match, der mehrere Live Trickers haben kann. 
 */
package ch.datahouse;

import java.util.ArrayList;

public class HockeyMatch {
	
	private ArrayList<LiveTicker> livetickers = null;
	
	
	public HockeyMatch() {
		this.livetickers = new ArrayList<LiveTicker>();
	}
	
	public ArrayList<LiveTicker> getLiveTickers() {
		return this.livetickers;
	}
	
	public LiveTicker getLiveTicker(int number) {
		return this.livetickers.get(number);
	}
	
	public void addLiveTicker(LiveTicker liveticker) {
		this.livetickers.add(liveticker);
	}
	
	public int LiveTickerSize() {
		return livetickers.size();
	}
	
}
