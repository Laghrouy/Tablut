package Model.IA;

import Model.Coup;
import Model.Joueur;
import Model.Moteur;
import Model.Plateau;
import Model.TypeJoueur;
import java.util.*;

/**
 * IA basée sur l'algorithme MinMax avec élagage alpha-bêta, table de transposition
 * pour mémoriser les évaluations déjà calculées, et ordonnancement des coups
 * selon une heuristique afin de prioriser les meilleurs déplacements.
 */
public class IAMinMax extends IA {
    private final int maxDepth;  // Profondeur maximale d'exploration
    private final Map<String, Integer> transpositionTable = new HashMap<>(); // Cache des évaluations

    /**
     * Constructeur de l'IA MinMax.
     * @param moteur moteur de jeu
     * @param niveau niveau de l'IA (contient la profondeur)
     */
    public IAMinMax(Moteur moteur, NiveauIA niveau) {
        super(moteur);
        this.maxDepth = niveau.getProfondeur();
    }

    /**
     * Choisit le meilleur coup possible à jouer via MinMax.
     * @return vrai si un coup valide a été joué
     */
    @Override
    public boolean choisirCoup() {
        long start = System.currentTimeMillis();

        List<Coup> coupsPossibles = moteur.getCoupsPossibles().stream().toList();
        if (coupsPossibles.isEmpty()) return false;

        random = new Random();
        TypeJoueur joueurActuel = moteur.getJoueurActuel().getRole();

        Coup meilleurCoup = null;
        int meilleurScoreOriente = Integer.MIN_VALUE;

        // Clone du plateau initial
        Plateau plateauInitial = moteur.getPlateau().clone();

        // Évalue chaque coup possible
        for (Coup coup : coupsPossibles) {
            Plateau copiePlateau = plateauInitial.clone();

            Moteur simulation = new Moteur(copiePlateau,
                    new Joueur(joueurActuel),
                    new Joueur(joueurActuel == TypeJoueur.GARDE ? TypeJoueur.MERCENAIRE : TypeJoueur.GARDE));
            simulation.coup(coup);

            int score = alphaBeta(copiePlateau, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            int scoreOriente = (joueurActuel == TypeJoueur.GARDE) ? score : -score;

            if (scoreOriente > meilleurScoreOriente) {
                meilleurScoreOriente = scoreOriente;
                meilleurCoup = coup;
            } else if (scoreOriente == meilleurScoreOriente && !random.nextBoolean()) {
                meilleurCoup = coup;
            }
        }

        long duree = System.currentTimeMillis() - start;
        if (meilleurCoup != null && moteur.estDeplacementValide(meilleurCoup)) {
            System.out.println("Coup IAMinMax: " + meilleurCoup + " (score=" + meilleurScoreOriente + ", temps=" + duree + "ms)");
            return moteur.coup(meilleurCoup);
        }
        return false;
    }

    /**
     * Applique l'algorithme MinMax avec élagage alpha-bêta et cache.
     * @param plateau plateau courant
     * @param profondeur profondeur restante
     * @param alpha borne inférieure pour élagage
     * @param beta borne supérieure pour élagage
     * @param estMaximisant vrai si c'est au tour du joueur maximisant
     * @return score heuristique du plateau
     */
    private int alphaBeta(Plateau plateau, int profondeur, int alpha, int beta, boolean estMaximisant) {
        String cle = plateau.toString() + "_" + profondeur + "_" + estMaximisant;
        if (transpositionTable.containsKey(cle)) return transpositionTable.get(cle);

        if (profondeur == 0 || plateau.roiSurUnBord() || plateau.estRoiCapture()) {
            int evaluation = Heuristique.evaluate(plateau);
            transpositionTable.put(cle, evaluation);
            return evaluation;
        }

        Moteur moteurGen = new Moteur(plateau.clone(),
                new Joueur(estMaximisant ? TypeJoueur.GARDE : TypeJoueur.MERCENAIRE),
                new Joueur(estMaximisant ? TypeJoueur.MERCENAIRE : TypeJoueur.GARDE));

        List<Coup> coups = new ArrayList<>(moteurGen.getCoupsPossibles());

        // Trie les coups selon leur évaluation heuristique
        coups.sort((a, b) -> {
            int scoreA = Heuristique.evaluate(simulerCoup(plateau, a));
            int scoreB = Heuristique.evaluate(simulerCoup(plateau, b));
            return estMaximisant ? Integer.compare(scoreB, scoreA) : Integer.compare(scoreA, scoreB);
        });

        int meilleureValeur = estMaximisant ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Coup coup : coups) {
            Plateau plateauFils = simulerCoup(plateau, coup);
            int valeur = alphaBeta(plateauFils, profondeur - 1, alpha, beta, !estMaximisant);

            if (estMaximisant) {
                meilleureValeur = Math.max(meilleureValeur, valeur);
                alpha = Math.max(alpha, meilleureValeur);
            } else {
                meilleureValeur = Math.min(meilleureValeur, valeur);
                beta = Math.min(beta, meilleureValeur);
            }

            if (beta <= alpha) break; // Élagage alpha-bêta
        }

        transpositionTable.put(cle, meilleureValeur);
        return meilleureValeur;
    }

    /**
     * Simule un coup en clonant le plateau et en l'appliquant.
     * @param plateau plateau initial
     * @param coup coup à simuler
     * @return nouveau plateau après le coup
     */
    private Plateau simulerCoup(Plateau plateau, Coup coup) {
        Plateau copie = plateau.clone();
        Moteur moteurTmp = new Moteur(copie,
                new Joueur(TypeJoueur.MERCENAIRE),
                new Joueur(TypeJoueur.GARDE));
        moteurTmp.coup(coup);
        return copie;
    }
}