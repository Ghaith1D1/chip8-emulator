import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class Display extends JPanel {
    private boolean[] pixels = new boolean[64 * 32];
    private final int SCALE = 10; // Jeder CHIP-8 Pixel wird 10x10 groß gezeichnet

    // Wird von der Game Loop aufgerufen, wenn sich das Bild ändert
    public void updateDisplay(boolean[] newPixels) {
        this.pixels = newPixels;
        repaint(); // Sagt Java, dass das Panel neu gezeichnet werden soll
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Hintergrund schwarz malen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 64 * SCALE, 32 * SCALE);

        // Weiße Pixel.
        g.setColor(Color.GREEN);
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 64; x++) {
                if (pixels[x + (y * 64)]) {
                    g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
                }
            }
        }
    }
}