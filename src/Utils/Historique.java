package Utils;

import Model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Historique {
    public static class EtatJeu {
        private final Piece[][] plateau;
        private final Coup coup;
        private final TypeJoueur joueurActuel;
        
        public EtatJeu(Plateau plateau, Coup coup, TypeJoueur joueurActuel) {
            this.plateau = new Piece[9][9];
            // Copie profonde du plateau
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    this.plateau[i][j] = plateau.getCase(new Coordonnee(i, j)).getPiece();
                }
            }
            this.coup = coup;
            this.joueurActuel = joueurActuel;
        }
        
        public Plateau getPlateau() {
            Plateau nouveauPlateau = new Plateau();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    nouveauPlateau.getCase(new Coordonnee(i, j)).setPiece(plateau[i][j]);
                }
            }
            return nouveauPlateau;
        }
        
        public Coup getCoup() {
            return coup;
        }
        
        public TypeJoueur getJoueurActuel() {
            return joueurActuel;
        }
    }

    private Stack<EtatJeu> historique;
    private Stack<EtatJeu> rejouer;

    public Historique() {
        this.historique = new Stack<>();
        this.rejouer = new Stack<>();
    }

    public void enregistrer(Plateau plateau, Coup coup, TypeJoueur joueurActuel) {
        EtatJeu etat = new EtatJeu(plateau, coup, joueurActuel);
        historique.push(etat);
        rejouer.clear();
    }

    public Coup dernierCoup() {
        if (historique.isEmpty()) return null;
        return historique.peek().getCoup();
    }

    public EtatJeu annulerCoup() {
        if (historique.isEmpty()) return null;
        EtatJeu etat = historique.pop();
        rejouer.push(etat);
        return etat;
    }

    public EtatJeu rejouerCoup() {
        if (rejouer.isEmpty()) return null;
        EtatJeu etat = rejouer.pop();
        historique.push(etat);
        return etat;
    }

    public List<Coup> getHistorique() {
        List<Coup> coups = new ArrayList<>();
        for (EtatJeu etat : historique) coups.add(etat.getCoup());
        return coups;
    }
}
