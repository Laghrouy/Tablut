package Model.IA;

public enum NiveauIA {
    FACILE(2),
    MOYEN(4),
    DIFFICILE(6);

    private final int profondeur;

    NiveauIA(int profondeur) {
        this.profondeur = profondeur;
    }

    public int getProfondeur() {
        return profondeur;
    }
}
