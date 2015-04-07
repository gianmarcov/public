/* Copyright (C) Vitelli Gianmarco - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Vitelli Gianmarco <gianmarco.vitelli@gmail.com>, 21:05 19 Februar 2015
 * 
 * File:
 * /EventMatchingCalculator/src/ch/datahouse/EventReader.java
 * 
 * Description:
 * Diese Klasse liesst die gewünschte Datei ein und generiert ein Object Model im Arbeitsspeicher.
 * 
 * Speziell:
 * Die Klasse kann mehr als nur zwei LiveTicker einlesen, indem in der ersten Zeile mehrere Zahlen mit der Leertaste
 * voneinander getrennt worden und der Grösse nach sortiert sind. Da ansonsten die Berechnung nicht stimmt.
 * 
 * Bespiel:
 * 
 * richtig:
 * 5 4 3 2 1
 * falsch:
 * 1 5 2 1 2
 * 
 * Berechnung:
 * N M O P Q
 * 5 4 3 2 1
 * 
 * Formel:
 * 
 * N+M+1 = Start Live Tricker 2 Quelle
 * 
 * Beispiel Berechnung:
 * 5+1 = 6 Start Live Tricker 1 Quelle
 * 10+1 = 11 Start Live Tricker 2 Quelle
 * 13+1 = 14 Start Live Tricker 3 Quelle
 * 
 * Ausnahmebehandlung:
 * Es wird davon ausgegangen, dass die Daten richtig in die Datei geschrieben wurden, wenn nicht wird eine Fehlermeldung ausgegeben
 * und das Programm bricht ab.
 */
package ch.datahouse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class EventReader {

	private final short DEFAULT_BUFFER_SIZE = 4096;
	private String filepath = null;
	
	public EventReader(String filepath) {
		this.filepath = filepath;
	}
	
	public HockeyMatch read() {
		HockeyMatch hockeymatch = null;
		
		try {
			hockeymatch = parse(readFile());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("");
			System.exit(1);
		}
		
		return hockeymatch;
	}
	
	private String readFile() throws IOException {
		String data = "";
		File file = new File(filepath);
		
		if(!file.exists()) {
			//Schliesse das Programm, wenn die angegebene Datei nicht existiert, return code 1
			System.out.println("");
			System.exit(1);
		}else {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int n = 0;
			
				InputStream input = new FileInputStream(file);
				ByteArrayOutputStream output = new ByteArrayOutputStream();
			
				while ((n = input.read(buffer))>=0) {
					output.write(buffer, 0, n);
				}
				
				data = new String(output.toByteArray());
				input.close();
		}
		return data;
	}
	
	private HockeyMatch parse(String data) throws Exception {
		HockeyMatch hockeymatch = new HockeyMatch();
		String[] separated = data.split("\n");
		String[] goals =  separated[0].split(" "); // Länge des Array's ergibt die Anzahl LiveTicker
		int[] liveticker_separator_pointer = new int[goals.length]; // 
		
		/* Lese die Anzahl Tore der LiveTicker und instanziiere die Objekte */
		for(int i=0,n=0,r=1; i < goals.length; i++) { // n = number, r = reminder
			if ((n = Integer.parseInt(goals[i])) <= 100 && n >= 0 ) {
				hockeymatch.addLiveTicker(new LiveTicker((byte) Integer.parseInt(goals[i])));
				r += n;
				liveticker_separator_pointer[i] = r;
			}else {
				throw new Exception("Die gesamte Anzahl Tore darf nicht kleiner als 0 und nicht über 100 sein!");
			}
		}
		
		/* Lese die einzelne Tore (Role Heim oder Gast und die Sekunden) N+LiveTicker */
		for(int i=0; i < goals.length; i++) {
			String[] values = separated[i+1].split(" ");
			
			if(values[0].equals("H") || values[0].equals("G")) {
				boolean heim = (values[0].equals("H")) ? true : false;
				float secound = Float.parseFloat(values[1]);
				hockeymatch.getLiveTicker(i).addGoalEvent(new GoalEvent(heim, secound));
			}else {
				throw new Exception("Nur die Buchstaben H (Heim) oder G (Gast) sind erlaubt");
			}
		}
		
		/* Lese die übrige Tore (Role Heim oder Gast und die Sekunden) berechne (N+M..Anzahl LiveTicker)+1 */
		int remaining_lines_count = (separated.length - (goals.length + 1));
		
		for(int i=remaining_lines_count,n=0; i < separated.length; i++) {
			String[] values = separated[i].split(" ");
			
			if(values[0].equals("H") || values[0].equals("G")) {
				boolean heim = (values[0].equals("H")) ? true : false;
				float secound = Float.parseFloat(values[1]);
				hockeymatch.getLiveTicker(n).addGoalEvent(new GoalEvent(heim, secound));
			}else {
				throw new Exception("Nur die Buchstaben H (Heim) oder G (Gast) sind erlaubt");
			}
			
			if(i == liveticker_separator_pointer[n]) {
				n++;
			}
		}	
		
		return hockeymatch;
	}
}
