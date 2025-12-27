<div align="center">
	<h1>Tablut - Jeu de stratégie viking</h1>
</div>

---

## 📝 Présentation

Ce projet est une implémentation complète du jeu Tablut (variante du Hnefatafl) en Java, avec interface graphique (Swing), intelligence artificielle (IA) multi-niveaux, architecture MVC, et tests unitaires.

---

## 📂 Structure du projet

```
src/
	Model/         # Logique du jeu, IA, pièces, plateau, etc.
		IA/          # IA MinMax, IA aléatoire, heuristique, niveaux
	Vue/           # Interface graphique Swing (plateau, menu, etc.)
	Controleur/    # Contrôleur MVC, adaptation vue/contrôleur
	Utils/         # Outils divers (historique, etc.)
	Tablut.java    # Point d'entrée principal (console)
res/
	Images/        # Ressources graphiques
	Data/          # Données diverses
test/            # Tests unitaires JUnit
README.md        # Ce fichier
qodana.yaml      # Configuration analyse statique
```

---

## 🚀 Compilation & Exécution

### Prérequis
- Java 17 ou supérieur (JDK recommandé)
- [JUnit 5](https://junit.org/junit5/) pour les tests

### Compilation (console)

```sh
javac -d bin src/**/*.java
```

### Lancement (console, IA vs IA)

```sh
java -cp bin Tablut
```

### Lancement (interface graphique)

Lancez la classe `Vue.Menu` ou exécutez le projet dans un IDE (IntelliJ, Eclipse...)

---

## 🧠 Intelligence Artificielle (IA)

- **IA Aléatoire** : choisit un coup au hasard parmi les coups légaux.
- **IA MinMax** : recherche du meilleur coup via MinMax avec élagage alpha-bêta, profondeur paramétrable (facile, moyen, difficile).
- **Heuristique** : évaluation du plateau selon la position du roi, la menace, la mobilité, etc.

---

## 🏗️ Architecture (MVC)

- **Model** : Plateau, pièces, règles, IA, moteur de jeu, historique.
- **Vue** : Interface graphique Swing (plateau, menu, surbrillance, etc.).
- **Contrôleur** : Gère les interactions utilisateur, synchronise la vue et le modèle.

---

## 🧪 Tests

Des tests unitaires sont présents dans `test/Model/PlateauTest.java` (JUnit 5).

Pour exécuter les tests :

```sh
cd test
javac -cp .:../src:PATH_TO_JUNIT5_JAR Model/PlateauTest.java
java -jar PATH_TO_JUNIT5_CONSOLE_LAUNCHER --class-path .:../src --scan-class-path
```

---

## 📜 Règles du jeu Tablut

### 🎯 But du jeu
Joueur blanc : Dirige le Roi et ses gardes. Il doit faire sortir le Roi vers n'importe quel bord du plateau.
Joueur noir : Commande les mercenaires. Il doit capturer le Roi.

### 🧩 Disposition initiale
Plateau de 9×9 cases. Le Roi est au centre (le trône), entouré de 8 gardes blancs. Les 16 mercenaires noirs sont positionnés autour.

### 🚶‍♂️ Déplacement des pièces
Les pièces se déplacent comme une tour aux échecs (horizontalement ou verticalement, sans limite de cases).
Pas de saut par-dessus d'autres pièces.
Une case ne peut être occupée que par une seule pièce.
Le trône devient inaccessible une fois quitté par le Roi.
Les pièces peuvent sauter "par-dessus" le trône sans s’y arrêter.

### ⚔️ Capture des pièces
Capturer en entourant une pièce adverse sur deux côtés (horizontalement ou verticalement).
Le Roi ne peut pas capturer.
Si une pièce se met entre deux ennemis, elle n’est pas capturée.

### 🏁 Fin de la partie
Victoire du joueur blanc : Le Roi atteint un bord du plateau.
Victoire du joueur noir : Le Roi est encerclé sur 4 côtés (ou 3 côtés + trône).
Défaite automatique : Un joueur sans coup légal perd.

### 📊 Statistiques (exemple)
Blancs : 58.04%
Noirs : 39.36%
Nulles : 2.58%

---

## 👥 Contributeurs

- [@gpoikitchi](https://github.com/gpoikitchi) — Messispa BOUSSAID
- [@TomGontard](https://github.com/TomGontard) — Tom Gontard
- [@Mouxouu](https://github.com/Mouxouu) — Mouxouu
- [@Laghrouy](https://github.com/Laghrouy) — laghrouy
