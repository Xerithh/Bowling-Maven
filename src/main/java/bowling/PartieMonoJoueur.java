package bowling;

import static bowling.BowlingConstants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartieMonoJoueur {
	
	private final List<Tour> tours;
	private Tour tourCourant;
	
	public PartieMonoJoueur() {
		this.tours = new ArrayList<>(MAX_TOURS);
		demarrerNouveauTour();
	}
	
	private void demarrerNouveauTour() {
		if (tours.size() < MAX_TOURS) {
			tourCourant = new Tour(tours.size() + 1);
			tours.add(tourCourant);
		} else {
			tourCourant = null;
		}
	}

	public boolean enregistreLancer(int nombreDeQuillesAbattues) {
		if (estTerminee()) {
			throw new IllegalStateException("La partie est terminée");
		}
		
		Lancer lancer = new Lancer(nombreDeQuillesAbattues);
		boolean continuerTour = tourCourant.ajouterLancer(lancer);
		
		// Si le tour est terminé, on passe au suivant
		if (!continuerTour && tourCourant.estComplet()) {
			demarrerNouveauTour();
		}
		
		return continuerTour;
	}

	public int score() {
		int scoreTotal = 0;
		for (int i = 0; i < tours.size(); i++) {
			Tour tour = tours.get(i);

			if (tour.getNumeroTour() == MAX_TOURS) {
				scoreTotal += tour.getLancers().stream().mapToInt(Lancer::getQuillesAbattues).sum();
			} else {
				scoreTotal += tour.getScoreBase();
				
				if (tour.estStrike()) {
					scoreTotal += getBonusStrike(tours, i);
				} else if (tour.estSpare()) {
					scoreTotal += getBonusSpare(tours, i);
				}
			}
		}
		return scoreTotal;
	}

	private int getBonusStrike(List<Tour> tours, int indexTour) {
		int bonus = 0;
		if (indexTour + 1 < tours.size()) {
			Tour prochainTour = tours.get(indexTour + 1);
			
			// Premier lancer suivant
			if (!prochainTour.getLancers().isEmpty()) {
				bonus += prochainTour.getLancers().get(0).getQuillesAbattues();
			}
			
			// Deuxième lancer suivant
			if (prochainTour.estStrike() && prochainTour.getNumeroTour() < MAX_TOURS) {
				// Si le prochain est un strike (et pas le dernier tour), on doit aller chercher le suivant
				if (indexTour + 2 < tours.size()) {
					Tour tourSuivant = tours.get(indexTour + 2);
					if (!tourSuivant.getLancers().isEmpty()) {
						bonus += tourSuivant.getLancers().get(0).getQuillesAbattues();
					}
				}
			} else {
				// Sinon (pas strike ou c'est le dernier tour), on prend le 2ème lancer de ce tour
				if (prochainTour.getLancers().size() > 1) {
					bonus += prochainTour.getLancers().get(1).getQuillesAbattues();
				}
			}
		}
		return bonus;
	}

	private int getBonusSpare(List<Tour> tours, int indexTour) {
		int bonus = 0;
		if (indexTour + 1 < tours.size()) {
			Tour prochainTour = tours.get(indexTour + 1);
			if (!prochainTour.getLancers().isEmpty()) {
				bonus += prochainTour.getLancers().get(0).getQuillesAbattues();
			}
		}
		return bonus;
	}

	public boolean estTerminee() {
		return tours.size() == MAX_TOURS && 
		       (tourCourant == null || tourCourant.estComplet());
	}

	public int numeroTourCourant() {
		if (estTerminee()) {
			return 0;
		}
		return tourCourant != null ? tourCourant.getNumeroTour() : 0;
	}

	public int numeroProchainLancer() {
		if (estTerminee()) {
			return 0;
		}
		if (tourCourant == null) {
			return 0;
		}
		return tourCourant.getNombreLancers() + 1;
	}
	
	public List<Tour> getTours() {
		return Collections.unmodifiableList(tours);
	}
	
	public Tour getTourCourant() {
		return tourCourant;
	}
}
