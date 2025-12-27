package Vue;

import Controleur.GameInitializer;
import Controleur.ControlerJeu;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Menu {
    private JFrame frame;
    private BufferedImage ImageFond;

    public Menu() {
        frame = new JFrame("Jeu de Tablut - Menu Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1985, 1090);
        frame.setLocationRelativeTo(null);
        createMenu();
        frame.setVisible(true);
    }

    private void createMenu() {
        try {
            ImageFond = ImageIO.read(new File("Images/fondmenu.png")); // res/Images/fondmenu.png
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image de fond : " + e.getMessage());
        }

        JPanel menuPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (ImageFond != null) {
                    g.drawImage(ImageFond, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        menuPanel.setOpaque(false);

        // Titre
        JLabel title = new JLabel("TABLUT");
        title.setFont(new Font("Serif", Font.BOLD, 120));
        title.setForeground(new Color(255, 200, 0));
        title.setBounds(50, 80, 700, 180);
        menuPanel.add(title);

        // Boutons
        addButton(menuPanel, "Nouvelle Partie", 1510, 400, e -> DemarrerJeu());
        addButton(menuPanel, "Règles du Jeu", 1510, 650, e -> Regles());
        addButton(menuPanel, "Quitter", 1510, 800, e -> System.exit(0));

        frame.add(menuPanel);
    }

    private void addButton(JPanel panel, String text, int x, int y, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 200, 50);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(210, 180, 140));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.addActionListener(action);
        panel.add(button);
    }

    private void DemarrerJeu() {
        frame.dispose();
        SwingUtilities.invokeLater(() -> new FenetreChoixMode(frame));
    }

    private void Regles() {
        JOptionPane.showMessageDialog(frame,
            "Règles du jeu de Tablut :\n\n" +
            "1. Le but du jeu est de capturer le roi ou de le faire fuir.\n" +
            "2. Les joueurs déplacent leurs pièces sur le plateau.\n" +
            "3. Les pièces se déplacent en ligne droite, comme aux échecs.\n" +
            "4. Le roi peut se déplacer d'une case dans n'importe quelle direction.\n" +
            "5. Les défenseurs doivent protéger le roi et l'aider à fuir.\n" +
            "6. Les attaquants doivent capturer le roi en l'encerclant.\n",
            "Règles du Jeu", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::new);
    }
}