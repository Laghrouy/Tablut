package Model.IA;

import java.util.Random;

import Model.Moteur;

public abstract class IA {

    public Moteur moteur;
    public Random random;

    public IA(Moteur moteur) {
        this.moteur = moteur;
        this.random = new Random();
    }

    public void setMoteur(Moteur moteur) {
        this.moteur = moteur;
    }

    public abstract boolean choisirCoup();
}
