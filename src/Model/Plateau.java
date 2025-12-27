package Model;

import java.util.ArrayList;
import java.util.List;

public class Plateau implements Cloneable {
    private static final int TAILLE = 9;
    private final Case[][] cases;

    public Plateau(boolean initialiser) {
        this.cases = new Case[TAILLE][TAILLE];
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int col = 0; col < TAILLE; col++) {
                TypeCase type = TypeCase.NORMALE;
                if (estBord(new Coordonnee(ligne, col))) {
                    type = TypeCase.SORTIE;
                } else if (ligne == 4 && col == 4) {
                    type = TypeCase.TRONE;
                }
                cases[ligne][col] = new Case(type);
            }
        }
        if (initialiser) {
            initialiser();
        }
    }

    public Plateau() {
        this(true);
    }

    private void initialiser() {

        // Roi au centre
        cases[4][4].setPiece(Piece.ROI);

        // Défenseurs autour du roi
        int[][] positionsDefenseurs = {
                {2, 4}, {3, 4}, {5, 4}, {6, 4},
                {4, 2}, {4, 3}, {4, 5}, {4, 6}
        };
        for (int[] pos : positionsDefenseurs) {
            cases[pos[0]][pos[1]].setPiece(Piece.DEFENSEUR);
        }

        // Attaquants - 16 pièces
        int[][] positionsAttaquants = {
                {0, 3}, {0, 4}, {0, 5}, {1, 4},
                {3, 0}, {4, 0}, {5, 0}, {4, 1},
                {8, 3}, {8, 4}, {8, 5}, {7, 4},
                {3, 8}, {4, 8}, {5, 8}, {4, 7}
        };
        for (int[] pos : positionsAttaquants) {
            cases[pos[0]][pos[1]].setPiece(Piece.ATTAQUANT);
        }
    }

    private boolean estBord(Coordonnee coordonnee) {
        int ligne = coordonnee.getLigne();
        int col = coordonnee.getColonne();
        return (ligne == 0 || ligne == TAILLE - 1 || col == 0 || col == TAILLE - 1);
    }

    public Case getCase(Coordonnee coordonnee) {
        return cases[coordonnee.getLigne()][coordonnee.getColonne()];
    }

    public void setCase(Coordonnee coordonnee, Case nouvelleCase) {
        cases[coordonnee.getLigne()][coordonnee.getColonne()] = nouvelleCase;
    }

    public int getTaille() {
        return TAILLE;
    }

    public boolean estDansPlateau(Coordonnee coordonnee) {
        int ligne = coordonnee.getLigne();
        int colonne = coordonnee.getColonne();
        return ligne >= 0 && ligne < TAILLE && colonne >= 0 && colonne < TAILLE;
    }

    public boolean roiSurUnBord() {
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int col = 0; col < TAILLE; col++) {
                if (cases[ligne][col].getPiece() == Piece.ROI) {
                    return estBord(new Coordonnee(ligne, col));
                }
            }
        }
        return false;
    }

    public Coordonnee trouverRoi() {
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int col = 0; col < TAILLE; col++) {
                if (cases[ligne][col].getPiece() == Piece.ROI) {
                    return new Coordonnee(ligne, col);
                }
            }
        }
        return null;
    }

    public boolean estRoiCapture() {
        Coordonnee roi = trouverRoi();
        if (roi == null) throw new IllegalStateException("Le roi est introuvable sur le plateau.");

        int l = roi.getLigne();
        int c = roi.getColonne();
        int captures = 0;

        if (estBloqueParMercenaireOuTrone(new Coordonnee(l - 1, c))) captures++;
        if (estBloqueParMercenaireOuTrone(new Coordonnee(l + 1, c))) captures++;
        if (estBloqueParMercenaireOuTrone(new Coordonnee(l, c - 1))) captures++;
        if (estBloqueParMercenaireOuTrone(new Coordonnee(l, c + 1))) captures++;

        return captures >= 4;
    }

    private boolean estBloqueParMercenaireOuTrone(Coordonnee coordonnee) {
        if (!estDansPlateau(coordonnee)) return false;
        Case cCase = getCase(coordonnee);
        return cCase.getPiece() == Piece.ATTAQUANT || cCase.estTrone();
    }

    public List<Coordonnee> getPiecesDuJoueur(TypeJoueur typeJoueur) {
        List<Coordonnee> resultats = new ArrayList<>();
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                Piece piece = cases[i][j].getPiece();
                if (typeJoueur == TypeJoueur.MERCENAIRE && piece == Piece.ATTAQUANT) {
                    resultats.add(new Coordonnee(i, j));
                } else if (typeJoueur == TypeJoueur.GARDE && (piece == Piece.DEFENSEUR || piece == Piece.ROI)) {
                    resultats.add(new Coordonnee(i, j));
                }
            }
        }
        return resultats;
    }


    @Override
    public Plateau clone() {
        Plateau clone = new Plateau(false); // Create an empty board
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                clone.cases[i][j] = this.cases[i][j].clone(); // Deep clone each case
            }
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        /*
        sb.append("   ");
        for (int col = 0; col < getTaille(); col++) {
            sb.append(col).append(" ");
        }
        sb.append("\n");
        */

        for (int ligne = 0; ligne < getTaille(); ligne++) {
            //sb.append(ligne).append(" |");
            for (int col = 0; col < getTaille(); col++) {
                Case c = getCase(new Coordonnee(ligne, col));
                Piece piece = c.getPiece();
                if (c.estTrone() && piece == Piece.VIDE) {
                    sb.append(" *");
                } else{
                    sb.append(" "+pieceToChar(piece));
                }
            }
            //sb.append(" |");
            sb.append(" \n");
        }
        return sb.toString();
    }

    public int[][] getBoardState() {
        int[][] boardState = new int[TAILLE][TAILLE];
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                Piece p = cases[i][j].getPiece();
                if (p == Piece.ROI) {
                    boardState[i][j] = 3;
                } else if (p == Piece.DEFENSEUR) {
                    boardState[i][j] = 1;
                } else if (p == Piece.ATTAQUANT) {
                    boardState[i][j] = 2;
                } else {
                    boardState[i][j] = 0;
                }
            }
        }
        return boardState;
    }
    
    /**
     * Initialise un Plateau a partir de la chaine de caracteres
     * fourni par le toString d'un meme plateau
     * @param s chaine de caracteres representant le plateau
     *          exemple:
     *                   . . . A A A . . .
     *                   . . . . A . . . .
     *                   . . . . D . . . .
     *                   A . . . D . . . A
     *                   A A D D R D D A A
     *                   A . . . D . . . A
     *                   . . . . D . . . .
     *                   . . . . A . . . .
     *                   . . . A A A . . .
     * @return
     */
    public static Plateau fromString(String s) {
        Plateau plateau = new Plateau(false);// valeur de retour
        int taille = plateau.getTaille();
        String[] lignes = s.split("\n");
        int ligneIndex = 0;
        for (String ligne : lignes) {
            int colIndex = 0;
            for (char c : ligne.toCharArray()) {
                // Ignorer les caractères de séparation si présents
                if (Character.isWhitespace(c)) continue;
                Piece p = charToPiece(c);
                plateau.getCase(new Coordonnee(ligneIndex, colIndex)).setPiece(p);
                colIndex++;
            }
            ligneIndex++;
        }
        return plateau;
    }

    /**
     * Convertit un caractère en une pièce du jeu
     * @param c caractère représentant une pièce dans la chaîne de description du plateau
     * @return la pièce correspondante (ROI, DEFENSEUR, ATTAQUANT, VIDE(par default))
     */
    private static Piece charToPiece(char c) {
        switch (c) {
            case 'R': return Piece.ROI;
            case 'D': return Piece.DEFENSEUR;
            case 'A': return Piece.ATTAQUANT;
            default:  return Piece.VIDE;
        }
    }

    /**
     * Convertit une pièce du jeu en un caractère de description.
     * @param piece la pièce à convertir
     * @return le caractère représentant la pièce
     */
    private static char pieceToChar(Piece piece) {
        switch (piece) {
            case ROI:       return 'R';
            case DEFENSEUR: return 'D';
            case ATTAQUANT: return 'A';
            default:        return '.';
        }
    }

    /**
     * Renvoi la valeur d'une abssice ou d'une ordonnee oppose a celle
     * fourni par symetrie centrale par rapport au centre du plateau
     * @param i ordonnee ou abssice dont on veut la valeur oppose
     * @return  ordonnee ou abssice oppose par rapport au centre du plateau
     */
    private int oppose(int i){
        return TAILLE-1-i;
    }

    /**
     * Renvoie si le plateau est equivalent a this a la symetrie pres
     * @param obj plateau a compare
     * @return vrai si et seulement si le plateau est equivalent a this
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                                  // cas de meme adresse
        if (obj == null || getClass() != obj.getClass()) return false; // cas d'objet non plateau
        Plateau that = (Plateau) obj;
        boolean equals = false;                                        // valeur de retour
        /**
         * {a, b, c} : a => (x<->y) ; b => x<-oppose(x) ; c => y<-oppose(y)
         */
        int[][] configs = {{0,0,0},{0,0,1},{0,1,0},{0,1,1},{1,0,0},{1,0,1},{1,1,0},{1,1,1}};
        for(int[] config : configs){
            equals = true;
            int i = 0, j = 0;
            while(equals&&(i<TAILLE)){
                j = 0;
                while(equals&&(j<TAILLE)){
                    Case c1 = getCase(new Coordonnee(i,j));
                    int x = i, y = j, tmp;
                    if(config[1]==1) x = oppose(x);
                    if(config[2]==1) y = oppose(y);
                    if(config[0]==1){ tmp = x; x = y; y = tmp; } // echange des coordonnes
                    // Recuperation de la case correspondante a la configuration
                    Case c2 = that.getCase(new Coordonnee(x,y));
                    if(!c1.equals(c2)){
                        equals = false;
                    }
                    j++;
                }
                i++;
            }
            if(equals) break;
        }
        return equals;
    }
}