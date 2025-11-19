package bowling;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        PartieMonoJoueur partie = new PartieMonoJoueur();
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Bowling - Partie MonoJoueur. Entrez le nombre de quilles abattues par lancer (0-10). 'q' pour quitter.");

            while (!partie.estTerminee()) {
                System.out.printf("Tour %d, prochain lancer %d. Score courant: %d%n",
                        partie.numeroTourCourant(), partie.numeroProchainLancer(), partie.score());

                System.out.print("> ");
                String line = sc.nextLine().trim();
                if (line.equalsIgnoreCase("q") || line.equalsIgnoreCase("quit")) {
                    System.out.println("Abandon de la partie.");
                    break;
                }

                try {
                    int quilles = Integer.parseInt(line);
                    if (quilles < 0 || quilles > 10) {
                        System.out.println("Veuillez entrer un entier entre 0 et 10.");
                        continue;
                    }
                    boolean memeTour = partie.enregistreLancer(quilles);
                    if (memeTour) {
                        System.out.println("Même tour — lancez à nouveau.");
                    } else {
                        System.out.println("Tour terminé — passage au tour suivant.");
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Entrée invalide — entrez un nombre (0-10) ou 'q' pour quitter.");
                } catch (IllegalStateException ise) {
                    System.out.println("Impossible d'enregistrer le lancer : la partie est terminée.");
                    break;
                }
            }

            System.out.printf("Partie terminée. Score final : %d%n", partie.score());
        }
    }
}
