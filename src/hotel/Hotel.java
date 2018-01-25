package hotel;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Hotel {

    private ArrayList<Room> rooms;

    public Hotel() {
        rooms = new ArrayList<Room>();
    }

    public void readFrom(Scanner input) {
        // Read the unplace first
        // Read the five rooms from the input file
        for (int n = 0; n < 5; n++) {
            Room newRoom = new Room();
            newRoom.readFrom(input);
            rooms.add(newRoom);
        }
    }

    public void unplaceRead(Scanner input) {
        String name = input.next() + input.nextLine();
        int howMany = input.nextInt();
        ObservableList<Reservation> reservations;
        reservations = FXCollections.observableArrayList();
        for (int n = 0; n < howMany; n++) {
            Reservation nextReservation = new Reservation();
            nextReservation.readFrom(input);

            reservations.add(nextReservation);
        }
        FXCollections.sort(reservations);
    }

    public void writeTo(PrintWriter output) {
        for (Room r : rooms) {
            r.writeTo(output);
        }

    }

    public Room getRoom(String forName) {
        for (Room r : rooms) {
            if (r.getName().equalsIgnoreCase(forName)) {
                return r;
            }
        }
        return null;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
