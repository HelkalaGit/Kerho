package Sali;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Salin kaupunki joka osaa mm. itse huolehtia tunnusNro:staan.
 * 
 * @author Joel
 * @version Mar 13, 2017
 */
public class Kaupunki implements Cloneable{

   private int      tunnusNro;
   private String   nimi            = "";
   private String   kaupunki        = "";
   private String   yhtHenkilo      = "";
   private String   katuosoite      = "";
   private String   postinumero     = "";
   private String   sahkoposti      = "";
   private String   puhelin         = "";
   private int      perustettu      = 0;
   
   private static int seuraavaNro   = 1;
   
   /**
    * Palauttaa k:tta kaupungin kenttää vastaavan kysymyksen
    * @param k kuinka monennen kentän kysymys palautetaan
    * @return k:netta kenttää vastaava kysymys
    */
   public String getKysymys(int k) {
       switch(k) {
       case 0: return "Tunnus nro";
       case 1: return "Nimi";
       case 2: return "Kaupunki";
       case 3: return "Yhteys henk.";
       case 4: return "Katuosoite";
       case 5: return "Postinumero";
       case 6: return "Sahkoposti";
       case 7: return "Puhelin";
       case 8: return "Perustettu";
       default: return "Jonne oot!";
       }
   }
   
   /**
    * Antaa k:n kentän sisällön merkkijonona
    * @param k monennenko kentän sisältö palautetaan
    * @return kentän sisältö merkkijonona
    */
   public String anna(int k) {
       switch ( k ) {
       case 0: return "" + tunnusNro;
       case 1: return nimi;
       case 2: return kaupunki;
       case 3: return yhtHenkilo;
       case 4: return katuosoite;
       case 5: return postinumero;
       case 6: return sahkoposti;
       case 7: return puhelin;
       case 8: return "" + perustettu;
       default: return "Jonne oot!";
       }
   }
   
   /**
    * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
    * @param k kuinka monennen kentän arvo asetetaan
    * @param jono jonoa joka asetetaan kentän arvoksi
    * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
    * @example
    * <pre name="test">
    * Kaupunki kvl = new Kaupunki();
    * kvl.aseta(1,"Kouvola") === null;
    * kvl.aseta(8, "kouvola") === "Perustusvuosi väärin jono = \"kouvola\"";
    * </pre>
    */
   public String aseta(int k, String jono) {
       String tjono = jono.trim();
       StringBuilder sb = new StringBuilder(tjono);
       switch( k ) {
       case 0:
           setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
           return null;
       case 1: 
           nimi = tjono;
           return null;
       case 2:
           kaupunki = tjono;
           return null;
       case 3:
           yhtHenkilo = tjono;
           return null;
       case 4:
           katuosoite = tjono;
           return null;
       case 5: 
           postinumero = tjono;
           return null;
       case 6:
           sahkoposti = tjono;
           return null;
       case 7:
           puhelin = tjono;
           return null;
       case 8:
           try {
               perustettu = Mjonot.erotaEx(sb, '§', perustettu);
           } catch (NumberFormatException ex ) {
               return "Perustusvuosi väärin " + ex.getMessage();
           }
           return null;
       default: 
           return "Jonne oot!";
       }
   }

   
   /**
    * Palauttaa kaupunkien kenttien lukumäärän 
    * @return kenttien lukumäärä
    */
   public int getKenttia() {
       return 9;
   }
   
   /**
    * Eka kenttä joka on mielekäs kysyttäväksi
    * @return ekan kentän indeksi
    */
   public int ekaKentta() {
       return 1;
   }
   
   /**
    * Laitetaan kaupungille uusi nimi
    * @param s uusi nimi
    * @return null jos ei ongelmia
    */
   public String setNimi(String s) {
       nimi = s;
       return null;
   }
   
   /**
    * Laitetaan kaupungille uusi katuosoite
    * @param s uusi katuosoite
    * @return null jos ei ongelmia
    */
   public String setKatuosoite(String s) {
       katuosoite = s;
       return null;
   }
   
   /**
    * Laitetaan kaupungille uusi kaupunki  
    * @param s uusi kaupunki
    * @return null jos ei ongelmia
    */
   public String setKaupunki(String s) {
       kaupunki = s;
       return null;
   }
   
   /**
    * Laitetaan kaupungille uusi postinumero
    * @param s Uusi postinumero
    * @return null jos ei ongelmia
    */
   public String setPostinumero(String s){
       if(!s.matches("[0-9]") ) return "Postinumeron oltava numeerinen";
       postinumero = s;
       return null;
   }
   
   /**
    * @return palauttaa salin kaupungin
    * @example
    * <pre name="test">
    * Kaupunki kvl = new Kaupunki();
    * kvl.vastaaKouvola();
    * kvl.getNimi() =R= "1";
    * </pre>
    */
   public String getNimi() {
       return kaupunki;
   }
   
   /**
    * @return palauttaa salin postinumeron
    * @example
    * <pre name="test">
    * Kaupunki kvl = new Kaupunki();
    * kvl.vastaaKouvola();
    * kvl.getPostinumero() =R= "1";
    * </pre>
    */
   public String getPostinumero() {
       return postinumero;
   }
   
   /**
    * @return palauttaa salin kaupungin
    * @example
    * <pre name="test">
    * Kaupunki kvl = new Kaupunki();
    * kvl.vastaaKouvola();
    * kvl.getKaupunki() =R= "1";
    * </pre>
    */
   public String getKaupunki() {
       return kaupunki;
   }
   
   /**
    * @return palauttaa salin katuosoitteen
    */
   public String getKatuosoite() {
       return katuosoite;
   }
    
   /**
    * Tehdään kaupungista klooni muutoksia varten
    */
   @Override
   public Kaupunki clone() throws CloneNotSupportedException {
       Kaupunki uusi;
       uusi = (Kaupunki) super.clone();
       return uusi;
   }
   
   /**
    * Asettaa tunnusnumeron ja samalla varmistaa että
    * seuraava numero on aina suurempi kuin tähän mennessä suurin
    * @param nr asetettava tunnusnumero
    */
   private void setTunnusNro(int nr) {
       tunnusNro = nr;
       if(tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
   }
   
   /**
    * Palauttaa salin tiedot merkkijonona jonka voi tallentaa tiedostoon
    * @return sali tolppaeroteltuna merkkijonona
    * @example
    * <pre name="test">
    * Kaupunki kaupunki = new Kaupunki();
    * kaupunki.parse("  3 | Mekka | Kouvola");
    * kaupunki.toString().startsWith("3|Mekka|Kouvola|") === true;
    * </pre>
    */
   @Override
   public String toString() {
       StringBuilder sb = new StringBuilder("");
       String erotin = "";
       for(int k = 0; k < getKenttia(); k++) {
           sb.append(erotin);
           sb.append(anna(k));
           erotin = "|";
       }
       return sb.toString();
   }
   
   /**
    * Selvittää salin tiedot | erotellusta merkkijonosta
    * @param rivi josta salin tiedot otetaan
    */
   public void parse(String rivi) {
       StringBuilder sb = new StringBuilder(rivi);
       for(int k = 0; k < getKenttia(); k++){
           aseta(k, Mjonot.erota(sb, '|'));
       }
   }
   
   /**
    * Apumetodi, jolla saadaan täytettyä testiarvot kaupungille
    */
   public void vastaaKouvola() {
       nimi = "1"; 
       kaupunki = "1";
       yhtHenkilo = "";
       katuosoite = "";
       postinumero = "1";
       sahkoposti = "";
       puhelin = "";
       perustettu = 0;
   }
   
    /**
     * Tulostetaan kaupungin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d",  tunnusNro) + "  " + nimi + "  "
                + yhtHenkilo);
        out.println("  " + katuosoite + "  " + postinumero + "  " + kaupunki);
        out.println("  " + puhelin + "  " + sahkoposti);
        out.println("  Perustettu " + perustettu);
    }
    
    /**
     * Tulostetaan kaupungin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Antaa kaupungille seuraavan rekisterinumeron
     * @return kaupungin uusi tunnusNro
     * @example
     * <pre name="test">
     * Kaupunki kvl1 = new Kaupunki();
     * kvl1.getTunnusNro() === 0;
     * kvl1.rekisteroi();
     * Kaupunki kvl2 = new Kaupunki();
     * kvl2.rekisteroi();
     * int n1 = kvl1.getTunnusNro();
     * int n2 = kvl2.getTunnusNro();
     * n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        if( tunnusNro != 0) return tunnusNro;
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return 0;
    }
    
    /**
     * Palauttaa kaupungin tunnusnumeron.
     * @return kaupungin tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Pieni testiohjelma Kaupunki-luokalle
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kaupunki kvl = new Kaupunki();
        Kaupunki kvl2 = new Kaupunki();
        
        kvl.rekisteroi();
        kvl2.rekisteroi();
        
        kvl.vastaaKouvola();
        kvl.tulosta(System.out);
        kvl2.vastaaKouvola();
        kvl2.tulosta(System.out);

    }

}
