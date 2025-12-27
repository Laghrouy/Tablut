package Controleur;

import Model.*;
import Vue.*;
import Model.IA.IAAleatoire;

import javax.swing.*;

public class GameInitializer implements FenetreChoixMode.GameModeListener {
    private final JFrame Frame;

    public GameInitializer(JFrame Frame) {
        this.Frame = Frame;
    }

    @Override
    public void ModeJvJ() {
        startGame(false, false);
    }

    @Override
    public void ModeJvIA() {
        startGame(false, true);
    }

    @Override
    public void ModeIAvIA() {
        startGame(true, true);
    }

    private void startGame(boolean isIA1, boolean isIA2) {
        try {
            Plateau plateau = new Plateau();
            Joueur joueur1 = createPlayer(TypeJoueur.MERCENAIRE, isIA1);
            Joueur joueur2 = createPlayer(TypeJoueur.GARDE, isIA2);
            Moteur moteur = new Moteur(plateau, joueur1, joueur2);

            ControlerJeu gameController = new ControlerJeu(moteur);
            TablutPlateau tablutPlateau = new TablutPlateau();
            
            JFrame gameFrame = new JFrame("Jeu de Tablut");
            new TablutViewAdapter(gameController, tablutPlateau, gameFrame);

            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameFrame.add(tablutPlateau);
            gameFrame.pack();
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(Frame,
                "Erreur lors du démarrage du jeu : " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private Joueur createPlayer(TypeJoueur typeJoueur, boolean isIA) {
        return isIA ? new Joueur(typeJoueur, new IAAleatoire(null)) : new Joueur(typeJoueur);
    }
}