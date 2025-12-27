package Controleur;

import Model.*;
import Utils.*;
import java.util.*;



public class ControlerJeu {
    private Moteur moteur;
    private Historique historique;

    public ControlerJeu(Moteur moteur) {
        this.moteur = moteur;
        this.historique = new Historique();
    }

    public boolean jouerCoup(Coup coup) {
        boolean res = moteur.coup(coup);
        if (res) historique.enregistrer(moteur.getPlateau(), coup, moteur.getJoueurActuel().getRole());
        return res;
    }

    public void jouerCoupIA() {
        // Implémentation pour jouer un coup IA
    }

    public Coup dernierCoup() {
        return historique.dernierCoup();
    }

    public boolean annulerCoup() {
        Historique.EtatJeu etat = historique.annulerCoup();
        if (etat == null) return false;
        moteur.setPlateau(etat.getPlateau());
        moteur.setJoueurActuel(etat.getJoueurActuel());
        return true;
    }

    public boolean rejouerCoup() {
        Historique.EtatJeu etat = historique.rejouerCoup();
        if (etat == null) return false;
        moteur.setPlateau(etat.getPlateau());
        moteur.setJoueurActuel(etat.getJoueurActuel());
        return true;
    }

    public boolean estPartieTerminee() {
        return moteur.estPartieTerminee();
    }

    public Set<Coup> getCoupsPossibles() {
        return moteur.getCoupsPossibles();
    }

    public boolean estDeplacementValide(Coup coup) {
        return moteur.estDeplacementValide(coup);
    }

    public Plateau getPlateau() {
        return moteur.getPlateau();
    }

    public Joueur getJoueurActuel() {
        return moteur.getJoueurActuel();
    }

    public void reinitialiser() {
        moteur.reinitialiser();
        historique = new Historique();
    }
}