package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EcouteurDeSouris extends MouseAdapter {
    private final Color DEFAULT_COLOR = new Color(89, 60, 28);
    private final Color HOVER_COLOR = new Color (120, 85, 45);
    private final Color PRESS_COLOR = new Color (70, 45, 20);
    private final JButton button;

    public EcouteurDeSouris(JButton button) {
        this.button = button;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        button.setBackground(DEFAULT_COLOR);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited (MouseEvent e) {
        button.setBackground(DEFAULT_COLOR);
        button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mousePressed (MouseEvent e) {
        button.setBackground(PRESS_COLOR);
    }

    @Override
    public void mouseReleased (MouseEvent e) {
        button.setBackground(DEFAULT_COLOR);
    }
    
}