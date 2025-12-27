/*
 * Représente une case individuelle du plateau de jeu.
 * Contient un type (NORMALE, TRONE, SORTIE) et une pièce (ou VIDE).
 * Fournit des méthodes utilitaires pour vérifier l'état de la case.
 */

package Model;

import java.io.Serializable;

public class Case implements Cloneable, Serializable {
    private TypeCase type;
    private Piece piece;

    public Case(TypeCase type) {
        this.type = type;
        this.piece = Piece.VIDE;
    }

    public Case(TypeCase type, Piece piece) {
        this.type = type;
        this.piece = piece;
    }

    public TypeCase getType(){
        return this.type;
    }

    public void setType(TypeCase type){
        this.type = type;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece pice){
        this.piece = pice;
    }
    public boolean estVide() {
        return piece == Piece.VIDE;
    }

    public boolean estOccupee() {
        return piece != Piece.VIDE;
    }

    public boolean estTrone() {
        return type == TypeCase.TRONE;
    }

    public boolean estSortie() {
        return type == TypeCase.SORTIE;
    }

    public Case clone() {
        return new Case(this.type, this.piece); // Copy type and piece
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Case that = (Case) obj;
        return this.type == that.getType() && this.piece == that.getPiece();
    }
}
