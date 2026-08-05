import java.util.Arrays;

public class CPU {
    // Speicher und Register
    private byte[] ram = new byte[4096];
    private byte[] v = new byte[16];
    private int i;
    private int pc = 0x200;
    private int[] stack = new int[16];
    private int sp = 0;

    // Timer und Display-Zustand
    public byte delayTimer;
    public byte soundTimer;
    public boolean[] display = new boolean[64 * 32];
    public boolean drawFlag = false;

    // Vollständiges Fontset (0 bis F)
    private final byte[] fontset = {
            (byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xF0, // 0
            (byte) 0x20, (byte) 0x60, (byte) 0x20, (byte) 0x20, (byte) 0x70, // 1
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // 2
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 3
            (byte) 0x90, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0x10, // 4
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 5
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 6
            (byte) 0xF0, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x40, // 7
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 8
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 9
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0x90, // A
            (byte) 0xE0, (byte) 0x90, (byte) 0xE0, (byte) 0x90, (byte) 0xE0, // B
            (byte) 0xF0, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0xF0, // C
            (byte) 0xE0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xE0, // D
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // E
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0x80  // F
    };

    public CPU() {
        // Lade Fontset in den Speicherbereich 0x000 bis 0x1FF
        for (int j = 0; j < fontset.length; j++) {
            ram[j] = fontset[j];
        }
    }

    public void loadProgram(byte[] program) {
        for (int j = 0; j < program.length; j++) {
            ram[0x200 + j] = program[j];
        }
    }

    // Die CPU-Schleife
    public void cycle(boolean[] keys) {
        // Fetch: 2 Bytes lesen
        int opcode = ((ram[pc] & 0xFF) << 8) | (ram[pc + 1] & 0xFF);

        // Nützliche Variablen für die Dekodierung (x, y, n, nn, nnn)
        int x = (opcode & 0x0F00) >> 8;
        int y = (opcode & 0x00F0) >> 4;
        int nn = opcode & 0x00FF;
        int nnn = opcode & 0x0FFF;

        // Decode & Execute
        switch (opcode & 0xF000) {
            case 0x0000:
                switch (opcode & 0x00FF) {
                    case 0x00E0: // 00E0: Display löschen
                        Arrays.fill(display, false);
                        drawFlag = true;
                        pc += 2;
                        break;
                    case 0x00EE: // 00EE: Return aus Subroutine
                        sp--;
                        pc = stack[sp];
                        pc += 2;
                        break;
                    default:
                        System.out.println("Unbekannter 0-Opcode: " + Integer.toHexString(opcode));
                        pc += 2;
                }
                break;

            case 0x1000: // 1NNN: Jump
                pc = nnn;
                break;

            case 0x2000: // 2NNN: Subroutine aufrufen
                stack[sp] = pc;
                sp++;
                pc = nnn;
                break;

            case 0x3000: // 3XNN: Skip next wenn VX == NN
                if ((v[x] & 0xFF) == nn) pc += 4;
                else pc += 2;
                break;

            case 0x4000: // 4XNN: Skip next wenn VX != NN
                if ((v[x] & 0xFF) != nn) pc += 4;
                else pc += 2;
                break;

            case 0x5000: // 5XY0: Skip next wenn VX == VY
                if (v[x] == v[y]) pc += 4;
                else pc += 2;
                break;

            case 0x6000: // 6XNN: Setze Register VX auf den Wert NN
                v[x] = (byte) nn;
                pc += 2;
                break;

            case 0x7000: // 7XNN: Addiere NN zu VX (ohne Carry-Flag)
                v[x] = (byte) ((v[x] & 0xFF) + nn);
                pc += 2;
                break;

            case 0x8000: // Arithmetische und logische Operationen
                int vx = v[x] & 0xFF;
                int vy = v[y] & 0xFF;
                switch (opcode & 0x000F) {
                    case 0x0000: v[x] = (byte) vy; break;
                    case 0x0001: v[x] = (byte) (vx | vy); break;
                    case 0x0002: v[x] = (byte) (vx & vy); break;
                    case 0x0003: v[x] = (byte) (vx ^ vy); break;
                    case 0x0004: // ADD
                        v[x] = (byte) (vx + vy);
                        v[0xF] = (byte) ((vx + vy) > 255 ? 1 : 0);
                        break;
                    case 0x0005: // SUB (mit Borrow)
                        v[x] = (byte) (vx - vy);
                        v[0xF] = (byte) (vx >= vy ? 1 : 0);
                        break;
                    case 0x0006: // Shift Right
                        v[x] = (byte) (vx >> 1);
                        v[0xF] = (byte) (vx & 0x1);
                        break;
                    case 0x0007: // SUBN
                        v[x] = (byte) (vy - vx);
                        v[0xF] = (byte) (vy >= vx ? 1 : 0);
                        break;
                    case 0x000E: // Shift Left
                        v[x] = (byte) (vx << 1);
                        v[0xF] = (byte) ((vx >> 7) & 0x1);
                        break;
                }
                pc += 2;
                break;

            case 0x9000: // 9XY0: Skip next wenn VX != VY
                if (v[x] != v[y]) pc += 4;
                else pc += 2;
                break;

            case 0xA000: // ANNN: Setze Register I
                i = nnn;
                pc += 2;
                break;

            case 0xC000: // CXNN: Zufallszahl
                int random = (int) (Math.random() * 256);
                v[x] = (byte) (random & nn);
                pc += 2;
                break;

            case 0xD000: // DXYN: Zeichne Sprite (KORRIGIERT FÜR CLIPPING & KOLLISION)
                int xPos = (v[x] & 0xFF) % 64;
                int yPos = (v[y] & 0xFF) % 32;
                int height = opcode & 0x000F;

                v[0xF] = 0; // Kollisions-Flag zurücksetzen

                for (int yLine = 0; yLine < height; yLine++) {
                    int pixelData = ram[i + yLine];

                    for (int xLine = 0; xLine < 8; xLine++) {
                        if ((pixelData & (0x80 >> xLine)) != 0) {
                            int xCoord = xPos + xLine;
                            int yCoord = yPos + yLine;

                            // Clipping: Was über den Rand geht, abschneiden, nicht rüber-wrappen!
                            if (xCoord >= 64 || yCoord >= 32) continue;

                            int index = xCoord + (yCoord * 64);

                            if (display[index]) {
                                v[0xF] = 1;
                            }
                            display[index] ^= true;
                        }
                    }
                }
                drawFlag = true;
                pc += 2;
                break;

            case 0xE000: // Tastatur-Eingaben
                switch (opcode & 0x00FF) {
                    case 0x009E: // EX9E: Skip wenn Taste in VX gedrückt ist
                        if (keys[v[x] & 0xF]) pc += 4;
                        else pc += 2;
                        break;
                    case 0x00A1: // EXA1: Skip wenn Taste in VX NICHT gedrückt ist
                        if (!keys[v[x] & 0xF]) pc += 4;
                        else pc += 2;
                        break;
                }
                break;

            case 0xF000: // Timer, Audio, Speicher, BCD
                switch (opcode & 0x00FF) {
                    case 0x0007: // FX07: Setze VX auf den Wert des Delay Timers
                        v[x] = delayTimer;
                        break;
                    case 0x0015: // FX15: Setze den Delay Timer auf den Wert von VX
                        delayTimer = v[x];
                        break;
                    case 0x0018: // FX18: Setze den Sound Timer auf den Wert von VX
                        soundTimer = v[x];
                        break;
                    case 0x001E: // FX1E: Addiere VX zu I
                        i += (v[x] & 0xFF);
                        break;
                    case 0x0029: // FX29: Setze Index-Register auf das Sprite in VX (Schriftzeichen)
                        i = (v[x] & 0x0F) * 5;
                        break;
                    case 0x0033: // FX33: BCD-Repräsentation speichern
                        int value = v[x] & 0xFF;
                        ram[i] = (byte) (value / 100);
                        ram[i + 1] = (byte) ((value / 10) % 10);
                        ram[i + 2] = (byte) (value % 10);
                        break;
                    case 0x0055: // FX55: Register V0 bis VX in Speicher ab Adresse I ablegen
                        for (int j = 0; j <= x; j++) {
                            ram[i + j] = v[j];
                        }
                        break;
                    case 0x0065: // FX65: Register V0 bis VX aus Speicher ab Adresse I füllen
                        for (int j = 0; j <= x; j++) {
                            v[j] = ram[i + j];
                        }
                        break;
                    default:
                        System.out.println("Unbekannter F-Opcode: " + Integer.toHexString(opcode));
                }
                pc += 2;
                break;

            default:
                System.out.println("Unbekannter Opcode: " + Integer.toHexString(opcode));
                pc += 2;
        }
    }
}