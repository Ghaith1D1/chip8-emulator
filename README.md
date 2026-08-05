# 🕹️ CHIP-8 Emulator

##  Kurzbeschreibung
Das Programm ist ein Emulator (Interpreter) für die historische virtuelle Spielkonsole **CHIP-8** aus den 1970er Jahren.

Das Programm ist in der Lage:
* Originale CHIP-8 ROM-Dateien (z. B. klassische Spiele wie *Pong* oder *Tetris*) binär einzulesen.
* Die darin enthaltenen Bytecode-Instruktionen mithilfe einer emulierten CPU auszuführen.
* Das Spielgeschehen visuell darzustellen.

##  Benutzungsbeispiel
Der Benutzer startet die Anwendung über die Benutzeroberfläche oder das Terminal und übergibt dabei als Argument den Pfad zu einer CHIP-8 ROM-Datei:

```bash
java -jar emulator.jar roms/pong.ch8
``` 

Ablauf:

 * Das Programm validiert die Datei und lädt sie in den emulierten Arbeitsspeicher.

* Es öffnet sich ein Anwendungsfenster, das das monochrome Spielfeld anzeigt.

* Der Benutzer kann nun das Spiel in Echtzeit steuern.


Dabei werden die Tasten auf das historische 16-Tasten-Hex-Keypad über die PC-Tastatur gemappt:


    1 2 3 4  
    Q W E R  
    A S D F  
    Y X C V  

Das Spiel läuft flüssig, bis der Benutzer das Fenster schließt.

## ROMs beziehen

Die benötigten CHIP-8 ROMs, die man mit diesem Emulator nutzen kann, findet man im folgenden Archiv:
[CHIP-8 ROM Archive](https://johnearnest.github.io/chip8Archive/)

**Hinweis:** Ich habe bereits ein paar spielbare ROMs im Ordner `roms/` mitgeladen.