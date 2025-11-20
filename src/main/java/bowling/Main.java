package bowling;

public class Main {

	public static void main(String[] args) {
		IPartieMultiJoueurs partie = new PartieMultiJoueurs();
		String[] players = {"Pierre", "Paul"};

		System.out.println(partie.demarreNouvellePartie(players));

		System.out.println(partie.enregistreLancer(5));
		System.out.println(partie.enregistreLancer(3));
		System.out.println(partie.enregistreLancer(10));
		System.out.println(partie.enregistreLancer(7));
		System.out.println(partie.enregistreLancer(3));

		System.out.println("Score Pierre: " + partie.scorePour("Pierre"));
		System.out.println("Score Paul: " + partie.scorePour("Paul"));
	}
}
