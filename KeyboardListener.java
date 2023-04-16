import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.util.ArrayList;

public class KeyboardListener extends JComponent implements KeyListener {
    private ArrayList<Integer> keys;
    
    /**
     * Initializes instance variables
     */
    public KeyboardListener() {
        keys = new ArrayList<Integer>();
    }
    
    // Required by KeyListener abstract class
    public void keyTyped(KeyEvent e) {}

    /**
     * Add key to array when pressed down, but only if it isn't already in the array
     */
    public void keyPressed(KeyEvent e) {
        if (!keys.contains(e.getKeyCode())) {
            keys.add(e.getKeyCode());
        }
    }

    /**
     * Remove key from array when key is released
     */
    public void keyReleased(KeyEvent e) {
        keys.remove(keys.indexOf(e.getKeyCode()));
    }

    // Getter
    public ArrayList<Integer> getKeys() {
        return keys;
    }
}
