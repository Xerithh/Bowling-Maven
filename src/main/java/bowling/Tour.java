package bowling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static bowling.BowlingConstants.*;

public class Tour {
    
    private final List<Lancer> lancers;
    private final int numeroTour; // 1 à 10
    private boolean complet;

    public Tour(int numeroTour) {
        if (numeroTour < 1 || numeroTour > MAX_TOURS) {
            throw new IllegalArgumentException("Numéro de tour invalide: " + numeroTour);
        }
        this.numeroTour = numeroTour;
        this.lancers = new ArrayList<>(3);
        this.complet = false;
    }
    
    public boolean ajouterLancer(Lancer lancer) {
        if (complet) {
            throw new IllegalStateException("Le tour est déjà complet");
        }
        
        lancers.add(lancer);
        
        // Dernier tour (10e): règles spéciales
        if (estDernierTour()) {
            return gererDernierTour();
        }
        
        // Tours 1-9: règles standard
        if (lancer.estStrike()) {
            complet = true;
            return false; // Strike: tour terminé
        }
        
        if (lancers.size() == 2) {
            complet = true;
            return false; // 2 lancers effectués: tour terminé
        }
        
        return true; // Premier lancer non-strike: le tour continue
    }
    
    private boolean gererDernierTour() {
        if (lancers.size() == 1) {
            if (lancers.get(0).estStrike()) {
                return true; // Strike au 1er lancer: 2 lancers bonus
            }
            return true; // Pas strike: on continue au 2e lancer
        }
        
        if (lancers.size() == 2) {
            // Si strike au 1er OU spare après 2 lancers: on continue
            if (lancers.get(0).estStrike() || estSpare()) {
                return true; // Droit à un 3e lancer
            }
            complet = true;
            return false; // Pas de bonus: tour fini
        }
        
        if (lancers.size() == 3) {
            complet = true;
            return false; // 3 lancers effectués: tour terminé
        }
        
        return true;
    }
    
    public boolean estDernierTour() {
        return numeroTour == MAX_TOURS;
    }
    
    public boolean estStrike() {
        return !lancers.isEmpty() && lancers.get(0).estStrike();
    }
    
    public boolean estSpare() {
        if (lancers.size() < 2) return false;
        int total = lancers.get(0).getQuillesAbattues() + lancers.get(1).getQuillesAbattues();
        return total == QUILLES_PAR_TOUR && !estStrike();
    }
    
    public FrameType getType() {
        if (estStrike()) return FrameType.STRIKE;
        if (estSpare()) return FrameType.SPARE;
        return FrameType.NORMAL;
    }
    
    public boolean estComplet() {
        return complet;
    }
    
    public int getNumeroTour() {
        return numeroTour;
    }
    
    public List<Lancer> getLancers() {
        return Collections.unmodifiableList(lancers);
    }
    
    public int getNombreLancers() {
        return lancers.size();
    }
    
    public Lancer getLancer(int index) {
        if (index < 0 || index >= lancers.size()) {
            return new Lancer(0); // Lancer par défaut si hors limites
        }
        return lancers.get(index);
    }

    public int getScoreBase() {
        return lancers.stream()
            .mapToInt(Lancer::getQuillesAbattues)
            .limit(2) // Ne compte que les 2 premiers lancers pour le score de base
            .sum();
    }
    
    @Override
    public String toString() {
        return String.format("Tour %d [%s] - %s", 
            numeroTour, 
            getType(), 
            lancers);
    }
}
