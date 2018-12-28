package HT2;
	
import Sali.Gymobs;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * @author Joel
 * @version Feb 14, 2017
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			final FXMLLoader ldr = new FXMLLoader(getClass().getResource("ValikkoView.fxml"));
			final Pane root = (Pane) ldr.load();
		    final ControllerValikko gymCtrl = (ControllerValikko) ldr.getController();
		    
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("GymObs");
			
			Gymobs gymobs = new Gymobs();
			gymCtrl.setGym(gymobs);
			
			primaryStage.show();
			if ( !gymCtrl.avaa() ) Platform.exit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Käynnistetään ohjelma
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
