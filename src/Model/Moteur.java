package Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Moteur {

    private Plateau plateau;
    private final Joueur joueur1; // MERCENAIRE
    private final Joueur joueur2; // GARDE
    private Joueur joueurActuel;
    private boolean estPartieTerminee;
    private Set<Coup> coupsPossibles;

    public Moteur(Plateau plateau, Joueur joueur1, Joueur joueur2) {
        this.plateau = plateau;
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.joueurActuel = joueur1; // MERCENAIRE commence
        this.estPartieTerminee = false;
        updateCoupsPossibles();
    }

    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public Joueur getJoueurActuel() {
        return joueurActuel;
    }

    public void setJoueurActuel(TypeJoueur type) {
        if (type == TypeJoueur.MERCENAIRE) {
            this.joueurActuel = joueur1;
        } else {
            this.joueurActuel = joueur2;
        }
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }

    public boolean estPartieTerminee() {
        return estPartieTerminee;
    }

    public Set<Coup> getCoupsPossibles(){ return coupsPossibles;}

    /**
     * Renvoie les coups possibles avec une piece donnee
     * @param coordonnee Coordonnee de la piece sur le plateau
     * @return l'ensemble des coups possibles a partir de la piece, null si la piece n'appartient pas au joueur actuel
     */
    public Set<Coup> getCoupsPossibles(Coordonnee coordonnee){
        Set<Coup> coupsPossibles = new HashSet<Coup>(16); // valeur de retour
        Case casePiece = plateau.getCase(coordonnee); // recuperation de la piece sur la case

        if(estPieceDuJoueurActuel(casePiece.getPiece(),joueurActuel.getRole())){ // si la piece appartient au joueur actuel
            // Pour chaques coups verifier si il implique la piece donnee
            for(Coup coup : this.coupsPossibles){
                if(coup.getDepart()==coordonnee){
                    coupsPossibles.add(coup);
                }
            }
        }
        return coupsPossibles;
    }

    public boolean coup(Coup coup) {
        Coordonnee depart = coup.getDepart();
        Coordonnee arrivee = coup.getArrivee();

        if (!estDeplacementValide(coup)) {
            return false;
        }

        Piece piece = plateau.getCase(depart).getPiece();

        plateau.getCase(depart).setPiece(Piece.VIDE);
        plateau.getCase(arrivee).setPiece(piece);

        verifierCapture(arrivee, joueurActuel.getRole());

        Joueur gagnant = estVictoire();
        if (gagnant != null) {
            estPartieTerminee = true;
        } else {
            changerJoueur();
            updateCoupsPossibles();
        }

        return true;
    }

    public void changerJoueur() {
        joueurActuel = (joueurActuel == joueur1) ? joueur2 : joueur1;
    }

    public boolean estDeplacementValide(Coup coup) {
        return this.coupsPossibles.contains(coup);
    }

    private boolean estCoupValide(Coup coup) {
        Coordonnee depart = coup.getDepart();
        Coordonnee arrivee = coup.getArrivee();

        if (!plateau.estDansPlateau(depart) || !plateau.estDansPlateau(arrivee)) return false;
        if (!estDepartValide(depart)) return false;
        if (!estArriveeValide(arrivee)) return false;
        if (!estDeplacementEnLigneDroite(coup)) return false;
        return estCheminValide(coup);
    }

    private boolean estDepartValide(Coordonnee depart) {
        Case caseDepart = plateau.getCase(depart);
        if (caseDepart.estVide()) return false;
        return estPieceDuJoueurActuel(caseDepart.getPiece(), joueurActuel.getRole());
    }

    private boolean estArriveeValide(Coordonnee arrivee) {
        Case caseArrivee = plateau.getCase(arrivee);
        return caseArrivee.estVide() && !caseArrivee.estTrone();
    }

    private boolean estPieceDuJoueurActuel(Piece piece, TypeJoueur type) {
        return switch (type) {
            case GARDE -> piece == Piece.DEFENSEUR || piece == Piece.ROI;
            case MERCENAIRE -> piece == Piece.ATTAQUANT;
        };
    }

    private boolean estDeplacementEnLigneDroite(Coup coup) {
        Coordonnee depart = coup.getDepart();
        Coordonnee arrivee = coup.getArrivee();
        return depart.getLigne() == arrivee.getLigne() || depart.getColonne() == arrivee.getColonne();
    }

    private boolean estCheminValide(Coup coup) {
        Coordonnee depart = coup.getDepart();
        Coordonnee arrivee = coup.getArrivee();
        int ligneDepart = depart.getLigne();
        int colDepart = depart.getColonne();
        int ligneArrivee = arrivee.getLigne();
        int colArrivee = arrivee.getColonne();

        if (ligneDepart == ligneArrivee) {
            for (int col = Math.min(colDepart, colArrivee) + 1; col < Math.max(colDepart, colArrivee); col++) {
                if (!plateau.getCase(new Coordonnee(ligneDepart, col)).estVide()) return false;
            }
        } else if (colDepart == colArrivee) {
            for (int ligne = Math.min(ligneDepart, ligneArrivee) + 1; ligne < Math.max(ligneDepart, ligneArrivee); ligne++) {
                if (!plateau.getCase(new Coordonnee(ligne, colDepart)).estVide()) return false;
            }
        }

        return true;
    }

    public void verifierCapture(Coordonnee arrivee, TypeJoueur joueur) {
        Case caseSource = plateau.getCase(arrivee);

        if (caseSource.getPiece() == Piece.ROI) return;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int lAdj = arrivee.getLigne() + dir[0];
            int cAdj = arrivee.getColonne() + dir[1];
            Coordonnee adj = new Coordonnee(lAdj, cAdj);

            if (!plateau.estDansPlateau(adj)) continue;

            Case caseAdj = plateau.getCase(new Coordonnee(lAdj, cAdj));

            if (caseAdj.estVide() || caseAdj.getPiece() == Piece.ROI || estPieceDuJoueurActuel(caseAdj.getPiece(), joueur)) {
                continue;
            }

            int lOpp = lAdj + dir[0];
            int cOpp = cAdj + dir[1];
            Coordonnee derriere = new Coordonnee(lOpp, cOpp);

            if (!plateau.estDansPlateau(derriere)) continue;

            Case caseOpp = plateau.getCase(derriere);

            boolean capture = caseOpp.getPiece()!=Piece.ROI && (caseOpp.estTrone() || (caseOpp.estOccupee() && estPieceDuJoueurActuel(caseOpp.getPiece(), joueur)));
            if (capture) {
                caseAdj.setPiece(Piece.VIDE);
            }
        }
    }

    /**
     * Les sorites sont les directions dans lesquelles la
     * piece (ici le Roi) peut attendre une case du bord
     * via un coup legal(valide). Entre autres :
     * 0 <= nbSortieRoiPossible <= 4
     * (N'utilise pas la methode estBord() du Plateau =>
     * methode prevu pour un plateau 9x9 ou les sorties
     * du Roi sont les bords)
     * @return nombre
     */
    public int nbSortieRoiPossible() {
        int nbSortie = 0; // valeur de retour

        // Recuperation des coordonnees du Roi
        Coordonnee coordonneRoi = plateau.trouverRoi();
        int x = coordonneRoi.getColonne();
        int y = coordonneRoi.getLigne();

        // Initialisation des coups sortants
        ArrayList<Coup> coupsSortants = new ArrayList<Coup>(4);
        coupsSortants.add(new Coup(coordonneRoi, new Coordonnee(0,x)));
        coupsSortants.add(new Coup(coordonneRoi, new Coordonnee(y,8)));
        coupsSortants.add(new Coup(coordonneRoi, new Coordonnee(8,x)));
        coupsSortants.add(new Coup(coordonneRoi, new Coordonnee(y,0)));

        // Traitement(validitee) des coups
        for (Coup coup : coupsSortants){
            if (estDeplacementValide(coup)) {
                nbSortie++;
            }
        }
        return nbSortie;
    }

    /**
     * Le joueur du camp des Gardes est en "Echec" si apres le
     * tour des Mercenaires le Roi peu atteindre un bord au prochain
     * coup si les Gardes n'interposent pas de piece(s) entre le ou
     * les sorties et le Roi.
     * (Opere un parcours des 4 sorties du Roi possibles et non une recherche)
     * @return vrai si et seulement si les Gardes sont en "Echec"
     */
    public boolean estGardeEnEchec(){
        return nbSortieRoiPossible()>=1;
    }

    public Joueur estVictoire() {
        if (plateau.estRoiCapture()) return joueur1.getRole() == TypeJoueur.GARDE ? joueur2 : joueur1;
        if (plateau.roiSurUnBord()) return joueur1.getRole() == TypeJoueur.GARDE ? joueur1 : joueur2;

        if (getCoupsPossibles().isEmpty()) {
            return (joueurActuel == joueur1) ? joueur2 : joueur1;
        }

        return null;
    }

    private void updateCoupsPossibles() {
        Set<Coup> coups = new HashSet<Coup>(150);
        List<Coordonnee> positions = plateau.getPiecesDuJoueur(joueurActuel.getRole());
        int taille = plateau.getTaille();

        for (Coordonnee depart : positions) {
            int ligne = depart.getLigne();
            int colonne = depart.getColonne();

            // Vérifie les déplacements en ligne horizontale (même ligne)
            for (int j = 0; j < taille; j++) {
                if (j != colonne) {
                    Coordonnee arrivee = new Coordonnee(ligne, j);
                    if (estCoupValide(new Coup(depart, arrivee))) {
                        coups.add(new Coup(depart, arrivee));
                    }
                }
            }

            // Vérifie les déplacements en ligne verticale (même colonne)
            for (int i = 0; i < taille; i++) {
                if (i != ligne) {
                    Coordonnee arrivee = new Coordonnee(i, colonne);
                    if (estCoupValide(new Coup(depart, arrivee))) {
                        coups.add(new Coup(depart, arrivee));
                    }
                }
            }
        }

        this.coupsPossibles = coups;
    }

    public void reinitialiser() {
        this.plateau = new Plateau();
        this.joueurActuel = joueur1;
        this.estPartieTerminee = false;
    }
}