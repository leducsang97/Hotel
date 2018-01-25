/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel;

import java.io.PrintWriter;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Sang
 */
public class unPlaced {
     private String name = "Unplaced";
    private ObservableList<Reservation> reservations;
    
     public unPlaced() { reservations = FXCollections.observableArrayList(); }
     
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
        int howMany = reservations.size();
        output.println(howMany);
        for(int n = 0;n < howMany;n++)
            reservations.get(n).writeTo(output);
    }
    
    public ObservableList<Reservation> getReservations() { return reservations; }
}
