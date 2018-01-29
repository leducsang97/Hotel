package hotelcustom;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import hotel.Reservation;
import java.time.LocalDate;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Sang Le based on prof Gregg.
 */
public class FXMLNewReservationDialogController implements Initializable {

    private RoomsPane rooms;
    private LocalDate date;
    // 
    private Reservation current;

    @FXML
    private TextField eventNumber;
    @FXML
    private TextField customerNumber;
    @FXML
    private TextField groupSize;
    @FXML
    private TextField startTime;
    @FXML
    private TextField endTime;

    @FXML
    private void cancelDialog(ActionEvent event) {
        eventNumber.getScene().getWindow().hide();
    }
    @FXML
    private Label errorText;

    @FXML
    private void acceptDialog(ActionEvent event) {
        int evtNumber = Integer.parseInt(eventNumber.getText());
        int custNumber = Integer.parseInt(customerNumber.getText());
        int start = Integer.parseInt(startTime.getText());
        int end = Integer.parseInt(endTime.getText());
        int size = Integer.parseInt(groupSize.getText());
        Reservation newEvent = new Reservation(evtNumber, custNumber, date, start, end, size);
        
        if (current != null) {
            rooms.editReservation(current, newEvent);
            eventNumber.getScene().getWindow().hide();
        } else {

            rooms.addReservation(newEvent);
            eventNumber.getScene().getWindow().hide();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        current = null;

    }

    public void setReservation(Reservation newR) {
        current = newR;
    }

    public void setRooms(RoomsPane rooms) {
        this.rooms = rooms;
    }

    public void setup() {
        eventNumber.setText(current.getCode());
        customerNumber.setText(String.valueOf(current.getCustomerNumber()));
        groupSize.setText(String.valueOf(current.getGroupSize()));
        startTime.setText(String.valueOf(current.getStartTime()));
        endTime.setText(String.valueOf(current.getEndTime()));
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
