package HT2;

import java.net.URL;
import java.util.ResourceBundle;

import Sali.Kaupunki;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Salin muokkauksen controlleri
 * @author Joel
 * @version Feb 16, 2017
 *
 */
public class ControllerMuokkaaSali implements ModalControllerInterface<Kaupunki>, Initializable {
    
    @FXML private Button buttonCancel;
    @FXML private Button buttonLisaaSali;
    @FXML private GridPane gridKaupunki;


    
    @FXML
    void handleCancel() {
        kaupunkiKohdalla = null;
        ModalController.closeStage(buttonCancel);
    }

    @FXML
    void handleLisaaSali() {
        ModalController.closeStage(buttonLisaaSali);
        ModalController.showModal(ControllerValikko.class.getResource("LisaaSaliView.fxml"),
                "Uusi Sali", null, "");
    }

    @FXML
    void handleOK() {
        if( kaupunkiKohdalla != null && kaupunkiKohdalla.getNimi().trim().equals("") ){
            //naytaVirhe("Nimi ei saa olla tyhj‰!");
            return;
        }
        ModalController.closeStage(buttonCancel);
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }

    @Override
    public Kaupunki getResult() {
        return kaupunkiKohdalla;
    }

    /**
     * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(apuKaupunki.ekaKentta(), Math.min(kentta, apuKaupunki.getKenttia()-1));
        edits[kentta].requestFocus();
        
    }

    @Override
    public void setDefault(Kaupunki oletus) {
        kaupunkiKohdalla = oletus;
        naytaKaupunki(edits,kaupunkiKohdalla);
    }
    
    
    // ===========================================================
    // T‰st‰ eteenp‰in hurjaa koodaamista
    
    TextField[] edits; 
    private Kaupunki kaupunkiKohdalla;
    private int kentta = 0;
    private static Kaupunki apuKaupunki = new Kaupunki();

    
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }
    
    /**
     * @param edits textfieldit johon sijoitetaan tiedot
     * @param kaupunki kaupunki jonka tietoja halutaan
     */
    public static void naytaKaupunki(TextField[] edits, Kaupunki kaupunki) {
        if( kaupunki == null ) return;
        
        for (int k = kaupunki.ekaKentta(); k < kaupunki.getKenttia(); k++) {
            edits[k].setText(kaupunki.anna(k));
        }
    }

    /**
     * Luodaan GridPaneen kaupungin tiedot
     * @param gridKaupunki mihin tiedot luodaan
     * @return luodut tekstikent‰t
     */
    public static TextField[] luoKentat(GridPane gridKaupunki) {
        gridKaupunki.getChildren().clear();
        TextField[] edits = new TextField[apuKaupunki.getKenttia()];
        
        for (int i = 0, k = apuKaupunki.ekaKentta(); k < apuKaupunki.getKenttia(); k++, i++) {
            Label label = new Label(apuKaupunki.getKysymys(k));
            gridKaupunki.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e" + k);
            gridKaupunki.add(edit, 1, i);
        }
        return edits;
    }
    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikentt‰, johon voidaan tulostaa kaupunkien tiedot.
     */
    protected void alusta() { 
        edits = luoKentat(gridKaupunki);
        for(TextField edit : edits) {
            if( edit != null)
                edit.setOnKeyReleased(e -> kasitteleMuutosKaupunkiin((TextField)(e.getSource())));
        }
    }
    
    /**
     * Palauttaa komponentin id:st‰ saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mik‰ arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !(obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        String s = node.getId();
        if ( s.length() < 1 ) return oletus;
        return Mjonot.erotaInt(s.substring(1), oletus);
    }
    
    /**
     * K‰sitell‰‰n kaupunkiin tullut muutos
     * @param edit muuttunut kentt‰
     */
    private void kasitteleMuutosKaupunkiin(TextField edit) {
        if(kaupunkiKohdalla == null) return;
        int k = getFieldId(edit,apuKaupunki.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = kaupunkiKohdalla.aseta(k,s);
        if(virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            //naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            //naytaVirhe(virhe);
        }
    }
    
    /**
     * Luodaan kaupungin kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle 
     * @param oletus mit‰ dataan n‰ytet‰‰n oletuksena
     * @param kentta mik‰ kentt‰ saa fokuksen kun n‰ytet‰‰n
     * @return null jos painetaan Cancel, muuten t‰ytetty tietue
     */
    public static Kaupunki kysyKaupunki(Stage modalityStage, Kaupunki oletus, int kentta) {
        return ModalController.<Kaupunki, ControllerMuokkaaSali>showModal(
                ControllerMuokkaaSali.class.getResource("MuokkaaSaliView.fxml"),
                "Sali",
                modalityStage, oletus, 
                ctrl -> ctrl.setKentta(kentta)
                );
    }

}
