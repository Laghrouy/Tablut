package Model.IA;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Model.Coup;
import Model.Moteur;

public class IAAleatoire extends IA {

    public IAAleatoire(Moteur moteur){
        super(moteur);
    }

    @Override
    public boolean choisirCoup() {
        Set<Coup> coupsPossibles = moteur.getCoupsPossibles();
        if (coupsPossibles.isEmpty()) return false;
        List<Coup> listCoupsPossibles = new ArrayList<>(coupsPossibles);
        Coup choisi = listCoupsPossibles.get(random.nextInt(coupsPossibles.size()));
        return moteur.coup(choisi);
    }
}
