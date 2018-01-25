package hotelcustom;

import hotel.Hotel;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Joe Gregg
 */
public class FXMLDocumentController implements Initializable {
    private Hotel model;
    private LocalDate selectedDate;
    private RoomsPane rooms;
    
    private ReservationPane selected;
    
    @FXML
    private BorderPane innerPane;
    @FXML
    private DatePicker datePicker;
    
    public RoomsPane getRoomsPane() { return rooms; }
    
    @FXML
    private void dateChanged(ActionEvent event) {
        selectedDate = datePicker.getValue();
        rooms.setDate(selectedDate);
    }
    
    @FXML
    private void addReservation(ActionEvent event) {
        Stage parent = (Stage) rooms.getScene().getWindow();
        
        try {
            ReservationPane selected = rooms.getSelected();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNewReservationDialog.fxml"));
            Parent root = (Parent)loader.load();
            Scene scene = new Scene(root);
            Stage dialog = new Stage();
            dialog.setScene(scene);
            dialog.setTitle("Create New Reservation");
            dialog.initOwner(parent);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setX(parent.getX() + parent.getWidth()/4);
            dialog.setY(parent.getY() + parent.getHeight()/3);
            
            FXMLNewReservationDialogController controller = (FXMLNewReservationDialogController) loader.getController();
            controller.setDate(selectedDate);
            controller.setRooms(rooms);
            dialog.show();
        } catch(Exception ex) {
            System.out.println("Could not open dialog.");
            ex.printStackTrace();
        }
    }
    
    @FXML 
    private void editReservation(ActionEvent event){
        Stage parent = (Stage) datePicker.getScene().getWindow();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNewReservationDialog.fxml"));
            Parent root = (Parent)loader.load();
            Scene scene = new Scene(root);
            Stage dialog = new Stage();
            dialog.setScene(scene);
            dialog.setTitle("Edit Reservation");
            dialog.initOwner(parent);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setX(parent.getX() + parent.getWidth()/4);
            dialog.setY(parent.getY() + parent.getHeight()/3);
            
            FXMLNewReservationDialogController controller = (FXMLNewReservationDialogController) loader.getController();
            controller.setDate(selectedDate);
            controller.setRooms(rooms);
            try{
                selected = rooms.getSelected();
                System.out.print(selected);
                controller.setReservation(selected.getEvent());
                controller.setup();
            }
            catch(Exception ex){
                System.out.println("Ngu"+ selected);
            }
            dialog.show();
        } catch(Exception ex) {
            System.out.println("Could not open dialog.");
            ex.printStackTrace();
        }
    }
    @FXML
    private void saveAll(ActionEvent event) {
        PrintWriter output = null;
        try { 
            output = new PrintWriter(new File("events.txt"));
            model.writeTo(output);
            output.close();
        } catch(Exception ex) {
            System.out.println("Error writing data to text file");
        }
    }
    
    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedDate = LocalDate.now();
        datePicker.setValue(selectedDate);
        
        model = new Hotel();
        Scanner input = null;
        try {
            input = new Scanner(new File("events.txt"));
            model.readFrom(input);
            input.close();
        } catch (Exception ex) {
            System.out.println("Can not load data file.");
            ex.printStackTrace();
            if(input != null)
                input.close();
        }
        
        rooms = new RoomsPane(model,selectedDate);
        ScrollPane holder = new ScrollPane();
        selected = rooms.getSelected();

        holder.setContent(rooms);
        innerPane.setCenter(holder);
    }    
    
}
