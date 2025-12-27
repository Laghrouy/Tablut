package Model;

import Model.IA.IA;

/**
 * Classe représentant un joueur dans le jeu Tablut.
 * Il peut être contrôlé par un humain (sans IA) ou une IA concrète (IAMinMax, etc.).
 */
public class Joueur {

    private final TypeJoueur role;
    private final IA ia;

    /**
     * Constructeur pour un joueur humain (sans IA).
     * @param role Le type du joueur (MERCENAIRE ou GARDE)
     */
    public Joueur(TypeJoueur role) {
        this.role = role;
        this.ia = null;
    }

    /**
     * Constructeur pour un joueur contrôlé par une IA.
     * @param role Le type du joueur (MERCENAIRE ou GARDE)
     * @param ia L'IA qui contrôle ce joueur
     */
    public Joueur(TypeJoueur role, IA ia) {
        this.role = role;
        this.ia = ia;
    }

    /**
     * @return Le rôle de ce joueur (GARDE ou MERCENAIRE)
     */
    public TypeJoueur getRole() {
        return role;
    }

    /**
     * @return true si ce joueur est contrôlé par une IA
     */
    public boolean estIA() {
        return ia != null;
    }

    /**
     * @return L'IA associée à ce joueur, ou null s'il s'agit d'un humain
     */
    public IA getIA() {
        return ia;
    }
}
