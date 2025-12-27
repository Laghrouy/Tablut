package Vue;

import Model.Piece;
import javax.swing.*;
import java.awt.*;

public class CaseUI extends JButton {
    private static final int Taille = 88;
    private Piece piece;
    private boolean highlight = false;

    public CaseUI() {
        setPreferredSize(new Dimension(Taille, Taille));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        repaint();
    }

    public void sethighlight(boolean highlight) {
        this.highlight = highlight;
        setBorder(BorderFactory.createLineBorder(highlight ? Color.RED : Color.BLACK));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (piece != null) {
            switch (piece) {
                case ROI -> {
                    g.setColor(Color.YELLOW);
                    g.fillOval(10, 10, Taille-20, Taille-20);
                }
                case DEFENSEUR -> {
                    g.setColor(Color.WHITE);
                    g.fillOval(10, 10, Taille-20, Taille-20);
                }
                case ATTAQUANT -> {
                    g.setColor(Color.BLACK);
                    g.fillOval(10, 10, Taille-20, Taille-20);
                }
            }
        }
    }
}