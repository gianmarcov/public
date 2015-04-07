/* Copyright (C) Vitelli Gianmarco - All Rights Reserved
 *
 * Written by Vitelli Gianmarco <gianmarco.vitelli@gmail.com>, 21:05 19 Februar 2015
 * 
 * File:
 * /EventMatchingCalculator/src/ch/datahouse/MatchWriter.java
 * 
 * Description:
 * Diese Klasse schreibt das Datenmodel formatiert in die gewünschte Datei.
 */
package ch.datahouse;

import java.io.PrintWriter;
import java.text.DecimalFormat;

public class MatchWriter {
	
	private String filepath = null;
	
	public MatchWriter(String filepath) {
		this.filepath = filepath;
	}
	
	/* (Über-)Schreibe das Hockey Match in die gewünschte Datei */
	public void writeFile(HockeyMatch output) {
		try {
			PrintWriter writer = new PrintWriter(filepath, "UTF-8");
			
			LiveTicker liveticker = output.getLiveTicker(0);
			writer.println(liveticker.getGoals());
			
			for(GoalEvent goalevent : liveticker.getGoalEvents()) {
				writer.println((goalevent.isHome() ? 'H': 'G')+" "+new DecimalFormat("00.0").format(goalevent.getSecound()));
			}
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
