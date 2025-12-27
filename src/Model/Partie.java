package Model;

public class Partie {
    private final Joueur joueur1;
    private final Joueur joueur2;
    private final Moteur moteur;

    public Partie(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        Plateau plateau = new Plateau();
        this.moteur = new Moteur(plateau, joueur1, joueur2);

        if (joueur1.estIA()) {
            joueur1.getIA().setMoteur(this.moteur);
        }
        if (joueur2.estIA()) {
            joueur2.getIA().setMoteur(this.moteur);
        }
    }

    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public Moteur getMoteur() {
        return moteur;
    }

    public Joueur getJoueurActuel() {
        return moteur.getJoueurActuel();
    }

    public boolean estPartieTerminee() {
        return moteur.estPartieTerminee();
    }

    public boolean jouerCoup(Coup coup) {
        return moteur.coup(coup);
    }
}