package Model.IA;

import Model.Coordonnee;
import Model.Joueur;
import Model.Moteur;
import Model.Piece;
import Model.Plateau;
import Model.TypeJoueur;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe d'évaluation statique pour une IA MinMax.
 * Calcule un score global du plateau, positif si l'avantage est pour les gardes, négatif s'il est pour les mercenaires.
 */
public class Heuristique {

    // Scores terminaux
    private static final int WIN_SCORE = 10000;     // Score renvoyé si le roi s'échappe
    private static final int LOSE_SCORE = -10000;   // Score renvoyé si le roi est capturé

    // Pondérations des critères d'évaluation
    private static final int KING_ESCAPE_WEIGHT = 30;      // Poids de la proximité du roi vers une sortie
    private static final int KING_THREAT_WEIGHT = 80;      // Poids de la menace autour du roi
    private static final int PIECE_WEIGHT = 50;            // Valeur d'une pièce vivante
    private static final int PIECE_DIFF_WEIGHT = 2;        // Poids de la différence de pièces
    private static final int MOBILITY_DIFF_WEIGHT = 2;     // Poids de la mobilité relative
    private static final int EXIT_PATHS_WEIGHT = 500;      // Poids du nombre de chemins libres pour le roi

    /**
     * Évalue un plateau de jeu à un instant donné et renvoie un score relatif.
     * Un score > 0 signifie un avantage pour les gardes, < 0 un avantage pour les mercenaires.
     */
    public static int evaluate(Plateau plateau) {
        // 1) Cas de victoire ou défaite immédiate
        if (plateau.roiSurUnBord())   return WIN_SCORE;
        if (plateau.estRoiCapture()) return LOSE_SCORE;

        // 2) Coordonnée du roi
        Coordonnee roi = plateau.trouverRoi();

        // 2a) Proximité à une bordure (distance minimale en cases libres)
        int distanceVersSortie = shortestDistanceToEdge(plateau, roi);
        int scoreEscape = (plateau.getTaille() - distanceVersSortie) * KING_ESCAPE_WEIGHT;

        // 2b) Nombre de chemins directs d'évasion possibles pour le roi
        int cheminsEvasion = countExitPaths(plateau, roi);
        int scoreEvasion = cheminsEvasion * EXIT_PATHS_WEIGHT;
        if (cheminsEvasion > 1) return WIN_SCORE / 2; // Avantage net pour les gardes

        // 3) Nombre d'ennemis adjacents au roi (menace directe)
        int nbMenaces = countAdjacentPieces(plateau, roi, Piece.ATTAQUANT);
        int scoreMenace = -KING_THREAT_WEIGHT * (int) Math.pow(2, nbMenaces); // exponentiel

        // 4) Différence de pièces vivantes : (gardes + roi) - mercenaires
        int nbGardes = plateau.getPiecesDuJoueur(TypeJoueur.GARDE).size();
        int nbMercenaires = plateau.getPiecesDuJoueur(TypeJoueur.MERCENAIRE).size();
        int scorePieces = (nbGardes - nbMercenaires) * PIECE_DIFF_WEIGHT * PIECE_WEIGHT;

        // 5) Différence de mobilité (nombre de coups possibles)
        int[] mobilities = mobility(plateau);
        int scoreMobilite = (mobilities[1] - mobilities[0]) * MOBILITY_DIFF_WEIGHT;


        // 6) Score global cumulé
        return scoreEscape + scoreEvasion + scoreMenace + scorePieces + scoreMobilite;
    }

    /**
     * Compte le nombre de directions (haut, bas, gauche, droite) dans lesquelles
     * le roi peut atteindre une bordure du plateau sans rencontrer d'obstacle.
     *
     * @param plateau Le plateau de jeu
     * @param roi La position actuelle du roi
     * @return Le nombre de chemins d'évasion directs disponibles pour le roi
     */
    private static int countExitPaths(Plateau plateau, Coordonnee roi) {
        int cheminsLibres = 0;
        int taille = plateau.getTaille();

        // Directions orthogonales : haut, bas, gauche, droite
        int[][] directions = {
            {-1, 0}, // haut
            {1, 0},  // bas
            {0, -1}, // gauche
            {0, 1}   // droite
        };

        for (int[] direction : directions) {
            int ligne = roi.getLigne() + direction[0];
            int colonne = roi.getColonne() + direction[1];

            // Si la case adjacente au roi est hors du plateau, cette direction est impossible
            if (ligne < 0 || ligne >= taille || colonne < 0 || colonne >= taille) {
                continue;
            }

            // Si la case adjacente n'est pas vide, ce chemin est bloqué immédiatement
            if (plateau.getCase(new Coordonnee(ligne, colonne)).getPiece() != Piece.VIDE) {
                continue;
            }

            // On avance dans cette direction jusqu'à soit : une pièce, soit le bord
            boolean cheminLibre = true;
            while (ligne > 0 && ligne < taille - 1 && colonne > 0 && colonne < taille - 1) {
                ligne += direction[0];
                colonne += direction[1];

                if (plateau.getCase(new Coordonnee(ligne, colonne)).getPiece() != Piece.VIDE) {
                    cheminLibre = false;
                    break;
                }
            }

            // Si la boucle est sortie sur un bord ET sans obstruction : chemin d’évasion valide
            if (cheminLibre && (ligne == 0 || ligne == taille - 1 || colonne == 0 || colonne == taille - 1)) {
                cheminsLibres++;
            }
        }

        return cheminsLibres;
    }


    /**
     * Calcule la distance minimale (en nombre de cases libres)
     * permettant au roi de rejoindre une bordure du plateau.
     * Utilise une recherche en largeur (BFS) pour explorer les chemins accessibles.
     *
     * @param plateau Le plateau de jeu actuel
     * @param roi La position actuelle du roi
     * @return La distance minimale vers un bord ou la taille du plateau si aucun chemin n'est libre
     */
    private static int shortestDistanceToEdge(Plateau plateau, Coordonnee roi) {
        int taille = plateau.getTaille();

        // Grille de cases déjà visitées
        boolean[][] visite = new boolean[taille][taille];

        // File de parcours BFS : {ligne, colonne, distance}
        Queue<int[]> file = new LinkedList<>();
        file.offer(new int[]{roi.getLigne(), roi.getColonne(), 0});
        visite[roi.getLigne()][roi.getColonne()] = true;

        // Directions orthogonales : haut, bas, gauche, droite
        int[][] directions = {
            {-1, 0}, // haut
            {1, 0},  // bas
            {0, -1}, // gauche
            {0, 1}   // droite
        };

        while (!file.isEmpty()) {
            int[] caseActuelle = file.poll();
            int ligne = caseActuelle[0];
            int colonne = caseActuelle[1];
            int distance = caseActuelle[2];

            // Si on atteint une bordure, on retourne la distance
            if (ligne == 0 || ligne == taille - 1 || colonne == 0 || colonne == taille - 1) {
                return distance;
            }

            // Exploration des 4 cases voisines
            for (int[] dir : directions) {
                int nouvelleLigne = ligne + dir[0];
                int nouvelleColonne = colonne + dir[1];

                // Vérifie que la case est dans le plateau et libre
                if (nouvelleLigne >= 0 && nouvelleLigne < taille &&
                    nouvelleColonne >= 0 && nouvelleColonne < taille &&
                    !visite[nouvelleLigne][nouvelleColonne] &&
                    plateau.getCase(new Coordonnee(nouvelleLigne, nouvelleColonne)).getPiece() == Piece.VIDE) {

                    // Marquer comme visité et ajouter à la file
                    visite[nouvelleLigne][nouvelleColonne] = true;
                    file.offer(new int[]{nouvelleLigne, nouvelleColonne, distance + 1});
                }
            }
        }

        // Aucun chemin libre vers une sortie trouvé
        return taille;
    }


    /**
     * Compte le nombre de pièces de type donné adjacentes orthogonalement à une coordonnée.
     * @param plateau Le plateau sur lequel effectuer la recherche
     * @param centre La coordonnée autour de laquelle chercher
     * @param typeRecherche Le type de pièce à compter (ex : ATTAQUANT)
     * @return Le nombre de pièces adjacentes du type recherché
     */
    private static int countAdjacentPieces(Plateau plateau, Coordonnee centre, Piece typeRecherche) {
        int compteur = 0;
        int taille = plateau.getTaille();

        // Directions orthogonales : haut, bas, gauche, droite
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int ligne = centre.getLigne() + direction[0];
            int colonne = centre.getColonne() + direction[1];

            // Vérifie que la case est dans les limites du plateau
            if (ligne >= 0 && ligne < taille && colonne >= 0 && colonne < taille) {
                Piece pieceVoisine = plateau.getCase(new Coordonnee(ligne, colonne)).getPiece();
                if (pieceVoisine == typeRecherche) {
                    compteur++;
                }
            }
        }
        return compteur;
    }


    /**
     * Calcule la mobilité des deux camps à partir d’un unique clone du plateau.
     * @param plateau Le plateau actuel
     * @return Un tableau contenant : [mobilité des mercenaires, mobilité des gardes]
     */
    private static int[] mobility(Plateau plateau) {
        // Cloner le plateau une seule fois
        Plateau clone = plateau.clone();

        // Calcul de la mobilité pour les mercenaires (joueur actuel = mercenaire)
        Moteur moteurMercenaire = new Moteur(clone,
                new Joueur(TypeJoueur.MERCENAIRE),
                new Joueur(TypeJoueur.GARDE));
        int mobilityMercenaire = moteurMercenaire.getCoupsPossibles().size();

        // Calcul de la mobilité pour les gardes (joueur actuel = garde)
        Moteur moteurGarde = new Moteur(clone,
                new Joueur(TypeJoueur.GARDE),
                new Joueur(TypeJoueur.MERCENAIRE));
        int mobilityGarde = moteurGarde.getCoupsPossibles().size();

        // Retourner les deux valeurs dans un tableau
        return new int[] {mobilityMercenaire, mobilityGarde};
    }

}