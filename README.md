# 🕹️ CHIP-8 Emulator

## Short Description

This program is an emulator (interpreter) for the historic virtual game console **CHIP-8** from the 1970s.

The program is capable of:
* Reading original CHIP-8 ROM files (e.g., classic games like *Pong* or *Tetris*) in binary format.
* Executing the contained bytecode instructions using an emulated CPU.
* Rendering the gameplay visually.

## Usage Example

The user starts the application via the user interface or the terminal, passing the path to a CHIP-8 ROM file as an argument:

```bash
java -jar emulator.jar roms/pong.ch8
``` 

Execution Flow:

 * The program validates the file and loads it into the emulated memory.
* An application window opens, displaying the monochrome playfield.

* The user can now control the game in real-time.


The keys are mapped to the historical 16-key hex keypad via the PC keyboard:

    1 2 3 4  
    Q W E R  
    A S D F  
    Y X C V  

The game runs smoothly until the user closes the window.
## Getting ROMs

The required CHIP-8 ROMs compatible with this emulator can be found in the following archive:
[CHIP-8 ROM Archive](https://johnearnest.github.io/chip8Archive/)

**Note:** I have already included a few playable ROMs in the `roms/` directory.
