package Sali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Salin kaupungisto joka osaa mm. lis‰t‰ uuden kaupungin
 * 
 * @author Joel
 * @version Mar 13, 2017
 */
public class Kaupungit {

    private static int          MAX_KAUPUNKEJA = Integer.MAX_VALUE;
    private int                 lkm;
    private String              kokoNimi = "";
    private String tiedostonPerusNimi = "";
    private Kaupunki[] alkiot = new Kaupunki[3];
    private boolean muutettu = false;
    
    
    /** 
     * Lis‰‰ uuden kaupungin tietorakenteeseen. Ottaa kaupungin omistukseensa.
     * @param kaupunki lis‰tt‰v‰n kaupungin viite. Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException poikkeus joka heitet‰‰n
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Kaupungit kaupungit = new Kaupungit();
     * Kaupunki kvl1 = new Kaupunki(), kvl2 = new Kaupunki();
     * kaupungit.getLkm() === 0;
     * kaupungit.lisaa(kvl1); kaupungit.getLkm() === 1;
     * kaupungit.lisaa(kvl2); kaupungit.getLkm() === 2;
     * kaupungit.lisaa(kvl1); kaupungit.getLkm() === 3;
     * kaupungit.anna(0) === kvl1;
     * kaupungit.anna(1) === kvl2;
     * kaupungit.anna(2) === kvl1;
     * kaupungit.anna(1) == kvl1 === false;
     * kaupungit.anna(1) == kvl2 === true;
     * kaupungit.lisaa(kvl1); kaupungit.getLkm() === 4;
     * kaupungit.lisaa(kvl1); kaupungit.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Kaupunki kaupunki) throws SailoException {
        if (lkm >= MAX_KAUPUNKEJA)  throw new SailoException("Liikaa alkioita");
        if ( lkm >= alkiot.length ) alkiot = Arrays.copyOf(alkiot, alkiot.length * 2);
        alkiot[lkm++] = kaupunki;
        muutettu = true;
    }
    
    /**
     * Poistaa kaupungin jolla on valittu tunnusnumero
     * @param id kaupungin tunnusnumero
     * @return 1 jos onnistu, 0 jos ei
     */
    public int poista(int id) {
        int ind = etsiId(id);
            if (ind < 0 ) return 0;
            lkm--;
            for(int i = ind; i < lkm; i++) 
                alkiot[i] = alkiot[i+1];
            alkiot[lkm] = null;
            muutettu = true;
            return 1;
    }
    
    
    /**
     * Palauttaa viitteen i:teen kaupunkiin.
     * @param i monennenko kaupungin viite halutaan
     * @return viite kaupunkiin, jonda indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Kaupunki anna(int i) throws IndexOutOfBoundsException {
        if ( i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /**
     * Palauttaa salin kaupunkien lukum‰‰r‰n
     * @return kaupunkien lukum‰‰r‰
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Lukee saliston tiedostosta
     * @param hakemisto tiedoston hakemisto 
     * @throws SailoException jos lukeminen ep‰onnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        alkiot = new Kaupunki[5];
        lkm = 0;
        File theDir = new File(hakemisto);
        if(!theDir.exists()) {
            theDir.mkdir();
        }
        
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            kokoNimi = fi.readLine();
            if(kokoNimi == null ) throw new SailoException("Salin nimi puuttuu");
            String rivi = fi.readLine();
            if(rivi == null ) throw new SailoException("Maksimikoko puuttuu");
            
            while ( (rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Kaupunki kaupunki = new Kaupunki();
                kaupunki.parse(rivi); 
                lisaa(kaupunki);
            }
            muutettu = false;
        } catch (FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch (IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    /**
     * @throws SailoException heitt‰‰ exception jos ei onnistu
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    /** 
     * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen 
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }
    
    /**
     * Tallentaa saliston tiedostoon.
     * @throws SailoException jos talletus ep‰onnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(alkiot.length);
            for ( Kaupunki kaupunki : alkiot ) {// menee 3. alkioon vaikka on vain 2 kaupunkia
                if(kaupunki == null) continue;
                fo.println(kaupunki.toString());
            }
            
        } catch (FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        
        muutettu = false;
    }
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    /**
     * Palauttaa salin koko nimen
     * @return Salin koko nimi merkkijonona
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    /**
     * Etsii kaupungin id:n perusteella
     * @param id id
     * @return indeksin jos lˆytyy tai -1 jos ei
     */
    public int etsiId(int id) {
        for(int i = 0; i < lkm; i++) 
            if(id == alkiot[i].getTunnusNro()) return i;
        return -1;
    }
    
    /**
     * Testiohjelma kaupungistolle
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Kaupungit kaupungit = new Kaupungit();
        
        Kaupunki kvl = new Kaupunki();
        Kaupunki kvl2 = new Kaupunki();
        
        kvl.rekisteroi();
        kvl2.rekisteroi();
        
        try {
            kaupungit.lisaa(kvl);
            kaupungit.lisaa(kvl2); kvl2.vastaaKouvola();
        } catch (SailoException e) {
            // 
        System.err.println(e.getMessage());
        } kvl.vastaaKouvola();
        
        System.out.println("===================== Kaupungit testi =====================");
        
        for (int i = 0; i < kaupungit.getLkm(); i++) {
            Kaupunki kaupunki = kaupungit.anna(i);
            System.out.println("Kaupunki nro: " + i);
            kaupunki.tulosta(System.out);
        }
    }
    
    /**
     * Etsit‰‰n kaupunkeja jotka sopivat hakuehtohin
     * @param hakuehto hakuehto jota etsit‰‰n kaupungista
     * @param kentta mink‰ kent‰n perusteella etsit‰‰n
     * @return lˆydetyt sopivat kaupungit
     */
    public Collection<Kaupunki> etsi(String hakuehto, int kentta) {
        String ehto = "*";
        if(hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto;
        Collection<Kaupunki> loytyneet = new ArrayList<Kaupunki>();
        for(int i = 0; i < alkiot.length; i++) {
            if(alkiot[i] == null)continue;
            if(WildChars.onkoSamat(alkiot[i].anna(kentta), ehto)) loytyneet.add(alkiot[i]);
        }
        return loytyneet;
    }
    
    /**
     * Korvataan tai lis‰t‰‰n muokattu kaupunki
     * @param kaupunki muokattu kaupunki
     * @throws SailoException exception heitto
     */
    public void korvaaTaiLisaa(Kaupunki kaupunki) throws SailoException {
        int id = kaupunki.getTunnusNro();
        for(int i = 0; i < lkm; i++) {
            if(alkiot[i].getTunnusNro() == id) {
                alkiot[i] = kaupunki;
                muutettu = true;
                return;
            }
        }
        lisaa(kaupunki);
    }

}
