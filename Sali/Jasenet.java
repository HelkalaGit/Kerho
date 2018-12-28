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
 * Gymin jäsenet, joka osaa mm. lisätä uuden jäsenen
 * @author Joel
 * @version Mar 25, 2017
 */
public class Jasenet implements Iterable<Jasen>{
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";
    
    /** Taulukko jäsenistä */
    private final List<Jasen> alkiot = new ArrayList<Jasen>();
    
    /** Jäsenten alustaminen */
    public Jasenet() {
        // toistaiseksi ei tarvitse tehdä mitään
    }
    
    /**
     * Lisää uuden jäsenen tietorakenteeseen. Ottaa jäsenen omistukseensa.
     * @param jas lisättävä jäsen. Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Jasen jas) {
        alkiot.add(jas);
        muutettu = true;
    }
    
    
    /**
     * Lukee saliston tiedostosta
     * @param hakemisto tiedoston hakemisto 
     * @throws SailoException jos lukeminen epäonnistuu
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
     * @throws SailoException jos talletus epäonnistuu
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
     * Korvataan tai lisätään uusi jäsen
     * @param jasen muokattu tai uusi jäsen
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
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
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
     * Palauttaa salin jäsenten lukumäärän
     * @return jäsenten lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    /**
     * Iteraattori kaikkien jäsenten läpikäymiseen
     */
    @Override
    public Iterator<Jasen> iterator() {
        return alkiot.iterator();
    }
    
    /**
     * Haetaam kaikki kaupunki jäsenet
     * @return tietorakenne jossa viitteet löydettyihin harrastuksiin
     */
    public List<Jasen> annaJasenet() {
        List<Jasen> loydetyt = new ArrayList<Jasen>();
        for(Jasen jas : alkiot) 
            loydetyt.add(jas);
        return loydetyt;
    }
    
    
    /**
     * Haetaam kaikki kaupunki jäsenet
     * @param tunnusnro kaupungin tunnusnumero jolle jäseniä haetaan
     * @return tietorakenne jossa viitteet löydettyihin harrastuksiin
     */
    public List<Jasen> annaJasenet(int tunnusnro) {
        List<Jasen> loydetyt = new ArrayList<Jasen>();
        for(Jasen jas : alkiot) 
            if(jas.getKaupunkiNro() == tunnusnro) loydetyt.add(jas);
        return loydetyt;
    }
    
    /**
     * Testiohjelma jäsenille
     * @param args ei käytössä
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
        
        System.out.println("==================== Jäsenet testi ====================");
        
        List<Jasen> jasenet2 = jasenet.annaJasenet(2);
        
        for(Jasen jas : jasenet2) {
            System.out.println(jas.getKaupunkiNro() + " ");
            jas.tulosta(System.out);
        }
        
    }
    
    /**
     * Poistetaan valittu jäsen
     * @param jasen valittu jäsen
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
     * Poistaa tietyn kaupungin salin jäsenet
     * @param tunnusNro kaupungin tunnusnumero jonka jäsenet poistetaan
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
