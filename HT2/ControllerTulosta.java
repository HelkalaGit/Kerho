package HT2;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Tulosta ikkunan controlleri
 * @author Joel
 * @version Feb 16, 2017
 *
 */
public class ControllerTulosta implements ModalControllerInterface<String>{
    @FXML private Button buttonCancel;
    
    @FXML
    void handleCancel() {
        ModalController.closeStage(buttonCancel);
        //cancel();
    }

    @FXML
    void handleTulostaSivu() {
        tulosta();
    }
    
    private void tulosta() {
        Dialogs.showMessageDialog("Tulostetaan! Ei toimi vielä.");
    }

    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }   
}
