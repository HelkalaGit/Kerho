package Sali;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Gymobs-luokka, joka huolehtii kaupungistosta. P‰‰osin kaikki metodit
 * ovat vain "v‰litt‰j‰metodeja" kaupungistoon
 * 
 * @author Joel
 * @version Mar 13, 2017
 */
public class Gymobs {
    
    private final Kaupungit kaupungit = new Kaupungit();
    private final Jasenet jasenet = new Jasenet();
    
    /**
     * Palauttaa gymin kaupunkim‰‰r‰n
     * @return kaupunkim‰‰r‰
     */
    public int getKaupunkeja() {
        return kaupungit.getLkm();
    }
    
    /**
     * Korvataan tai lis‰t‰‰n muokattu j‰sen
     * @param jasen muokattu j‰sen
     */
    public void korvaaTaiLisaa(Jasen jasen) {
        jasenet.korvaaTaiLisaa(jasen);
    }
    
    /**
     * Poistaa kaupungin salin ja siell‰ olevat j‰senet
     * @param kaupunki mik‰ kaupunki
     * @return monta poistettiin
     */
    public int poista(Kaupunki kaupunki) {
        if ( kaupunki == null ) return 0;
        int ret = kaupungit.poista(kaupunki.getTunnusNro());
        jasenet.poista(kaupunki.getTunnusNro());
        return ret;
    }
    
    /**
     * Poistetaan valittu j‰sen
     * @param jasen valittu j‰sen
     */
    public void poista(Jasen jasen) {
        if(jasen == null) return;
        jasenet.poista(jasen);
    }
    
    /**
     * Korvataan tai lis‰t‰‰n muokattu kaupunki
     * @param kaupunki muokattu kaupunki
     * @throws SailoException heitt‰‰ exception jos ei onnistu
     */
    public void korvaaTaiLisaa(Kaupunki kaupunki) throws SailoException {
        kaupungit.korvaaTaiLisaa(kaupunki);
    }
    
    /**
     * Lis‰‰ Gymobsiin uuden kaupungin
     * @param kaupunki lis‰tt‰v‰ kaupunki
     * @throws SailoException jos lis‰yst‰ ei voida tehd‰
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Gymobs gymobs = new Gymobs();
     * Kaupunki kvl1 = new Kaupunki(), kvl2 = new Kaupunki();
     * kvl1.rekisteroi(); kvl2.rekisteroi();
     * gymobs.getKaupunkeja() === 0;
     * gymobs.lisaa(kvl1); gymobs.getKaupunkeja() === 1;
     * gymobs.lisaa(kvl2); gymobs.getKaupunkeja() === 2;
     * gymobs.lisaa(kvl1); gymobs.getKaupunkeja() === 3;
     * gymobs.getKaupunkeja() === 3;
     * gymobs.annaKaupunki(0) === kvl1;
     * gymobs.annaKaupunki(1) === kvl2;
     * gymobs.annaKaupunki(2) === kvl1;
     * gymobs.annaKaupunki(3) === kvl1; #THROWS IndexOutOfBoundsException
     * gymobs.lisaa(kvl1); gymobs.getKaupunkeja() === 4;
     * gymobs.lisaa(kvl1); gymobs.getKaupunkeja() === 5;
     * gymobs.lisaa(kvl1);              #THROWS SailoException
     * </pre>
     */
    public void lisaa(Kaupunki kaupunki) throws SailoException {
        kaupungit.lisaa(kaupunki);
    }
    
    /**
     * Lis‰t‰‰n uusi j‰sen salille
     * @param jas lis‰tt‰v‰ j‰sen
     */
    public void lisaa(Jasen jas) {
        jasenet.lisaa(jas);
    }
    
    /**
     * Palauttaa i:n kaupungin
     * @param i monesko kaupunki palautetaan
     * @return viite i:teen kaupunkiin
     * @throws IndexOutOfBoundsException jos i v‰‰rin
     */
    public Kaupunki annaKaupunki(int i) throws IndexOutOfBoundsException {
        return kaupungit.anna(i);
    }
    
    /**
     * Haetaan kaikki kaupungin j‰senet
     * @param kaup kaupunki jolle j‰seni‰ haetaan
     * @return tietorakenne jossa viitteet lˆydettyihin harrastuksiin
     */
    public List<Jasen> annaJasenet(Kaupunki kaup) {
        return jasenet.annaJasenet(kaup.getTunnusNro());
    }
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi + "/";
        kaupungit.setTiedostonPerusNimi(hakemistonNimi + "kaupungit");
        jasenet.setTiedostonPerusNimi(hakemistonNimi + "nimet");
        //jasenet.setLogiTiedostonPerusNimi(hakemistonNimi + "logi");
    }
    
    /**
     * Lukee salin tiedot tiedostosta
     * @param nimi jota k‰ytet‰‰n lukemisessa
     * @throws SailoException jos lukeminen ep‰onnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        setTiedosto(nimi);
        
        kaupungit.lueTiedostosta(nimi);
        jasenet.lueTiedostosta(nimi);
        //jasenet.lueLogiTiedostosta(nimi);
        
    }
    
    /**
     * Tallettaa salin tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            kaupungit.tallenna();
        } catch (SailoException ex) {
            virhe = ex.getMessage();
        }
        
        try {
            jasenet.tallenna();
        } catch (SailoException ex) {
            virhe += ex.getMessage();
        }
        if( !"".equals(virhe) ) throw new SailoException(virhe);
    }
    
    /**
     * Etsit‰‰n hakuehtoihin sopivat kaupungit
     * @param hakuehto hakuehto
     * @param k mink‰ kaupungin tiedon mukaan etsit‰‰n
     * @return kaupungit jotka sopivat hakuehtoon
     * @throws SailoException poikkeus joka heitet‰‰n
     */
    public Collection<Kaupunki> etsi(String hakuehto, int k) throws SailoException{
        return kaupungit.etsi(hakuehto, k);
    }

    /**
     * Testiohjelma gymobsista
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Gymobs gymobs = new Gymobs();

        try {
            // gymobs.lueTiedostosta("mekka");
            
            Kaupunki kvl1 = new Kaupunki(), kvl2 = new Kaupunki();
            kvl1.rekisteroi();
            kvl1.vastaaKouvola();
            kvl2.rekisteroi();
            kvl2.vastaaKouvola();
            
            
            gymobs.lisaa(kvl1);
            gymobs.lisaa(kvl2);
            
            int id1 = kvl1.getTunnusNro();
            int id2 = kvl2.getTunnusNro();
            Jasen viljami11 = new Jasen(id1); viljami11.vastaaJonneViljami(id1); gymobs.lisaa(viljami11);
            Jasen viljami12 = new Jasen(id1); viljami12.vastaaJonneViljami(id1); gymobs.lisaa(viljami12);
            Jasen viljami21 = new Jasen(id1); viljami21.vastaaJonneViljami(id2); gymobs.lisaa(viljami21);
            Jasen viljami22 = new Jasen(id1); viljami22.vastaaJonneViljami(id2); gymobs.lisaa(viljami22);
            Jasen viljami23 = new Jasen(id1); viljami23.vastaaJonneViljami(id2); gymobs.lisaa(viljami23);
            
            System.out.println("==================== Gymobs testi ====================");
            
            for(int i = 0; i < gymobs.getKaupunkeja(); i++) {
                Kaupunki kaupunki = gymobs.annaKaupunki(i);
                System.out.println("Kaupunki paikassa: " + i);
                kaupunki.tulosta(System.out);
                List<Jasen> loytyneet = gymobs.annaJasenet(kaupunki);
                for(Jasen jasen : loytyneet) 
                    jasen.tulosta(System.out);
            }
        } catch (SailoException e) {
            System.err.println("Vikaa: "+ e.getMessage());
        }
        
    }
}
