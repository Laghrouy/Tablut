package Vue;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import Model.Piece;
import Model.TypeJoueur;

public class Pions extends JPanel {
    private Image image;

    public Pions(Piece type, TypeJoueur camp) {
        setOpaque(false);
        setPreferredSize(new Dimension(100, 100));
        ChargerImage(type, camp);
    }

    private void ChargerImage(Piece type, TypeJoueur camp) {
        try {
            String imagePath = "Images/"; // res/Images/
            switch (type) {
                case ROI:
                    imagePath += "roi2.png";
                    break;
                case DEFENSEUR:
                    imagePath += (camp == TypeJoueur.GARDE) ? "blanc2.png" : "noir2.png";
                    break;
                case ATTAQUANT:
                    imagePath += (camp == TypeJoueur.MERCENAIRE) ? "noir2.png" : "blanc2.png";
                    break;
                default:
                    imagePath = null; // Pas d'image pour les cases vides
            }

            if (imagePath != null) {
                image = ImageIO.read(new File(imagePath));
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement image: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int x = (getWidth() - image.getWidth(null)) / 2;
            int y = (getHeight() - image.getHeight(null)) / 2;
            g.drawImage(image, x, y, this);
        }
    }
}