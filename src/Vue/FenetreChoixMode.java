package Vue;

import Controleur.GameInitializer;

import javax.swing.*;
import java.awt.*;

public class FenetreChoixMode {
    public interface GameModeListener {
        void ModeJvJ();
        void ModeJvIA();
        void ModeIAvIA();
    }

    private final JFrame Frame;

    public FenetreChoixMode(JFrame Frame) {
        this.Frame = Frame;
        CreerFenetre();
    }

    private void CreerFenetre() {
        JFrame frame = new JFrame("Choix du Mode de Jeu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        GameInitializer Jeu = new GameInitializer(Frame);

        JButton jvjButton = new JButton("Joueur vs Joueur");
        jvjButton.addActionListener(e -> {
            Jeu.ModeJvJ();
            frame.dispose();
        });

        JButton jviaButton = new JButton("Joueur vs IA");
        jviaButton.addActionListener(e -> {
            Jeu.ModeJvIA();
            frame.dispose();
        });

        JButton iaviaButton = new JButton("IA vs IA");
        iaviaButton.addActionListener(e -> {
            Jeu.ModeIAvIA();
            frame.dispose();
        });

        panel.add(jvjButton);
        panel.add(jviaButton);
        panel.add(iaviaButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}