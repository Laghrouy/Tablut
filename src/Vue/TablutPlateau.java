package Vue;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TablutPlateau extends JPanel {
    private static final int SIZE = 9;
    private static final int CASE_SIZE = 88;
    private static final int OFFSET_X = 734;
    private static final int OFFSET_Y = 143;

    private CaseUI[][] casesUI;
    private BufferedImage fondPlateau;
    private JLabel joueurActuelLabel;
    private JButton annulerButton;
    private JButton refaireButton;
    private JButton menuButton;
    private ImageIcon coupPossibleIcone;

    private JLabel panneauLabel;
    private int panneauX = 100; // Position X par défaut
    private int panneauY = 100; // Position Y par défaut
    private int largeur = 500; // Largeur par défaut
    private int hauteur = 180; // Hauteur par défaut

    public TablutPlateau() {
        setLayout(null);
        initAffichage();
    }

    private void initAffichage() {
        try {
            fondPlateau = ImageIO.read(new File("Images/fondplat.png"));
            setPreferredSize(new Dimension(fondPlateau.getWidth(), fondPlateau.getHeight()));
        } catch (Exception e) {
            System.err.println("Erreur chargement fondplat.png");
        }

        try {
            Image img = ImageIO.read(new File("Images/coup_possibles.png"))
                .getScaledInstance(CASE_SIZE - 10, CASE_SIZE - 10, Image.SCALE_SMOOTH);
            coupPossibleIcone = new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("Erreur chargement coup_possibles.png : " + e.getMessage());
            coupPossibleIcone = null;
        }

        // Ajoute le panneau AVANT le label joueur
        try {
            Image img = ImageIO.read(new File("Images/panneau.png"))
                .getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
            panneauLabel = new JLabel(new ImageIcon(img));
            panneauLabel.setBounds(panneauX, panneauY, largeur, hauteur);
            panneauLabel.setOpaque(false);
            panneauLabel.setLayout(null); // Important pour placer le label à l'intérieur
            add(panneauLabel);
            setComponentZOrder(panneauLabel, 0);
        } catch (Exception e) {
            System.err.println("Erreur chargement panneau.png : " + e.getMessage());
            panneauLabel = null;
        }

        // Maintenant on peut ajouter le label du joueur dans le panneau
        initialiserLabeljoueur();

        initialiserCases();
        initialiserBoutons();
    }

    private void initialiserCases() {
        casesUI = new CaseUI[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                casesUI[i][j] = new CaseUI();
                casesUI[i][j].setBounds(
                    OFFSET_X + j * CASE_SIZE, 
                    OFFSET_Y + i * CASE_SIZE, 
                    CASE_SIZE, 
                    CASE_SIZE
                );
                add(casesUI[i][j]);
            }
        }
    }

    private void initialiserLabeljoueur() {
        joueurActuelLabel = new JLabel();
        joueurActuelLabel.setBounds(0, 0, largeur, hauteur); // Positionné dans le panneau
        joueurActuelLabel.setFont(new Font("Arial", Font.BOLD, 22));
        joueurActuelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        joueurActuelLabel.setVerticalAlignment(SwingConstants.CENTER);
        joueurActuelLabel.setForeground(Color.BLACK);

        if (panneauLabel != null) {
            panneauLabel.add(joueurActuelLabel);
            panneauLabel.repaint();
        }
    }

    private void initialiserBoutons() {
        // Position des boutons
        int boutonX = 1700;
        int boutonY1 = 500;
        int boutonY2 = 670;
        int boutonY3 = 840; // Position pour le bouton menu principal

        int buttonSize = 160; // Taille du bouton (zone cliquable)
        int iconSize = 140;   // Taille de l'image à l'intérieur

        // Bouton Annuler (retour.png)
        try {
            Image retourImg = ImageIO.read(new File("Images/retour.png")) // res/Images/retour.png
                .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            annulerButton = new JButton(new ImageIcon(retourImg));
            annulerButton.setContentAreaFilled(false);
            annulerButton.setBorderPainted(false);
            annulerButton.setFocusPainted(false);
            annulerButton.setOpaque(false);
        } catch (Exception e) {
            annulerButton = new JButton("Annuler");
        }
        annulerButton.setBounds(boutonX, boutonY1, buttonSize, buttonSize);
        annulerButton.setHorizontalAlignment(SwingConstants.CENTER);
        annulerButton.setVerticalAlignment(SwingConstants.CENTER);
        add(annulerButton);

        // Bouton Refaire (refaire.png)
        try {
            Image refaireImg = ImageIO.read(new File("Images/refaire.png")) // res/Images/refaire.png
                .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            refaireButton = new JButton(new ImageIcon(refaireImg));
            refaireButton.setContentAreaFilled(false);
            refaireButton.setBorderPainted(false);
            refaireButton.setFocusPainted(false);
            refaireButton.setOpaque(false);
        } catch (Exception e) {
            refaireButton = new JButton("Refaire");
        }
        refaireButton.setBounds(boutonX, boutonY2, buttonSize, buttonSize);
        refaireButton.setHorizontalAlignment(SwingConstants.CENTER);
        refaireButton.setVerticalAlignment(SwingConstants.CENTER);
        add(refaireButton);

        // Bouton Menu Principal (home.png)
        try {
            Image homeImg = ImageIO.read(new File("Images/home.png")) // res/Images/home.png
                .getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            menuButton = new JButton(new ImageIcon(homeImg));
            menuButton.setContentAreaFilled(false);
            menuButton.setBorderPainted(false);
            menuButton.setFocusPainted(false);
            menuButton.setOpaque(false);
        } catch (Exception e) {
            menuButton = new JButton("Menu Principal");
        }
        menuButton.setBounds(boutonX, boutonY3, buttonSize, buttonSize);
        menuButton.setHorizontalAlignment(SwingConstants.CENTER);
        menuButton.setVerticalAlignment(SwingConstants.CENTER);
        add(menuButton);
    }

    public void updateBoard(Piece[][] boardState, String currentPlayer) {
        joueurActuelLabel.setText("Joueur actuel : " + currentPlayer);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                casesUI[i][j].setPiece(boardState[i][j]);
                casesUI[i][j].sethighlight(false);
            }
        }
        repaint();
    }

    public void setCellClickListener(int row, int col, Runnable action) {
        casesUI[row][col].addActionListener(e -> action.run());
    }

    public void setAnnulerAction(Runnable action) {
        annulerButton.addActionListener(e -> action.run());
    }

    public void setRefaireAction(Runnable action) {
        refaireButton.addActionListener(e -> action.run());
    }

    public void setMenuAction(Runnable action) {
        menuButton.addActionListener(e -> action.run());
    }

    public void AfficherSurbrillance(int row, int col) {
        casesUI[row][col].setIcon(coupPossibleIcone);
    }

    public void EnleverSurbrillance() {
        for (CaseUI[] row : casesUI) {
            for (CaseUI cell : row) {
                cell.sethighlight(false);
                cell.setIcon(null); // Retire l'icône coup possible
            }
        }
    }

    public void GestionPanneau(int x, int y, int width, int height) {
        panneauX = x;
        panneauY = y;
        largeur = width;
        hauteur = height;
        if (panneauLabel != null) {
            panneauLabel.setBounds(panneauX, panneauY, largeur, hauteur);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondPlateau != null) {
            g.drawImage(fondPlateau, 0, 0, this);
        }
    }

    public void afficheGagnant(String winner) {
        JOptionPane.showMessageDialog(this, "Le gagnant est : " + winner);
    }
}