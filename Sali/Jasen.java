package Sali;

import static kanta.HetuTarkistus.rand;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.HetuTarkistus;

/**
 * J�sen joka osaa mm. itse huolethia tunnus_nro:staan.
 * @author Joel
 * @version Mar 25, 2017
 */
public class Jasen implements Cloneable {
    
    private int tunnusNro;
    private int kaupunkiNro;
    private String nimi         = "";
    private String katuosoite   = "";
    private String kaupunki     = "";
    private String postinumero  = "";
    private String hetu         = "";
    private int liittynyt       = 0;
    private String lisatieto    = "";
    
    private static int seuraavaNro = 1;
    /**
     * Alustetaan j�sen. Toistaiseksi ei tehd� mit��n.
     */
    public Jasen() {
        // Viel� ei tarvita mit��n
    }
    
    /**
     * Palautetaan j�senen kenttien m��r�
     * @return kenttien m��r�
     */
    public int getKenttia() {
        return 9;
    }
    
    /**
     * Palautetaan eka muokkaukseen sopiva kentt�
     * @return kent�n numero
     */
    public int ekaKentta() {
        return 2;
    }
    
    /**
     * Antaa k:n kent�n sis�ll�n merkkijonona
     * @param k monennenko kent�n sis�lt� palautetaan
     * @return kent�n sis�lt� merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + kaupunkiNro;
        case 2: return nimi;
        case 3: return katuosoite;
        case 4: return kaupunki;
        case 5: return postinumero;
        case 6: return hetu;
        case 7: return "" + liittynyt;
        case 8: return lisatieto;
        default: return "Jonne oot!";
        }
    }
    
    /**
     * Palauttaa k:tta kaupungin kentt�� vastaavan kysymyksen
     * @param k kuinka monennen kent�n kysymys palautetaan
     * @return k:netta kentt�� vastaava kysymys
     */
    public String getKysymys(int k) {
        switch(k) {
        case 0: return "Tunnus nro";
        case 1: return "Kaupungin nro";
        case 2: return "Nimi";
        case 3: return "Katuosoite";
        case 4: return "Kaupunki";
        case 5: return "Postinumero";
        case 6: return "Hetu";
        case 7: return "Liittynyt";
        case 8: return "Lis�tietoja";
        default: return "Jonne oot!";
        }
    }
    
    /**
     * Asettaa k:n kent�n arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kent�n arvo asetetaan
     * @param jono jonoa joka asetetaan kent�n arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     */
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuilder sb = new StringBuilder(tjono);
        switch( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '�', getTunnusNro()));
            return null;
        case 1: 
            setKaupunkiNro(Mjonot.erota(sb, '�', getKaupunkiNro()));
            return null;
        case 2:
            nimi = tjono;
            return null;
        case 3:
            katuosoite = tjono;
            return null;
        case 4:
            kaupunki = tjono;
            return null;
        case 5: 
            postinumero = tjono;
            return null;
        case 6:
            HetuTarkistus hetut = new HetuTarkistus();
            String virhe = hetut.tarkista(tjono);
            if(virhe != null ) return virhe;
            hetu = tjono;
            return null;
        case 7:
            try {
                liittynyt = Mjonot.erotaEx(sb, '�', liittynyt);
            } catch (NumberFormatException ex ) {
                return "Liittymisvuosi v��rin " + ex.getMessage();
            }
            return null;
        case 8:
            lisatieto = tjono;
            return null;
        default: 
            return "Jonne oot!";
        }
    }

    /**
     * Asetetaan j�senen kaupungin nro
     * @param nro numero
     * @return null jos ei ongelmia
     */
    public String setKaupunkiNro(int nro) {
        kaupunkiNro = nro;
        return null;
    }
    
    /**
     * Alustetaan tietyn kaupungin j�sen
     * @param kaupunkiNro kaupungin viitenumero
     */
    public Jasen(int kaupunkiNro) {
        this.kaupunkiNro = kaupunkiNro;
    }
    
    /**
     * Apumetodi jolla saadaan t�ytetty� testiarvot J�senelle.
     * Aloitusvuosi arvataan, jotta kahdella j�senell� ei olisi 
     * samoja tietoja
     * @param nro viite kaupunkiin, jonka j�sen on
     */
    public void vastaaJonneViljami(int nro) {
        kaupunkiNro = nro;
        nimi = "Jonne Viljami";
        katuosoite = "Lanitie 13";
        kaupunki = "Kouvola";
        postinumero = "45710";
        hetu = "120394-081E";
        liittynyt = rand(1900, 2000);
        lisatieto = "n�rtti";
    }
    
    /**
     * Tulostetaan j�senen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro) + " " + nimi + " " + " " + hetu + tunnusNro);
        out.println(katuosoite + ", " + kaupunki + ", " + postinumero);
        out.println(liittynyt + " " + lisatieto);
    }
    
    /**
     * Tulostetaan henkil�n tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Antaa j�senelle seuraavan rekisterinumeron.
     * @return j�senen uusi tunnus_nro;
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    /**
     * Palautetaan j�senen oma id
     * @return j�senen id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Palautetaan j�senen nimi
     * @return nimi
     */
    public String getNimi() {
        return nimi;
    }

    
    /**
     * Palautetaan mihin kaupunkiin j�sen kuuluu
     * @return kaupungin id
     */
    public int getKaupunkiNro() {
        return kaupunkiNro;
    }
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa ett�
     * seuraava numero on aina suurempi kuin t�h�n menness� suurin
     * @param nr asetettava tunnusnumero
     */
    public void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }
    
    /**
     * Tehd��n j�senest� klooni muutoksia varten
     */
    @Override
    public Jasen clone() throws CloneNotSupportedException {
        Jasen uusi;
        uusi = (Jasen) super.clone();
        return uusi;
    }
    
    /**
     * Palauttaa j�senen tiedot merkkijonona jonka voi tallentaa tiedostoon
     * @return j�senen tolppaeroteltuna merkkijonona
     */
    @Override
    public String toString() {
        return "" + getTunnusNro() + "|" + kaupunkiNro + "|" + nimi + "|" +
                katuosoite + "|" + kaupunki + "|" + postinumero + "|" + 
                hetu + "|" + liittynyt + "|" + lisatieto;
    }
    
    /**
     * Selvitt�� j�senen tiedot | erotellusta merkkijonosta
     * @param rivi josta j�senen tiedot otetaan
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        kaupunkiNro = Mjonot.erota(sb,'|', kaupunkiNro);
        nimi = Mjonot.erota(sb, '|', nimi);
        katuosoite = Mjonot.erota(sb, '|', katuosoite);
        kaupunki = Mjonot.erota(sb, '|', kaupunki);
        postinumero = Mjonot.erota(sb, '|', postinumero);
        hetu = Mjonot.erota(sb, '|', hetu);
        liittynyt = Mjonot.erota(sb,'|',liittynyt);
        lisatieto = Mjonot.erota(sb, '|', lisatieto);
    }
    
    /**
     * Testiohjelma J�senelle
     * @param args ei k�yt�ss�
     */
    public static void main(String[] args) {
        Jasen jas = new Jasen(); 
        jas.vastaaJonneViljami(2);
        jas.tulosta(System.out);

    }

    
}
