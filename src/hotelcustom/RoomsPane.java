package hotelcustom;

import hotel.Reservation;
import hotel.Hotel;
import hotel.Room;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class RoomsPane extends Pane {
    
    private Hotel hotel;
    private ObservableList<Reservation> unplaced;
    LocalDate currentDate;
    double dragStart;
    //New variable
    private ReservationPane selected;
    
    public RoomsPane(Hotel hotel, LocalDate date) {
        this.hotel = hotel;
        this.currentDate = date;
        unplaced = FXCollections.observableArrayList();
        setUpRoomsForDate(currentDate);
    }
    
    public void handleKeyPress(KeyEvent key) {
        System.out.println("Received key press " + key.getCode());
        if (key.getCode().equals(KeyCode.BACK_SPACE)) {
            removeReservation();
        }
        this.setUpRoomsForDate(currentDate);
    }
    
    public void handleMouseClick(MouseEvent key) {
        System.out.println("Received mouse click");
        selected.setSelected();
    }
    
    public void setDate(LocalDate newDate) {
        this.currentDate = newDate;
        setUpRoomsForDate(currentDate);
    }
    
    private void setUpRoomsForDate(LocalDate date) {
        this.getChildren().clear();

        // Set up the hour scale on the left
        int hour = 7;
        int hourY = ReservationPane.PIXELS_PER_HOUR + 10;
        for (hour = 7; hour < 23; hour++) {
            Text label = new Text(2, hourY, String.valueOf(hour));
            this.getChildren().add(label);
            hourY += ReservationPane.PIXELS_PER_HOUR;
        }

        // Set up the column heading and separators
        int nameX = ReservationPane.LEFT_MARGIN;
        Text heading = new Text(nameX + 2, 12, "Unplaced");
        this.getChildren().add(heading);
        nameX += ReservationPane.EVENT_WIDTH;
        for (Room r : hotel.getRooms()) {
            Line separator = new Line(nameX - 1, 0, nameX - 1, (23 - 6) * ReservationPane.PIXELS_PER_HOUR);
            separator.setStroke(Color.LIGHTGRAY);
            this.getChildren().add(separator);
            heading = new Text(nameX + 2, 12, r.getName());
            this.getChildren().add(heading);
            nameX += ReservationPane.EVENT_WIDTH;
        }

        // Display the events
        FilteredList<Reservation> filteredEvents = new FilteredList<Reservation>(unplaced, (rv) -> rv.getDate().equals(date));
        for (Reservation rv : filteredEvents) {
            ReservationPane pane = new ReservationPane(rv, null);
            pane.setAnchorX(ReservationPane.LEFT_MARGIN);
            pane.setOnMousePressed((evt) -> startDrag(evt, pane));
            pane.setOnMouseDragged((evt) -> continueDrag(evt, pane));
            pane.setOnMouseReleased((evt) -> finishDrag(evt, pane));
            this.getChildren().add(pane);
        }
        int x = ReservationPane.EVENT_WIDTH + ReservationPane.LEFT_MARGIN;
        for (Room r : hotel.getRooms()) {
            filteredEvents = new FilteredList<Reservation>(r.getReservations(), (rv) -> rv.getDate().equals(date));
            for (Reservation rv : filteredEvents) {
                ReservationPane pane = new ReservationPane(rv, r);
                pane.setAnchorX(x);
                pane.setOnMousePressed((evt) -> startDrag(evt, pane));
                pane.setOnMouseDragged((evt) -> continueDrag(evt, pane));
                pane.setOnMouseReleased((evt) -> finishDrag(evt, pane));
                this.getChildren().add(pane);
            }
            x += ReservationPane.EVENT_WIDTH;
        }
    }
    
    public void addReservation(Reservation r) {
        unplaced.add(r);
        this.setUpRoomsForDate(currentDate);
    }
    
    public void editReservation(Reservation o, Reservation n) {
        Room currentRoom = selected.getRoom();
        if (currentRoom != null) {
            selected.getRoom().removeReservation(o);
        } else {
            unplaced.remove(o);
        }
        if (currentRoom.allowsReservation(n)) {
            selected.getRoom().addReservation(n);
        } else {
            unplaced.add(n);
        }
        
        this.setUpRoomsForDate(currentDate);
    }
    
    public void removeReservation() {
        Reservation e = selected.getEvent();
        Room currentRoom = selected.getRoom();
        if (currentRoom != null) {
            currentRoom.removeReservation(e);
        } else {
            unplaced.remove(e);
        }
        this.setUpRoomsForDate(currentDate);
    }
    
    private void startDrag(MouseEvent event, ReservationPane pane) {
        dragStart = event.getSceneX();
        //
        try {
            selected.unSelected();
        } catch (Exception exp) {
            
        }
        selected = pane;
        
    }
    
    private void continueDrag(MouseEvent event, ReservationPane pane) {
        try {
            selected.unSelected();
        } catch (Exception exp) {
            
        }
        selected = pane;
        double dragNow = event.getSceneX();
        pane.setLayoutX(pane.getAnchorX() + dragNow - dragStart);
    }
    
    private void finishDrag(MouseEvent event, ReservationPane pane) {
        try {
            selected.unSelected();
        } catch (Exception exp) {
            
        }
        selected = pane;
        
        Point2D dragEnd = this.sceneToLocal(event.getSceneX(), event.getSceneY());
        Reservation e = pane.getEvent();
        // If we ended in the column on the left, we need to place this
        // Reservation in the unplaced column.
        if (dragEnd.getX() <= ReservationPane.EVENT_WIDTH + ReservationPane.LEFT_MARGIN) {
            Room currentRoom = pane.getRoom();
            if (currentRoom != null) {
                currentRoom.removeReservation(e);
                unplaced.add(e);
            }
            pane.setRoom(null);
            pane.setAnchorX(ReservationPane.LEFT_MARGIN);
        } else {
            // Otherwise, we need to figure out which of the five rooms we ended in...
            int roomIndex = (int) (((dragEnd.getX() - ReservationPane.LEFT_MARGIN) / ReservationPane.EVENT_WIDTH) - 1);
            Room targetRoom = hotel.getRooms().get(roomIndex);
            Room currentRoom = pane.getRoom();
            int oldAnchor = pane.getAnchorX();
            // ...and whether or not that Room will accept this Reservation.
            if (targetRoom != currentRoom && targetRoom.allowsReservation(e)) {
                if (currentRoom != null) {
                    currentRoom.removeReservation(e);
                } else {
                    unplaced.remove(e);
                }
                targetRoom.addReservation(e);
                pane.setRoom(targetRoom);
                pane.setAnchorX(ReservationPane.LEFT_MARGIN + (roomIndex + 1) * ReservationPane.EVENT_WIDTH);
            } else {
                pane.setAnchorX(oldAnchor);
            }
        }
    }
    
    public ReservationPane getSelected() {
        return selected;
    }
    
    @Override
    protected double computePrefHeight(double width) {
        return (23 - 7) * ReservationPane.PIXELS_PER_HOUR;
    }
    
    @Override
    protected double computePrefWidth(double height) {
        return 6 * ReservationPane.EVENT_WIDTH + ReservationPane.LEFT_MARGIN;
    }
    
    @Override
    public boolean isResizable() {
        return false;
    }
}
