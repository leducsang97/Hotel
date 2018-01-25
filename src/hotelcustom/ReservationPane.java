package hotelcustom;

import hotel.Reservation;
import hotel.Room;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ReservationPane extends Pane {
    public static final int PIXELS_PER_HOUR = 20;
    public static final int EVENT_WIDTH = 100;
    public static final int LEFT_MARGIN = 20;
    private Reservation event;
    private Room room;
    private int anchorX;
    private Rectangle border;
    
    public ReservationPane(Reservation e,Room r) {
        event = e;
        room = r;
        int ht = event.getDuration()*PIXELS_PER_HOUR;
        this.setHeight(ht);
        this.setWidth(EVENT_WIDTH);
        Text number = new Text(10,15,event.getCode()+" ("+event.getGroupSize()+")");
        border = new Rectangle(0,0,EVENT_WIDTH,ht);
        border.setFill(Color.WHITE);
        border.setStroke(Color.BLACK);
        this.getChildren().addAll(border,number);
    }
    
    public void setSelected(){
        border.setFill(Color.YELLOW);
    }
    
    public void unSelected(){
         border.setFill(Color.WHITE);
    }
    
    public void remove(){
        this.getChildren().clear();
        
    }
    
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    public Reservation getEvent() { return event; }
    // Compute and return the y coordinate of this event's box
    public int getBegin() {
        return (event.getStartTime()-6)*PIXELS_PER_HOUR;
    }
    // The anchor x is the x coordinate of the left edge of
    // this event's box
    public int getAnchorX() { return anchorX; }
    public void setAnchorX(int x) {
        anchorX = x;
        this.setLayoutX(anchorX);
        this.setLayoutY(this.getBegin());
    }
    
    // Panes are all resizeable, so we need to override this method
    // to keep the dimensions of our event panes fixed.
    @Override
    public boolean isResizable() {
        return false;
    }  
}
