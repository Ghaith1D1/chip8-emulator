import javax.swing.JFrame;

public class EmulatorMain {
    public static void main(String[] args) {
        // Prüfen, ob ein Argument übergeben wurde
        if (args.length < 1) {
            System.out.println("ROM-Pfad angeben.");
            return;
        }

        // 1. Komponenten initialisieren
        CPU cpu = new CPU();
        RomLoader loader = new RomLoader();
        Keypad keypad = new Keypad();
        Display display = new Display();

        // 2. ROM laden und an die CPU übergeben
        byte[] rom = loader.load(args[0]);
        cpu.loadProgram(rom);

        // 3. Swing-Fenster konfigurieren
        JFrame frame = new JFrame("CHIP-8 Emulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640 + 16, 320 + 39); // Bisschen Puffer für Fensterrahmen
        frame.add(display);
        frame.addKeyListener(keypad); // Tastatur anbinden
        frame.setResizable(false);
        frame.setVisible(true);

        // 4. Game Loop
        while (true) {

            for (int k = 0; k < 15; k++) {
                cpu.cycle(keypad.keys);
            }


            if (cpu.drawFlag) {
                display.updateDisplay(cpu.display);
                cpu.drawFlag = false;
            }

            if (cpu.delayTimer > 0) cpu.delayTimer--;
            if (cpu.soundTimer > 0) {
                if (cpu.soundTimer == 1)
                    cpu.soundTimer--;
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}