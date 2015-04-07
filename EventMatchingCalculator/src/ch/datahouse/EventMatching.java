/* Copyright (C) Vitelli Gianmarco - All Rights Reserved
 *
 * Written by Vitelli Gianmarco <gianmarco.vitelli@gmail.com>, 21:05 19 Februar 2015
 * 
 * File:
 * /EventMatchingCalculator/src/ch/datahouse/EventMatching.java
 * 
 * CHALLENGE 3: EVENT MATCHING
 * 	Von zwei verschiedenen Live-Tickern, sind die Tore eines Eishockey-Spiels bekannt. Beide Quellen sind fehlerhaft (das heisst Tore fehlen, bzw. die Zeiten sind ungenau; es darf davon ausgegangen weren, dass keine Tore zu viel angegeben werden) und die Daten sollen ergänzend genutzt werden, um eine möglichst gute Datenqualität zu erreichen.
 * 	Die benötigten Angaben werden aus dem File events.in gelesen, das wie folgt aufgebaut ist:
 * 	Erste Zeile; Anzahl gemeldeter Tore: "Tore Quelle 1 (N)" "Tore Quelle 2 (M)" (beides Integer im Bereich 1 − 100)
 * 	Zeile 2 - N+1; einzelne Tore, Quelle 1: "Team" "Zeitpunkt" (Team ist entweder "H" oder (Heim), "G" (Gast); der Zeitpunkt ist in Sekunden (Dezimalzahl) angegeben)
 * 	Zeile N+2 - N+M+1; einzelne Tore, Quelle 2: "Team" "Zeitpunkt" (Details wie zuvor)
 * 	Als Ausgabe sollen die tatsächliche gefallenen Tore (Anzahl in der ersten Zeile) in der richtigen Reihenfolge mit Team und Zeitpunkt, an dem sie gefallen sind, ins File events.out geschrieben werden.
 * 
 * Description:
 *  Die Main Klasse verknüpft alle Klassen und gibt den Ablauf, die Applikation kann durch das Terminal mit dem Befehl
 *  java -jar Programmname (Pfad der zu lesende Datei) (Pfad der zu schreibende Datei) aufgerufen werden.
 *  java -jar EventMatchingCalculator.jar ./Daten/events.in ./Daten/events.out
 */
package ch.datahouse;

public class EventMatching {
	
	// java -jar Programmname.jar (Pfad der zu lesende Datei) (Pfad der zu schreibende Datei)
	public static void main(String[] args) {
		String inputfilepath = args[0];
		String outputfilepath = args[1];
		
		EventMatchCalculator eventmatchcalculator = new EventMatchCalculator();
		EventReader eventreader = new EventReader(inputfilepath);
		MatchWriter matchwriter = new MatchWriter(outputfilepath);
		
		matchwriter.writeFile(eventmatchcalculator.CalculateMatch(eventreader.read()));
	}
}
