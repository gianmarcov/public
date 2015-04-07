Public project repository of Vitelli Gianmarco.

All projects are under the MIT license, if you have a question, don't hesitate to ask me (gianmarco.vitelli@gmail.com).
<h1>Projects</h1>
<h2>Lösung EVENT MATCHING</h2><br/>
<br/>
CHALLENGE 3: EVENT MATCHING<br/>
<br/>
Von zwei verschiedenen Live-Tickern, sind die Tore eines Eishockey-Spiels bekannt. Beide Quellen sind fehlerhaft (das heisst Tore fehlen, bzw. die Zeiten sind ungenau; es darf davon ausgegangen weren, dass keine Tore zu viel angegeben werden) und die Daten sollen ergänzend genutzt werden, um eine möglichst gute Datenqualität zu erreichen<br/>.
Die benötigten Angaben werden aus dem File events.in gelesen, das wie folgt aufgebaut ist:<br/>
<br/>
Erste Zeile; Anzahl gemeldeter Tore: "Tore Quelle 1 (N)" "Tore Quelle 2 (M)" (beides Integer im Bereich 1 − 100)<br/>
Zeile 2 - N+1; einzelne Tore, Quelle 1: "Team" "Zeitpunkt" (Team ist entweder "H" oder (Heim), "G" (Gast); der Zeitpunkt ist in Sekunden (Dezimalzahl) angegeben)<br/>
Zeile N+2 - N+M+1; einzelne Tore, Quelle 2: "Team" "Zeitpunkt" (Details wie zuvor)<br/>
Als Ausgabe sollen die tatsächliche gefallenen Tore (Anzahl in der ersten Zeile) in der richtigen Reihenfolge mit Team und Zeitpunkt, an dem sie gefallen sind, ins File events.out geschrieben werden.<br/>

Beispiel-Eingabe (events.in)<br/>
<br/>
3 2<br/>
G 32.1<br/>
G 100.5<br/>
H 60.4<br/>
G 35.3<br/>
H 65.4<br/>
<br/>
Beispiel-Ausgabe (events.out)<br/>
<br/>
3<br/>
G 33.7<br/>
H 62.9<br/>
G 100.5<br/>

Copyright (C) Vitelli Gianmarco - All Rights Reserved<br/>
