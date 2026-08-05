import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keypad implements KeyListener {

    public boolean[] keys = new boolean[16];

    @Override
    public void keyPressed(KeyEvent e) {
        setKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        setKey(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private void setKey(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_1: keys[0x1] = pressed; break;
            case KeyEvent.VK_2: keys[0x2] = pressed; break;
            case KeyEvent.VK_3: keys[0x3] = pressed; break;
            case KeyEvent.VK_4: keys[0xC] = pressed; break;

            case KeyEvent.VK_Q: keys[0x4] = pressed; break;
            case KeyEvent.VK_W: keys[0x5] = pressed; break;
            case KeyEvent.VK_E: keys[0x6] = pressed; break;
            case KeyEvent.VK_R: keys[0xD] = pressed; break;

            case KeyEvent.VK_A: keys[0x7] = pressed; break;
            case KeyEvent.VK_S: keys[0x8] = pressed; break;
            case KeyEvent.VK_D: keys[0x9] = pressed; break;
            case KeyEvent.VK_F: keys[0xE] = pressed; break;

            case KeyEvent.VK_Y: keys[0xA] = pressed; break;
            case KeyEvent.VK_X: keys[0x0] = pressed; break;
            case KeyEvent.VK_C: keys[0xB] = pressed; break;
            case KeyEvent.VK_V: keys[0xF] = pressed; break;
        }
    }
}