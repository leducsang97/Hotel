/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelcustom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Joe Gregg
 */
public class HotelCustom extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = (Parent)loader.load();
           
        Scene scene = new Scene(root);
       
        stage.setScene(scene);
        stage.setTitle("Reservation System");
        stage.show();
       
        FXMLDocumentController controller = (FXMLDocumentController) loader.getController();
        RoomsPane rooms = controller.getRoomsPane();
        // We attach the key handler to the scene instead of the RoomsPane. I 
        // suspect this is necessary because the ScrollPane that the RoomsPane is 
        // embedded in is grabbing the key events that are supposed to go to the
        // RoomsPane.
        scene.setOnKeyPressed((evt)->rooms.handleKeyPress(evt));
        scene.setOnMouseClicked((evt)->rooms.handleMouseClick(evt));
   }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
