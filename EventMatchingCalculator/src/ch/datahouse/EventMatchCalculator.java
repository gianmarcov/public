/* Copyright (C) Vitelli Gianmarco - All Rights Reserved
 *
 * Written by Vitelli Gianmarco <gianmarco.vitelli@gmail.com>, 21:05 19 Februar 2015
 * 
 * File:
 * /EventMatchingCalculator/src/ch/datahouse/EventMatchCalculator.java
 * 
 * Description:
 * Berechnet aus den fehlerhaften Daten, die möglichst gute Datenqualität, indem es den Mittelwert berechnet.
 */

package ch.datahouse;

import java.util.ArrayList;

public class EventMatchCalculator {

	public EventMatchCalculator() {
	}
	
	public HockeyMatch CalculateMatch(HockeyMatch input) {
		HockeyMatch output = new HockeyMatch();
		float r=0;
		
		/* Suche in jeden einzelnen Liveticker nach Fehler und korrigiere diese. Berechne aus den zwei gleichen Zahlen den Mittelwert aus */
		for(LiveTicker liveticker : input.getLiveTickers()) {
			liveticker.sortGoalEvent();
			ArrayList<GoalEvent> goalevents = liveticker.getGoalEvents();
			for(int i=1; i < goalevents.size();i++) {
				GoalEvent goalevents1 = goalevents.get(i);
				GoalEvent goalevents2 = goalevents.get(i-1);
				
				r = (goalevents1.getSecound() - goalevents2.getSecound());
				if(r > -10 && r < 10 && goalevents1.isHome() == goalevents2.isHome()) {
					liveticker.addGoalEvent(new GoalEvent(goalevents1.isHome(), ((goalevents1.getSecound() + goalevents2.getSecound()) / 2)));
					goalevents.remove(i);
					goalevents.remove(i-1);
					liveticker.sortGoalEvent();
					i=1; // Zwinge die Schleife von neu zu durchgehen da das Array sich verändert hat!
				}
			}
		}
		
		/* Suche in allen Liveticker nach Fehler und korrigiere diese. Berechne aus den zwei gleichen Zahlen den Mittelwert aus */
		for(LiveTicker liveticker1 : input.getLiveTickers()) {
			for(LiveTicker liveticker2 : input.getLiveTickers()) {
				/* Überspringe das gleiche Objekt */
				if(liveticker1 == liveticker2) {
					continue;
				}
				 ArrayList<GoalEvent> goalevents1 = liveticker1.getGoalEvents();
				 ArrayList<GoalEvent> goalevents2 = liveticker2.getGoalEvents();
				 for(int i=0; i < goalevents1.size();i++) {
					 for(int c=0; c < goalevents2.size();c++) {
						 GoalEvent goalevent1 = goalevents1.get(i);
						 GoalEvent goalevent2 = goalevents2.get(c);
						 r = (goalevent1.getSecound() - goalevent2.getSecound());
							if(r > -10 && r < 10 && goalevent1.isHome() == goalevent2.isHome()) {		
								liveticker1.addGoalEvent(new GoalEvent(goalevent1.isHome(), ((goalevent1.getSecound() + goalevent2.getSecound()) / 2)));
								goalevents1.remove(i);
								goalevents2.remove(c);
								liveticker1.sortGoalEvent();
								i=1; // Zwinge die Schleife von neu zu durchgehen, da das Array sich verändert hat!
								c=1;
								break;
							}
					 }
				 }
			}	
		}
		
		/* Erstelle ein neues Hockeymatch, benutze die alten Objekt Referenzen */
		LiveTicker liveticker = new LiveTicker();
		for(LiveTicker oldliveticker : input.getLiveTickers()) {
			liveticker.addAllGoalEvent(oldliveticker.getGoalEvents());
		}
		
		/* Lese wie viele GoalEvents gesetzt worden sind und übernehme die Anzahl als Goals */
		liveticker.setGoals((short) liveticker.getGoalEventsSize());
		output.addLiveTicker(liveticker);
		
		return output;
	}
}
