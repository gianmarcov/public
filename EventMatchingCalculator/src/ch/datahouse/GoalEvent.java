/* Copyright (C) Vitelli Gianmarco - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Vitelli Gianmarco <gianmarco.vitelli@gmail.com>, 21:05 19 Februar 2015
 * 
 * File:
 * /EventMatchingCalculator/src/ch/datahouse/GoalEvent.java
 * 
 * Description:
 * POJO (Plain Old Java Object), Containerklasse die den Event behält.
 */
package ch.datahouse;

public class GoalEvent {
	private boolean home; //true = Heim, false = Gast
	private float secound;
	
	public GoalEvent(boolean home, float secound) {
		this.home = home;
		this.secound = secound;
	}
	
	public boolean isHome() {
		return this.home;
	}
	
	public float getSecound() {
		return this.secound;
	}

}
