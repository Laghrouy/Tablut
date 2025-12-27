package Model;

import java.io.PipedOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class PlateauTest {

    @org.junit.jupiter.api.Test
    void testEquals() {

        /**************************************************************************************************/
        /************************************** INITIALSATION *********************************************/
        /**************************************************************************************************/
        
        // Plateau initiales
        Plateau plateauInit = new Plateau(true);
        Plateau plateauInit2 = new Plateau(true);

        /** Plateaux quelconques:
         * . . . . . . . . .
         * . . . D . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . D . . . . . .
         * A . . R . A . . .
         * . . . A . . . . .
         * . . . . . . . . .
         */
        Plateau plateauQuelconque = new Plateau(false);
        plateauQuelconque.setCase(new Coordonnee(1,3),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        plateauQuelconque.setCase(new Coordonnee(5,2),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        plateauQuelconque.setCase(new Coordonnee(6,0),new Case(TypeCase.SORTIE,Piece.ATTAQUANT));
        plateauQuelconque.setCase(new Coordonnee(6,3),new Case(TypeCase.NORMALE,Piece.ROI));
        plateauQuelconque.setCase(new Coordonnee(6,5),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        plateauQuelconque.setCase(new Coordonnee(7,3),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));

        Plateau plateauQuelconque2 = new Plateau(false);
        plateauQuelconque2.setCase(new Coordonnee(1,3),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        plateauQuelconque2.setCase(new Coordonnee(5,2),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        plateauQuelconque2.setCase(new Coordonnee(6,0),new Case(TypeCase.SORTIE,Piece.ATTAQUANT));
        plateauQuelconque2.setCase(new Coordonnee(6,3),new Case(TypeCase.NORMALE,Piece.ROI));
        plateauQuelconque2.setCase(new Coordonnee(6,5),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        plateauQuelconque2.setCase(new Coordonnee(7,3),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));

        /** Plateaux symetriques au plateau quelconque:
         * . . A . . . . . .
         * . . . . . . . . .
         * . . . D . . . . .
         * . A R . . . . D .
         * . . . . . . . . .
         * . . D . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         */
        Plateau symetrie1 = new Plateau(false);
        symetrie1.setCase(new Coordonnee(1,3),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie1.setCase(new Coordonnee(5,2),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie1.setCase(new Coordonnee(6,0),new Case(TypeCase.SORTIE,Piece.ATTAQUANT));
        symetrie1.setCase(new Coordonnee(6,3),new Case(TypeCase.NORMALE,Piece.ROI));
        symetrie1.setCase(new Coordonnee(6,5),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie1.setCase(new Coordonnee(7,3),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));

        /**
         * . . . . . . . . .
         * . . . . . A . . .
         * . . . A . R . . A
         * . . . . . . D . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . D . . .
         * . . . . . . . . .
         */
        Plateau symetrie2 = new Plateau(false);
        symetrie2.setCase(new Coordonnee(1,5),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie2.setCase(new Coordonnee(2,3),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie2.setCase(new Coordonnee(2,5),new Case(TypeCase.NORMALE,Piece.ROI));
        symetrie2.setCase(new Coordonnee(2,8),new Case(TypeCase.SORTIE,Piece.ATTAQUANT));
        symetrie2.setCase(new Coordonnee(3,6),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie2.setCase(new Coordonnee(7,5),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));

        /**
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . A . .
         * . . . . . . . . .
         * . D . . . . R A .
         * . . . . . D . . .
         * . . . . . . . . .
         * . . . . . . A . .
         */
        Plateau symetrie3 = new Plateau(false);
        symetrie3.setCase(new Coordonnee(3,6),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie3.setCase(new Coordonnee(5,1),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie3.setCase(new Coordonnee(5,6),new Case(TypeCase.NORMALE,Piece.ROI));
        symetrie3.setCase(new Coordonnee(5,7),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie3.setCase(new Coordonnee(6,5),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie3.setCase(new Coordonnee(8,6),new Case(TypeCase.SORTIE,Piece.ATTAQUANT));
        
        /**
         * . . . . . . . . .
         * . . . . . D . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . D . .
         * . . . A . R . . A
         * . . . . . A . . .
         * . . . . . . . . .
         */
        Plateau symetrie4 = new Plateau(false);
        symetrie4.setCase(new Coordonnee(1,5),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie4.setCase(new Coordonnee(5,6),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie4.setCase(new Coordonnee(6,3),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie4.setCase(new Coordonnee(6,5),new Case(TypeCase.NORMALE,Piece.ROI));
        symetrie4.setCase(new Coordonnee(6,8),new Case(TypeCase.SORTIE,Piece.ATTAQUANT));
        symetrie4.setCase(new Coordonnee(7,5),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        
        /**
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . A . . . . . .
         * . . . . . . . . .
         * . A R . . . . D .
         * . . . D . . . . .
         * . . . . . . . . .
         * . . A . . . . . .
         */
        Plateau symetrie5 = new Plateau(false);
        symetrie5.setCase(new Coordonnee(3,2),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie5.setCase(new Coordonnee(5,1),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie5.setCase(new Coordonnee(5,2),new Case(TypeCase.NORMALE,Piece.ROI));
        symetrie5.setCase(new Coordonnee(5,7),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie5.setCase(new Coordonnee(6,3),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie5.setCase(new Coordonnee(8,2),new Case(TypeCase.SORTIE,Piece.ATTAQUANT));
        
        /**
         * . . . . . . . . .
         * . . . A . . . . .
         * A . . R . A . . .
         * . . D . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . D . . . . .
         * . . . . . . . . .
         */
        Plateau symetrie6 = new Plateau(false);
        symetrie6.setCase(new Coordonnee(1,3),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie6.setCase(new Coordonnee(2,0),new Case(TypeCase.SORTIE,Piece.ATTAQUANT));
        symetrie6.setCase(new Coordonnee(2,3),new Case(TypeCase.NORMALE,Piece.ROI));
        symetrie6.setCase(new Coordonnee(2,5),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie6.setCase(new Coordonnee(3,2),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie6.setCase(new Coordonnee(7,3),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        
        /**
         * . . . . . . A . .
         * . . . . . . . . .
         * . . . . . D . . .
         * . D . . . . R A .
         * . . . . . . . . .
         * . . . . . . A . .
         * . . . . . . . . .
         * . . . . . . . . .
         * . . . . . . . . .
         */
        Plateau symetrie7 = new Plateau(false);
        symetrie7.setCase(new Coordonnee(0,6),new Case(TypeCase.SORTIE,Piece.ATTAQUANT));
        symetrie7.setCase(new Coordonnee(2,5),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie7.setCase(new Coordonnee(3,1),new Case(TypeCase.NORMALE,Piece.DEFENSEUR));
        symetrie7.setCase(new Coordonnee(3,6),new Case(TypeCase.NORMALE,Piece.ROI));
        symetrie7.setCase(new Coordonnee(3,7),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));
        symetrie7.setCase(new Coordonnee(5,6),new Case(TypeCase.NORMALE,Piece.ATTAQUANT));

        /**************************************************************************************************/
        /************************************** EQUALS CASES **********************************************/
        /**************************************************************************************************/

        // Plateaux de meme adresse
        assertEquals(plateauInit, plateauInit);

        // Plateaux initiales mais d'adresses differentes
        assertEquals(plateauInit, plateauInit2);

        // Plateaux quelconques identiques mais d'adresses differentes
        assertEquals(plateauQuelconque, plateauQuelconque2);

        // Plateau quelconque avec ses symetriques
        assertEquals(plateauQuelconque, symetrie1);
        assertEquals(plateauQuelconque, symetrie2);
        assertEquals(plateauQuelconque, symetrie3);
        assertEquals(plateauQuelconque, symetrie4);
        assertEquals(plateauQuelconque, symetrie5);
        assertEquals(plateauQuelconque, symetrie6);
        assertEquals(plateauQuelconque, symetrie7);

        /**************************************************************************************************/
        /************************************* NOT EQUALS CASES *******************************************/
        /**************************************************************************************************/

        // Plateau avec un objet non plateau
        assertNotEquals(plateauInit, new Object());

        // Plateau initiale avec plateau avec un pion en moins
        Plateau plateauMoinsUnPion = new Plateau(true);
        plateauMoinsUnPion.setCase(new Coordonnee(0,3),new Case(TypeCase.SORTIE,Piece.VIDE));
        assertNotEquals(plateauInit, plateauMoinsUnPion);


        // Plateau initiale avec plateau quelconque
        assertNotEquals(plateauInit, plateauQuelconque);
    }

}