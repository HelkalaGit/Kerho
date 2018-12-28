package Sali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Gymin j�senet, joka osaa mm. lis�t� uuden j�senen
 * @author Joel
 * @version Mar 25, 2017
 */
public class Jasenet implements Iterable<Jasen>{
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";
    
    /** Taulukko j�senist� */
    private final List<Jasen> alkiot = new ArrayList<Jasen>();
    
    /** J�senten alustaminen */
    public Jasenet() {
        // toistaiseksi ei tarvitse tehd� mit��n
    }
    
    /**
     * Lis�� uuden j�senen tietorakenteeseen. Ottaa j�senen omistukseensa.
     * @param jas lis�tt�v� j�sen. Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Jasen jas) {
        alkiot.add(jas);
        muutettu = true;
    }
    
    
    /**
     * Lukee saliston tiedostosta
     * @param hakemisto tiedoston hakemisto 
     * @throws SailoException jos lukeminen ep�onnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        alkiot.clear();
        File theDir = new File(hakemisto);
        if(!theDir.exists()) {
            theDir.mkdir();
        }
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            
            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Jasen jas = new Jasen();
                jas.parse(rivi);
                lisaa(jas);
            }
            muutettu = false;
            
        } catch ( FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    /**
     * @throws SailoException exception jos ei onnistu
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonNimi());
    }
    
    /**
     * Tallentaa saliston tiedostoon.
     * @throws SailoException jos talletus ep�onnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for ( Jasen  jas : alkiot ) {
                fo.println(jas.toString());
            }
        } catch (FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedosto " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }
    
    /**
     * Korvataan tai lis�t��n uusi j�sen
     * @param jasen muokattu tai uusi j�sen
     */
    public void korvaaTaiLisaa(Jasen jasen) {
        int id = jasen.getTunnusNro();
        //List<Jasen> jasenet = annaJasenet();
        for(int i = 0; i < alkiot.size(); i++) {
            if(alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, jasen);
                muutettu = true;
                return;
            }
        }
        lisaa(jasen);
    }
    
    /**
     * Asettaa tiedoston perusnimen 
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota k�ytet��n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    /**
     * Palauttaa salin j�senten lukum��r�n
     * @return j�senten lukum��r�
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    /**
     * Iteraattori kaikkien j�senten l�pik�ymiseen
     */
    @Override
    public Iterator<Jasen> iterator() {
        return alkiot.iterator();
    }
    
    /**
     * Haetaam kaikki kaupunki j�senet
     * @return tietorakenne jossa viitteet l�ydettyihin harrastuksiin
     */
    public List<Jasen> annaJasenet() {
        List<Jasen> loydetyt = new ArrayList<Jasen>();
        for(Jasen jas : alkiot) 
            loydetyt.add(jas);
        return loydetyt;
    }
    
    
    /**
     * Haetaam kaikki kaupunki j�senet
     * @param tunnusnro kaupungin tunnusnumero jolle j�seni� haetaan
     * @return tietorakenne jossa viitteet l�ydettyihin harrastuksiin
     */
    public List<Jasen> annaJasenet(int tunnusnro) {
        List<Jasen> loydetyt = new ArrayList<Jasen>();
        for(Jasen jas : alkiot) 
            if(jas.getKaupunkiNro() == tunnusnro) loydetyt.add(jas);
        return loydetyt;
    }
    
    /**
     * Testiohjelma j�senille
     * @param args ei k�yt�ss�
     */
    public static void main(String[] args) {
        Jasenet jasenet = new Jasenet();
        Jasen viljami1 = new Jasen();
        viljami1.vastaaJonneViljami(2);
        Jasen viljami2 = new Jasen();
        viljami2.vastaaJonneViljami(1);
        Jasen viljami3 = new Jasen();
        viljami3.vastaaJonneViljami(2);
        Jasen viljami4 = new Jasen();
        viljami4.vastaaJonneViljami(2);
        
        jasenet.lisaa(viljami1);
        jasenet.lisaa(viljami2);
        jasenet.lisaa(viljami3);
        jasenet.lisaa(viljami4);
        
        System.out.println("==================== J�senet testi ====================");
        
        List<Jasen> jasenet2 = jasenet.annaJasenet(2);
        
        for(Jasen jas : jasenet2) {
            System.out.println(jas.getKaupunkiNro() + " ");
            jas.tulosta(System.out);
        }
        
    }
    
    /**
     * Poistetaan valittu j�sen
     * @param jasen valittu j�sen
     */
    public void poista(Jasen jasen) {
        for(Iterator<Jasen> it = alkiot.iterator(); it.hasNext();) {
            Jasen jas = it.next();
            if(jas.getTunnusNro() == jasen.getTunnusNro()) {
                it.remove();
                muutettu = true;
            }
        }
    }


    /**
     * Poistaa tietyn kaupungin salin j�senet
     * @param tunnusNro kaupungin tunnusnumero jonka j�senet poistetaan
     * @return monta poistettiin
     */
    public int poista(int tunnusNro) {
        int n = 0;
        for(Iterator<Jasen> it = alkiot.iterator(); it.hasNext();) {
            Jasen jas = it.next();
            if(jas.getKaupunkiNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if ( n > 0 ) muutettu = true;
        return n;
    }

}
