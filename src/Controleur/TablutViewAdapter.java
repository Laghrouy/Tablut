package Controleur;

import Model.*;
import Vue.TablutPlateau;
import java.awt.*;
import Vue.Menu;

import javax.swing.JFrame;

public class TablutViewAdapter {
    private final ControlerJeu controller;
    private final TablutPlateau vue;
    private Point PieceChoisie = null;

    private JFrame parentFrame;

    public TablutViewAdapter(ControlerJeu controller, TablutPlateau vue, JFrame parentFrame) {
        this.controller = controller;
        this.vue = vue;
        this.parentFrame = parentFrame;
        setupView();
        setboutons();
        updateView();
    }

    private void setupView() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final int row = i;
                final int col = j;
                vue.setCellClickListener(row, col, () -> GestionClique(row, col));
            }
        }
    }

    private void setboutons() {
        vue.setAnnulerAction(() -> {
            if (controller.annulerCoup()) {
                updateView();
            }
        });

        vue.setRefaireAction(() -> {
            if (controller.rejouerCoup()) {
                updateView();
            }
        });

        vue.setMenuAction(() -> {
            parentFrame.dispose();
            new Menu().setVisible(true);
        });
    }

    private void updateView() {
        Plateau plateau = controller.getPlateau();
        String currentPlayer = controller.getJoueurActuel().getRole().toString();

        Piece[][] boardState = new Piece[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boardState[i][j] = plateau.getCase(new Coordonnee(i, j)).getPiece();
            }
        }

        vue.updateBoard(boardState, currentPlayer);
    }

    private void GestionClique(int row, int col) {
        if (controller.estPartieTerminee()) return;

        if (PieceChoisie == null) {
            if (selectionValide(row, col)) {
                PieceChoisie = new Point(row, col);

                for (Coup coup : controller.getCoupsPossibles()) {
                    Coordonnee depart = coup.getDepart();
                    if (depart.getLigne() == row && depart.getColonne() == col) {
                        Coordonnee arrivee = coup.getArrivee();
                        // Vérifie si ce coup est gagnant
                        if (controller.estCoupGagnant(coup)) {
                            vue.AfficherSurbrillanceGagnante(arrivee.getLigne(), arrivee.getColonne());
                        } else {
                            vue.AfficherSurbrillance(arrivee.getLigne(), arrivee.getColonne());
                        }
                    }
                }
            }
        } else {
            Coup coup = new Coup(
                new Coordonnee(PieceChoisie.x, PieceChoisie.y),
                new Coordonnee(row, col)
            );

            if (controller.jouerCoup(coup)) {
                if (controller.estPartieTerminee()) {
                    vue.afficheGagnant(controller.getJoueurActuel().getRole().toString());
                }
            }
            PieceChoisie = null;
            vue.EnleverSurbrillance();
            updateView();
        }
    }

    private boolean selectionValide(int row, int col) {
        Piece piece = controller.getPlateau().getCase(new Coordonnee(row, col)).getPiece();
        TypeJoueur role = controller.getJoueurActuel().getRole();

        return switch (role) {
            case GARDE -> piece == Piece.DEFENSEUR || piece == Piece.ROI;
            case MERCENAIRE -> piece == Piece.ATTAQUANT;
        };
    }
}
