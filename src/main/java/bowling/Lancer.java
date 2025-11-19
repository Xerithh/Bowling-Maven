package bowling;

public class Lancer {
    
    private final int quillesAbattues;
    
    public Lancer(int quillesAbattues) {
        if (quillesAbattues < 0 || quillesAbattues > BowlingConstants.QUILLES_PAR_TOUR) {
            throw new IllegalArgumentException(
                "Nombre de quilles invalide: " + quillesAbattues + 
                ". Doit être entre 0 et " + BowlingConstants.QUILLES_PAR_TOUR);
        }
        this.quillesAbattues = quillesAbattues;
    }
    
    public int getQuillesAbattues() {
        return quillesAbattues;
    }
    
    public boolean estStrike() {
        return quillesAbattues == BowlingConstants.QUILLES_PAR_TOUR;
    }
    
    @Override
    public String toString() {
        return String.valueOf(quillesAbattues);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lancer lancer = (Lancer) o;
        return quillesAbattues == lancer.quillesAbattues;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(quillesAbattues);
    }
}
