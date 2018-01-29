package hotel;

import java.io.PrintWriter;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Room {
    private String name;
    private int capacity;
    private ObservableList<Reservation> reservations;
    
    public Room() { reservations = FXCollections.observableArrayList(); }
    
    public String getName() { return name; }
   
    public boolean allowsReservation(Reservation newReservation) {
        if(newReservation.getGroupSize() > capacity) 
            return false;
        for(Reservation r : reservations) {
            if(r.compareTo(newReservation) == 0)
                return false;
        }
        return true;
    }
    
    public void addReservation(Reservation newReservation) {
        reservations.add(newReservation);
        FXCollections.sort(reservations);
    }
    
    public void removeReservation(Reservation toRemove) {
        reservations.remove(toRemove);
    }
    
    public void readFrom(Scanner input) {
        // Read the room details and reservations from the file
        name = input.next() + input.nextLine();
        capacity = input.nextInt();
        int howMany = input.nextInt();
        for(int n = 0;n < howMany;n++) {
            Reservation nextReservation = new Reservation();
            nextReservation.readFrom(input);
            reservations.add(nextReservation);
        }
        FXCollections.sort(reservations);
    }
    
    public void writeTo(PrintWriter output) {
        output.println(name);
        output.println(capacity);
        int howMany = reservations.size();
        output.println(howMany);
        for(int n = 0;n < howMany;n++)
            reservations.get(n).writeTo(output);
    }
    
    public ObservableList<Reservation> getReservations() { return reservations; }
}
