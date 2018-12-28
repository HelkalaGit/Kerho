package HT2;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Aloitus näytön controlleri
 * @author Joel
 * @version Feb 16, 2017
 *
 */
public class ControllerHT implements ModalControllerInterface<String>{
    
    @FXML private Button buttonCancel;
    @FXML private TextField textVastaus;
    private String vastaus = null;
    
    @FXML void handleAvausOk() {
        vastaus = textVastaus.getText();
        ModalController.closeStage(textVastaus);
    }

    @FXML void handleCancel() {
        ModalController.closeStage(textVastaus);
        //cancel();
    }

    @FXML void handleJatka() {
        ModalController.closeStage(textVastaus);
    }
    
    // ===============================
    
    /**
     * Luodaan nimenkysymisdialogi ja palautetaan siihen kirjoitettu nimi tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä nimeä näytetään oletuksena
     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
     */
    public static String kysyNimi(Stage modalityStage, String oletus) {
        return ModalController.showModal
                (ControllerHT.class.getResource("HTView.fxml"),
                 "GymObs",
                  modalityStage,
                  oletus);
    }

    @Override
    public String getResult() {
        return vastaus;
    }

    @Override
    public void handleShown() {
        textVastaus.requestFocus();
        
    }

    @Override
    public void setDefault(String oletus) {
        textVastaus.setText(oletus);
    }

}
