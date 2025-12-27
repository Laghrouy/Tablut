package Model;

/**
 * Représente une position (ligne, colonne) sur le plateau.
 */
public class Coordonnee {
    private final int ligne;
    private final int colonne;

    public Coordonnee(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    @Override
    public String toString() {
        return "(" + ligne + "," + colonne + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordonnee that = (Coordonnee) obj;
        return ligne == that.ligne && colonne == that.colonne;
    }

    @Override
    public int hashCode() {
        return 31 * ligne + colonne;
    }
}