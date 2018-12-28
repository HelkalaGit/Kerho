package HT2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import Sali.Gymobs;
import Sali.Jasen;
import Sali.Kaupunki;
import Sali.SailoException;
import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import static HT2.ControllerMuokkaaSali.*;

/**
 * @author Joel
 * @version Feb 14, 2017
 * Luokka k‰yttˆliittym‰n tapahtumien hoitamiseksi
 */
public class ControllerValikko implements Initializable {
    
    @FXML private ListChooser<Kaupunki> chooserKaupungit;
    @FXML private ListChooser<Jasen> chooserJasenet;
    @FXML private ScrollPane panelJasen;
    @FXML private TextField hakuehto;
    @FXML private TextField hakeEhtoJasen;
    @FXML private TextArea editTapahtuma;
    @FXML private GridPane gridKaupunki;
    @FXML private ComboBoxChooser<String> cbKentat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alusta();
    }
    
    @FXML private void handleAvaa() {
        avaa();
    }

    @FXML private void handleApua() {
        ModalController.showModal(ControllerValikko.class.getResource("ApuaView.fxml"),
                "Apua", null, "");//
    }
    
    @FXML private void handleTietoja() {
        ModalController.showModal(ControllerValikko.class.getResource("TietojaView.fxml"),
                "Tietoja", null, ""); //
    }

    @FXML private void handleLisaaAsiakas() {
        uusiJasen();
    }

    @FXML private void handleLisaaSali() {
        uusiSali();
    }

    @FXML private void handleLopeta() {
        tallenna();
        Platform.exit();
    }

    @FXML private void handleMuokkaaSali() {
        muokkaaSali(kentta);
    }

    @FXML private void handlePoistaAsiakas() {
        poistaJasen();
    }

    @FXML private void handlePoistaSali() {
        poistaSali();
    }

    @FXML private void handleTulosta() {
        ModalController.showModal(ControllerValikko.class.getResource("TulostaView.fxml"),
                "Tulosta", null, "");
    }

    @FXML private void handleTallenna() {
        tallenna(); 
    }
    
    @FXML private void handleJasenPainettu(MouseEvent event) {
        if(event.getClickCount() > 1) handleMuokkaaAsiakas();
    }
    
    @FXML private void handleMuokkaaAsiakas() {
        jasenKohdalla = chooserJasenet.getSelectedObject();
        muokkaaJasen();
    }
    
    @FXML private void handleHakuehto(){
        hae(0);
    }

    // ======================================================================
    // Hurjaa koodia

    private String salinnimi = "Mekka";
    private Gymobs gymobs;
    private Kaupunki kaupunkiKohdalla;
    private TextField edits[];
    private int kentta = 0;
    private Jasen jasenKohdalla;
    private static Kaupunki apukaupunki = new Kaupunki();
    private String logiTiedostonPerusNimi = "";
    private String logi = "";

    
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle 
     * yksi iso tekstikentt‰, johon voidaan tulostaa kaupunkien tiedot.
     * Alustetaan myˆs kaupunkilistan kuuntelija.
     */
    protected void alusta() {
        panelJasen.setFitToHeight(true);
        
        chooserKaupungit.clear();
        chooserKaupungit.addSelectionListener(e -> naytaKaupunki());
        chooserJasenet.clear();
        
        edits = ControllerMuokkaaSali.luoKentat(gridKaupunki);
        for( TextField edit : edits )
            if(edit != null) {
                edit.setEditable(false);
                edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaaSali(getFieldId(e.getSource(), kentta)); });
                edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));
            }
    }
    
    /**
     * N‰ytt‰‰ listasta valitun kaupungin tiedot, tilap‰isesti yhteen isoon edit-kentt‰‰n
     */
    protected void naytaKaupunki() {
        kaupunkiKohdalla = chooserKaupungit.getSelectedObject();
        
        if(kaupunkiKohdalla == null ) return;
        ControllerMuokkaaSali.naytaKaupunki(edits,kaupunkiKohdalla);
        naytaJasen();
    }
    
    /**
     * N‰ytt‰‰ valitun salin j‰senet
     */
    protected void naytaJasen() {
        chooserJasenet.clear();
        List<Jasen> loytyneet = gymobs.annaJasenet(kaupunkiKohdalla);
        for(Jasen jasen : loytyneet)
            chooserJasenet.add(jasen.getNimi(), jasen);
    }
    
    /**
     * Asettaa tekstin tapahtuma logiin
     */
    protected void naytaLogi() {
        editTapahtuma.appendText(logi);
    }
    
    /**
     * Tekee uuden tyhj‰n j‰senen editointia varten
     */
    public void uusiJasen() {
        if(kaupunkiKohdalla == null) return;
        
        Jasen jas = new Jasen(kaupunkiKohdalla.getTunnusNro());
        jas = ControllerMuokkaaAsiakas.kysyAsiakas(null, jas);
        if( jas == null ) return;
        jas.rekisteroi();
        gymobs.lisaa(jas);
        editTapahtuma.appendText(dateFormat.format(Calendar.getInstance().getTime()) + "\nLis‰ttiin uusi j‰sen " + jas.getNimi() + " paikkakunnan " + kaupunkiKohdalla.getKaupunki() + " salille\n");
        hae(kaupunkiKohdalla.getTunnusNro());
    }
    
    /**
     * Luo uuden salin jota aletaan editoimaan
     */
    protected void uusiSali() {
        try{
            Kaupunki uusi = new Kaupunki();
            uusi = ControllerMuokkaaSali.kysyKaupunki(null, uusi, 0);
            if(uusi == null) return;
            uusi.rekisteroi();
            gymobs.lisaa(uusi);
            editTapahtuma.appendText(dateFormat.format(Calendar.getInstance().getTime()) +"\nLis‰ttiin uusi sali paikkakunnalle " + uusi.getKaupunki() + "\n");
            hae(uusi.getTunnusNro());
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
    }
    
    /**
     * Tallenna metodi jolla jatketaan alempiin luokkiin
     * @return errori messageita jos on 
     */
    private String tallenna() {
        try {
            gymobs.tallenna();
            tallennaTapahtuma();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }

    /**
     * Hakee kaupunkien tiedot listaan 
     * @param knro kaupungin numero, joka aktivoidaan haun j‰lkeen
     */
    protected void hae(int knro) {
        int k = cbKentat.getSelectedIndex() + apukaupunki.ekaKentta() + 1;
        String ehto = hakuehto.getText();
        if(ehto.indexOf('*') < 0 ) ehto = "*" + ehto + "*";
        
        chooserKaupungit.clear();
        chooserJasenet.clear();
        int index = 0;
        Collection<Kaupunki> kaupungit;
        try{
            kaupungit = gymobs.etsi(ehto, k);
            int i = 0;
            for (Kaupunki kaupunki : kaupungit) {
                if(kaupunki.getTunnusNro() == knro) index = i;
                chooserKaupungit.add(kaupunki.getNimi(), kaupunki);
                i++;
                }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Kaupungin hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserKaupungit.setSelectedIndex(index); 
    }
    
    /**
     * Tulostaa kaupungin tiedot
     * @param os tietovirta johon tulostetaan
     * @param kaup tulostettava kaupunki
     */
    public void tulosta(PrintStream os, final Kaupunki kaup) {
        os.println("------------------------------");
        kaup.tulosta(os);
        os.println("------------------------------");
        List<Jasen> jasenet = gymobs.annaJasenet(kaup);
        for(Jasen jas:jasenet) 
            jas.tulosta(os);
    }
    
    /**
     * Muokataan valittua j‰sent‰
     */
    private void muokkaaJasen() {
        if(jasenKohdalla == null ) return;

        Jasen jasen;
        try {
            jasen = ControllerMuokkaaAsiakas.kysyAsiakas(null, jasenKohdalla.clone());
            if(jasen == null) return;
            gymobs.korvaaTaiLisaa(jasen);
            editTapahtuma.appendText(dateFormat.format(Calendar.getInstance().getTime()) +"\nMuokattiin j‰sent‰ " + jasen.getNimi() + "\n");
            hae(kaupunkiKohdalla.getTunnusNro());
        } catch (CloneNotSupportedException e) {
            //
        }
        
    }
    
    /**
     * Muokataan salia ja tehd‰‰n clooni
     * @param k kentt‰
     */
    private void muokkaaSali(int k) {
        if(kaupunkiKohdalla == null) return;
        
        try {
            Kaupunki kaupunki;
            kaupunki = ControllerMuokkaaSali.kysyKaupunki(null, kaupunkiKohdalla.clone(), k);
            if(kaupunki == null) return;
            gymobs.korvaaTaiLisaa(kaupunki);
            editTapahtuma.appendText(dateFormat.format(Calendar.getInstance().getTime()) +"\nMuokattiin paikkakunnan " + kaupunki.getKaupunki() + " tietoja.\n");
            hae(kaupunki.getTunnusNro());
        } catch (CloneNotSupportedException e) {
            //
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }
    
    /**
     * Poistetaan valitun kaupungin sali
     */
    private void poistaSali() {
        if(kaupunkiKohdalla == null) return;
        if(!Dialogs.showQuestionDialog("Poisto", "Poistetaanko kaupunki: " + kaupunkiKohdalla.getKaupunki(), "Kyll‰", "Ei"))
            return;
        gymobs.poista(kaupunkiKohdalla);
        editTapahtuma.appendText(dateFormat.format(Calendar.getInstance().getTime()) +"\nPoistettiin sali paikkakunnalta " + kaupunkiKohdalla.getKaupunki() + " ja sen j‰senet\n");
        int index = chooserKaupungit.getSelectedIndex();
        hae(0);
        chooserKaupungit.setSelectedIndex(index);
    }
    
    /**
     * Poistetaan valittu j‰sen
     */
    private void poistaJasen() {
        jasenKohdalla = chooserJasenet.getSelectedObject();
        if(jasenKohdalla == null) return;
        if(!Dialogs.showQuestionDialog("Poisto", "Poistetaanko j‰sen : " + jasenKohdalla.getNimi(), "Kyll‰", "Ei"))
            return;
        gymobs.poista(jasenKohdalla);
        editTapahtuma.appendText(dateFormat.format(Calendar.getInstance().getTime()) +"\nPoistettiin j‰sen " + jasenKohdalla.getNimi() + " paikkakunnan " + kaupunkiKohdalla.getKaupunki() + " salilta\n");
        hae(kaupunkiKohdalla.getTunnusNro());
    }

    
    /**
     * @param gymobs sali jota k‰sitell‰‰n
     */
    public void setGym(Gymobs gymobs) {
        this.gymobs = gymobs;
        naytaKaupunki();    
    }
    
    /**
     * Luodaan ikkunalle title
     * @param title title
     */
    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }

    /**
     * Asettaa tiedoston perusnimen 
     * @param nimi tallennustiedoston perusnimi
     */
    public void setLogiTiedostonPerusNimi(String nimi) {
        logiTiedostonPerusNimi = nimi;
    }
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setLogiTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi + "/";
        setLogiTiedostonPerusNimi(hakemistonNimi + "logi");
    }
    
    /**
     * Palauttaa logi tiedoston nimen
     * @return logi tiedoston nimi
     */
    public String getLogiTiedostonNimi() {
        return logiTiedostonPerusNimi + ".dat";
    }
    
    /**
     * Palautetaan logi tiedoston bakupin nimi
     * @return bakup tiedoston nimi
     */
    public String getLogiBakNimi() {
        return logiTiedostonPerusNimi + ".bak";
    }
    
    /**
     * Lukee avatun salin logi tiedoston
     * @param hakemisto mink‰ salin 
     * @throws SailoException heitetty virhe jos tulee
     */
    public void lueLogiTiedostosta(String hakemisto) throws SailoException {
        File theDir = new File(hakemisto);
        if(!theDir.exists()) {
            theDir.mkdir();
        }
        try(BufferedReader fi = new BufferedReader(new FileReader(getLogiTiedostonNimi())) )  {
            
            String rivi;
            while( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if("".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                lisaaLogi(rivi);
            }
        } catch ( FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getLogiTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    /**
     * Lis‰t‰‰n tapahtuma logiin teksti
     * @param rivi teksti
     */
    public void lisaaLogi(String rivi) {
        logi = logi + rivi + "\n";
    }
    
    /**
     * Alustaa salin lukemalla sen valitun nimisest‰ tiedostosta
     * @param nimi tiedosto josta kerhon tiedot luetaan
     * @return null jos onnistuu
     */
    protected String lueTiedosto(String nimi) {
        salinnimi = nimi;
        setTitle("Sali - " + salinnimi);
        try {
            setLogiTiedosto(nimi);
            gymobs.lueTiedostosta(nimi);
            editTapahtuma.clear();
            lueLogiTiedostosta(nimi);
            hae(0);
            naytaLogi();
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage();
            if(virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }
    
    /**
     * Tallennetaan salin logi tapahtumat
     * @throws SailoException heitetty errori jos on
     */
    public void tallennaTapahtuma() throws SailoException {
        File fbak = new File(getLogiBakNimi());
        File ftied = new File(getLogiTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(editTapahtuma.getText());
        } catch (FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedosto " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
    }
    
    /**
     * Kysyt‰‰n tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        String uusinimi = ControllerHT.kysyNimi(null, salinnimi);
        if(uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }
}
