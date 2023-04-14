import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.util.ArrayList;

public class KeyboardListener extends JComponent implements KeyListener {
    private ArrayList<Integer> keys;
    
    public KeyboardListener() {
        keys = new ArrayList<Integer>();
    }
    
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if (!keys.contains(e.getKeyCode())) {
            keys.add(e.getKeyCode());
        }
    }

    public void keyReleased(KeyEvent e) {
        keys.remove(keys.indexOf(e.getKeyCode()));
    }

    public ArrayList<Integer> getKeys() {
        return keys;
    }
}
