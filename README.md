/* Copyright (C) Vitelli Gianmarco - All Rights Reserved
 * Written by Vitelli Gianmarco <gianmarco.vitelli@gmail.com>, 21:05 19 Februar 2015
/*

Lösung zur CHALLENGE EVENT MATCHING /EVENTMATCHING

CHALLENGE 3: EVENT MATCHING

Von zwei verschiedenen Live-Tickern, sind die Tore eines Eishockey-Spiels bekannt. Beide Quellen sind fehlerhaft (das heisst Tore fehlen, bzw. die Zeiten sind ungenau; es darf davon ausgegangen weren, dass keine Tore zu viel angegeben werden) und die Daten sollen ergänzend genutzt werden, um eine möglichst gute Datenqualität zu erreichen.
Die benötigten Angaben werden aus dem File events.in gelesen, das wie folgt aufgebaut ist:

Erste Zeile; Anzahl gemeldeter Tore: "Tore Quelle 1 (N)" "Tore Quelle 2 (M)" (beides Integer im Bereich 1 − 100)

Zeile 2 - N+1; einzelne Tore, Quelle 1: "Team" "Zeitpunkt" (Team ist entweder "H" oder (Heim), "G" (Gast); der Zeitpunkt ist in Sekunden (Dezimalzahl) angegeben)

Zeile N+2 - N+M+1; einzelne Tore, Quelle 2: "Team" "Zeitpunkt" (Details wie zuvor)

Als Ausgabe sollen die tatsächliche gefallenen Tore (Anzahl in der ersten Zeile) in der richtigen Reihenfolge mit Team und Zeitpunkt, an dem sie gefallen sind, ins File events.out geschrieben werden.

Beispiel-Eingabe (events.in)

3 2
G 32.1
G 100.5
H 60.4
G 35.3
H 65.4

Beispiel-Ausgabe (events.out)

3
G 33.7
H 62.9
G 100.5
