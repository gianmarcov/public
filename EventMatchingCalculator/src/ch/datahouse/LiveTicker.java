/* Copyright (C) Vitelli Gianmarco - All Rights Reserved
 *
 * Written by Vitelli Gianmarco <gianmarco.vitelli@gmail.com>, 21:05 19 Februar 2015
 * 
 * File:
 * /EventMatchingCalculator/src/ch/datahouse/LiveTicker.java
 * 
 * Description:
 * POJO (Plain Old Java Object), Containerklasse für die LiveTickers die mehrere Events haben kann.
 */
package ch.datahouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LiveTicker {
	
	private ArrayList<GoalEvent> goalevents = null;
	private short goals = 0;
	
	public LiveTicker() {
		this.goalevents = new ArrayList<GoalEvent>();
	}
	
	public LiveTicker(byte goals) {
		this.goalevents = new ArrayList<GoalEvent>();
		this.goals = goals;
	}
	
	public ArrayList<GoalEvent> getGoalEvents() {
		return this.goalevents;
	}
	
	public int getGoalEventsSize() {
		return this.goalevents.size();
	}
	
	public GoalEvent getGoalEvent(int number) {
		return this.goalevents.get(number);
	}
	
	public void addGoalEvent(GoalEvent goalevent) {
		this.goalevents.add(goalevent);
	}
	
	public void addAllGoalEvent(ArrayList<GoalEvent> goalevents) {
		this.goalevents.addAll(goalevents);
	}
	
	public void sortGoalEvent() {
		Collections.sort(goalevents, new Comparator<GoalEvent>() {
			@Override
			public int compare(GoalEvent o1, GoalEvent o2) {
				 return Float.compare(o1.getSecound(), o2.getSecound());
			}
		});
	}
	
	public void setGoals(short number) {
		this.goals = number;
	}
	
	public short getGoals() {
		return this.goals;
	}
}
