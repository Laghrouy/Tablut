import Model.*;
import Model.IA.IA;
import Model.IA.IAHeuristique;
import Model.IA.IAMinMax;
import Model.IA.NiveauIA;

public class Tablut {
    public static void main(String[] args) {
        Plateau plateau = new Plateau();

        // Create IAs (moteur will be set after creation)
        IA iaGarde = new IAMinMax(null, NiveauIA.MOYEN);
        IA iaMercenaire = new IAMinMax(null, NiveauIA.MOYEN);

        // Create players with their IA
        Joueur mercenaire = new Joueur(TypeJoueur.MERCENAIRE, iaMercenaire);
        Joueur garde = new Joueur(TypeJoueur.GARDE, iaGarde);

        // Create the game engine with the correct order: mercenaire, garde
        Moteur moteur = new Moteur(plateau, mercenaire, garde);

        // Set the moteur reference in each IA (if needed)
        iaGarde.setMoteur(moteur);
        iaMercenaire.setMoteur(moteur);

        Partie partie = new Partie(garde, mercenaire);

        int tour = 1;
        System.out.println("Initial board:");
        System.out.println(partie.getMoteur().getPlateau());

        while (!partie.estPartieTerminee()) {
            System.out.println("Turn " + tour + " - Current player: " + partie.getJoueurActuel().getRole());
            boolean coupJoue = partie.getJoueurActuel().getIA().choisirCoup();
            System.out.println(partie.getMoteur().getPlateau());
            if (!coupJoue) {
                System.out.println("No possible move for " + partie.getJoueurActuel().getRole());
                break;
            }
            tour++;
        }

        if (partie.getMoteur().getPlateau().roiSurUnBord()) {
            System.out.println("Guards win (king escaped)!");
        } else if (partie.getMoteur().getPlateau().estRoiCapture()) {
            System.out.println("Mercenaries win (king captured)!");
        } else {
            System.out.println("Game ended with no winner.");
        }
    }
}