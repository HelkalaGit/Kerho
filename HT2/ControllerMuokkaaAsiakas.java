package HT2;

import java.net.URL;
import java.util.ResourceBundle;

import Sali.Jasen;
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
 * Asiakkaan muokkauksen controlleri
 * @author Joel
 * @version Feb 16, 2017
 *
 */
public class ControllerMuokkaaAsiakas implements ModalControllerInterface<Jasen>, Initializable {

    @FXML private Button buttonCancel;
    @FXML private GridPane gridJasen;
    @FXML private Label labelVirhe;


    
    @FXML void handleCancel() {
        jasenKohdalla = null;
        luoKentat(gridJasen);
        ModalController.closeStage(buttonCancel);
    }

    @FXML void handleLisaaAsiakas() {
        ModalController.showModal(ControllerValikko.class.getResource("LisaaAsiakasView.fxml"),
                "Uusi Asiakas", null, "");
    }

    @FXML void handleOK() {
        if( jasenKohdalla != null && jasenKohdalla.getNimi().trim().equals("") ){
            naytaVirhe("Nimi ei saa olla tyhjä!");
            return;
        }
        ModalController.closeStage(buttonCancel);
    }

    @Override
    public void handleShown() {
        kentta = Math.max(apuJasen.ekaKentta(), Math.min(kentta, apuJasen.getKenttia()-1));
        edits[kentta].requestFocus();
        
    }
    
    // ================================================================
    
    private Jasen jasenKohdalla;
    private int kentta = 0;
    TextField edits[];
    private static Jasen apuJasen = new Jasen();
    
    
    @Override
    public void setDefault(Jasen oletus) {
        jasenKohdalla = oletus;
        naytaJasen(edits,jasenKohdalla);
        
    }
    
    /**
     * @param edits textfieldit johon sijoitetaan tiedot
     * @param jasen jasen jonka tietoja halutaan
     */
    public static void naytaJasen(TextField[] edits, Jasen jasen) {
        if( jasen == null ) return;
        
        for (int k = jasen.ekaKentta(); k < jasen.getKenttia(); k++) {
            edits[k].setText(jasen.anna(k));
        }
    }
    
    /**
     * Luodaan jäsenen kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle 
     * @param oletus mitä dataan näytetään oletuksena
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Jasen kysyAsiakas(Stage modalityStage, Jasen oletus) {
        return ModalController.<Jasen, ControllerMuokkaaAsiakas>showModal(
                ControllerMuokkaaAsiakas.class.getResource("MuokkaaAsiakasView.fxml"),
                "Sali",
                modalityStage, oletus, null);
    }
    
    /**
     * Luodaan GridPaneen jäsenen tiedot
     * @param gridJasen mihin tiedot luodaana
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridJasen) {
        gridJasen.getChildren().clear();
        TextField[] edits = new TextField[apuJasen.getKenttia()];
        
        for (int i = 0, k = apuJasen.ekaKentta(); k < apuJasen.getKenttia(); k++, i++) {
            Label label = new Label(apuJasen.getKysymys(k));
            gridJasen.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e" + k);
            gridJasen.add(edit, 1, i);
        }
        return edits;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alusta();
        
    }

    private void alusta() {
        edits = luoKentat(gridJasen);
        for(TextField edit : edits) {
            if( edit != null)
                edit.setOnKeyReleased(e -> kasitteleMuutosJaseneen((TextField)(e.getSource())));
        }
    }
    
    /**
     * Palauttaa komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
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
     * Näytetään virhe labelissa jos on ongelmia
     * @param virhe mikä virhe näytetään
     */
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    
    /**
     * Käsitellään jäseneen tullut muutos
     * @param edit muuttunut kenttä
     */
    private void kasitteleMuutosJaseneen(TextField edit) {
        if(jasenKohdalla == null) return;
        int k = getFieldId(edit,apuJasen.ekaKentta());
        String s = edit.getText();
        if(s == null) s = "";
        String virhe = null;
        virhe = jasenKohdalla.aseta(k,s);
        if(virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }

    @Override
    public Jasen getResult() {
        return jasenKohdalla;
    }

}
