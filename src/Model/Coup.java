package Model;

/**
 * Représente un coup dans le jeu, avec une position de départ et d'arrivée.
 */
public class Coup {
    private final Coordonnee depart;
    private final Coordonnee arrivee;

    public Coup(Coordonnee depart, Coordonnee arrivee) {
        this.depart = depart;
        this.arrivee = arrivee;
    }

    public Coordonnee getDepart() {
        return depart;
    }

    public Coordonnee getArrivee() {
        return arrivee;
    }

    @Override
    public String toString() {
        return depart + " → " + arrivee;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coup other = (Coup) obj;
        return depart.equals(other.depart) && arrivee.equals(other.arrivee);
    }

    @Override
    public int hashCode() {
        return 31 * depart.hashCode() + arrivee.hashCode();
    }
}