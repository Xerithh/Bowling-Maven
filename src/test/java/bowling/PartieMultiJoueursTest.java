package bowling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe PartieMultiJoueurs
 */
class PartieMultiJoueursTest {
	
	private PartieMultiJoueurs partie;
	
	@BeforeEach
	void setUp() {
		partie = new PartieMultiJoueurs();
	}
	
	// ========== Tests de demarreNouvellePartie ==========
	
	@Test
	void testDemarreNouvellePartieAvecUnJoueur() {
		String[] joueurs = {"Pierre"};
		String resultat = partie.demarreNouvellePartie(joueurs);
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 1", resultat);
	}
	
	@Test
	void testDemarreNouvellePartieAvecDeuxJoueurs() {
		String[] joueurs = {"Pierre", "Paul"};
		String resultat = partie.demarreNouvellePartie(joueurs);
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 1", resultat);
	}
	
	@Test
	void testDemarreNouvellePartieAvecTableauNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			partie.demarreNouvellePartie(null);
		});
	}
	
	@Test
	void testDemarreNouvellePartieAvecTableauVide() {
		String[] joueurs = {};
		assertThrows(IllegalArgumentException.class, () -> {
			partie.demarreNouvellePartie(joueurs);
		});
	}
	
	// ========== Tests de enregistreLancer ==========
	
	@Test
	void testEnregistreLancerSansPartieEnCours() {
		assertThrows(IllegalStateException.class, () -> {
			partie.enregistreLancer(5);
		});
	}
	
	@Test
	void testEnregistreLancerDeuxLancersMemeTour() {
		String[] joueurs = {"Pierre"};
		partie.demarreNouvellePartie(joueurs);
		
		String resultat1 = partie.enregistreLancer(5);
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", resultat1);
		
		String resultat2 = partie.enregistreLancer(3);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", resultat2);
	}
	
	@Test
	void testEnregistreLancerStrike() {
		String[] joueurs = {"Pierre"};
		partie.demarreNouvellePartie(joueurs);
		
		String resultat = partie.enregistreLancer(10);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", resultat);
	}
	
	@Test
	void testAlternanceDeuxJoueurs() {
		String[] joueurs = {"Pierre", "Paul"};
		partie.demarreNouvellePartie(joueurs);
		
		// Pierre tour 1, boule 1
		String resultat1 = partie.enregistreLancer(5);
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", resultat1);
		
		// Pierre tour 1, boule 2 -> passe à Paul
		String resultat2 = partie.enregistreLancer(3);
		assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1", resultat2);
		
		// Paul strike -> passe à Pierre
		String resultat3 = partie.enregistreLancer(10);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", resultat3);
	}
	
	@Test
	void testExempleDuSujet() {
		String[] joueurs = {"Pierre", "Paul"};
		partie.demarreNouvellePartie(joueurs);
		
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", 
		             partie.enregistreLancer(5));
		assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1", 
		             partie.enregistreLancer(3));
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", 
		             partie.enregistreLancer(10));
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 2", 
		             partie.enregistreLancer(7));
		assertEquals("Prochain tir : joueur Paul, tour n° 2, boule n° 1", 
		             partie.enregistreLancer(3));
		
		assertEquals(18, partie.scorePour("Pierre"));
		assertEquals(10, partie.scorePour("Paul"));
	}
	
	// ========== Tests de scorePour ==========
	
	@Test
	void testScorePourJoueurInconnu() {
		String[] joueurs = {"Pierre", "Paul"};
		partie.demarreNouvellePartie(joueurs);
		
		assertThrows(IllegalArgumentException.class, () -> {
			partie.scorePour("Jacques");
		});
	}
	
	@Test
	void testScorePourInitial() {
		String[] joueurs = {"Pierre", "Paul"};
		partie.demarreNouvellePartie(joueurs);
		
		assertEquals(0, partie.scorePour("Pierre"));
		assertEquals(0, partie.scorePour("Paul"));
	}
	
	@Test
	void testScorePourApresQuelquesLancers() {
		String[] joueurs = {"Pierre", "Paul"};
		partie.demarreNouvellePartie(joueurs);
		
		partie.enregistreLancer(5);
		partie.enregistreLancer(3);
		assertEquals(8, partie.scorePour("Pierre"));
		
		partie.enregistreLancer(7);
		partie.enregistreLancer(2);
		assertEquals(9, partie.scorePour("Paul"));
	}
	
	// ========== Tests de partie terminée ==========
	
	@Test
	void testPartieTermineeAvecUnJoueur() {
		String[] joueurs = {"Pierre"};
		partie.demarreNouvellePartie(joueurs);
		
		// Jouer 10 tours avec des scores simples (pas de strike ni spare)
		for (int tour = 0; tour < 10; tour++) {
			partie.enregistreLancer(4);
			String resultat = partie.enregistreLancer(3);
			
			if (tour == 9) {
				// Dernier tour -> Partie terminée
				assertEquals("Partie terminée", resultat);
			}
		}
		
		assertEquals(70, partie.scorePour("Pierre")); // 7 * 10 = 70
	}
	
	@Test
	void testPartieTermineeAvecDeuxJoueurs() {
		String[] joueurs = {"Pierre", "Paul"};
		partie.demarreNouvellePartie(joueurs);
		
		// Jouer 10 tours pour chaque joueur
		for (int tour = 0; tour < 10; tour++) {
			// Pierre
			partie.enregistreLancer(4);
			partie.enregistreLancer(3);
			
			// Paul
			partie.enregistreLancer(5);
			String resultat = partie.enregistreLancer(2);
			
			if (tour == 9) {
				assertEquals("Partie terminée", resultat);
			}
		}
		
		assertEquals(70, partie.scorePour("Pierre"));
		assertEquals(70, partie.scorePour("Paul"));
	}
	
	@Test
	void testPartieTermineeAvecStrikeAuDixiemeTour() {
		String[] joueurs = {"Pierre"};
		partie.demarreNouvellePartie(joueurs);
		
		// 9 tours normaux
		for (int i = 0; i < 9; i++) {
			partie.enregistreLancer(4);
			partie.enregistreLancer(3);
		}
		
		// 10e tour avec strike -> 2 lancers bonus
		partie.enregistreLancer(10);
		partie.enregistreLancer(5);
		String resultat = partie.enregistreLancer(3);
		
		assertEquals("Partie terminée", resultat);
		assertEquals(81, partie.scorePour("Pierre")); // 9*7 + 18 = 81
	}
	
	@Test
	void testPartieTermineeAvecSpareAuDixiemeTour() {
		String[] joueurs = {"Pierre"};
		partie.demarreNouvellePartie(joueurs);
		
		// 9 tours normaux
		for (int i = 0; i < 9; i++) {
			partie.enregistreLancer(4);
			partie.enregistreLancer(3);
		}
		
		// 10e tour avec spare -> 1 lancer bonus
		partie.enregistreLancer(7);
		partie.enregistreLancer(3);
		String resultat = partie.enregistreLancer(5);
		
		assertEquals("Partie terminée", resultat);
		assertEquals(78, partie.scorePour("Pierre")); // 9*7 + 15 = 78
	}
	
	// ========== Tests avec strikes et spares ==========
	
	@Test
	void testScoreAvecStrike() {
		String[] joueurs = {"Pierre"};
		partie.demarreNouvellePartie(joueurs);
		
		// Tour 1: Strike
		partie.enregistreLancer(10);
		// Tour 2: 5 + 3
		partie.enregistreLancer(5);
		partie.enregistreLancer(3);
		
		// Score = 10 + 5 + 3 (bonus strike) + 5 + 3 = 26
		assertEquals(26, partie.scorePour("Pierre"));
	}
	
	@Test
	void testScoreAvecSpare() {
		String[] joueurs = {"Pierre"};
		partie.demarreNouvellePartie(joueurs);
		
		// Tour 1: Spare (7+3)
		partie.enregistreLancer(7);
		partie.enregistreLancer(3);
		// Tour 2: 5 + 2
		partie.enregistreLancer(5);
		partie.enregistreLancer(2);
		
		// Score = 10 + 5 (bonus spare) + 5 + 2 = 22
		assertEquals(22, partie.scorePour("Pierre"));
	}
	
	@Test
	void testPartieParfaite() {
		String[] joueurs = {"Pierre"};
		partie.demarreNouvellePartie(joueurs);
		
		// 12 strikes (9 tours + 3 lancers au 10e tour)
		for (int i = 0; i < 12; i++) {
			partie.enregistreLancer(10);
		}
		
		assertEquals(300, partie.scorePour("Pierre"));
	}
	
	@Test
	void testTroisJoueursAlternance() {
		String[] joueurs = {"Pierre", "Paul", "Jacques"};
		partie.demarreNouvellePartie(joueurs);
		
		// Tour 1 pour chaque joueur
		partie.enregistreLancer(5);  // Pierre boule 1
		partie.enregistreLancer(3);  // Pierre boule 2 -> passe à Paul
		
		// Vérifie les scores après le tour 1 de Pierre
		assertEquals(8, partie.scorePour("Pierre"));
		
		partie.enregistreLancer(10); // Paul strike -> passe à Jacques
		
		partie.enregistreLancer(7);  // Jacques boule 1
		partie.enregistreLancer(2);  // Jacques boule 2 -> passe à Pierre
		
		// Vérifie les scores après le tour 1 de chaque joueur
		assertEquals(8, partie.scorePour("Pierre"));
		assertEquals(10, partie.scorePour("Paul"));
		assertEquals(9, partie.scorePour("Jacques"));
		
		// Tour 2 de Pierre
		String resultat = partie.enregistreLancer(4);  // Pierre tour 2, boule 1
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 2", resultat);
		
		// Score de Pierre après le 1er lancer du tour 2
		assertEquals(12, partie.scorePour("Pierre")); // 8 + 4 = 12
	}
}
