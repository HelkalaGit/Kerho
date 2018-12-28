package Sali;

/**
 * Poikkeusluokka tietorankenteesta aiheutuville poikkeuksille.
 * @author Joel
 * @version Mar 13, 2017
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
     * käytettävä viesti
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}
