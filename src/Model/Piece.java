/*
 * Enumération représentant les différents types de pièces sur le plateau :
 * - VIDE : case vide
 * - ROI : pièce centrale à protéger/fuir
 * - DEFENSEUR : garde du roi
 * - ATTAQUANT : mercenaire ennemi
 */

package Model;

public enum Piece {
    VIDE,
    ROI,
    DEFENSEUR,
    ATTAQUANT
}
