package hotel;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class Reservation implements Comparable<Reservation> {
    private int eventNumber;
    private int customerNumber;
    private LocalDate day;
    private int startHour;
    private int endHour;
    private int groupSize;
    
    public Reservation() {
        
    }
    
    public Reservation(int eventNumber,int customerNumber,LocalDate day,int startHour,int endHour,int groupSize) {
        this.eventNumber = eventNumber;
        this.customerNumber = customerNumber;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
        this.groupSize = groupSize;
    }
    
    public int getGroupSize() { return groupSize; }
    public LocalDate getDate() { return day; }
    public int getStartTime() { return startHour; }
    public int getEndTime() { return endHour; }
    public int getCustomerNumber() { return customerNumber; }
    public int getDuration() { return endHour - startHour; }
    public String getCode() { return String.valueOf(eventNumber); }
    public String getTimeSpan() { return String.valueOf(startHour)+"-"+String.valueOf(endHour); }
    
    public void readFrom(Scanner input) {
        eventNumber = input.nextInt();
        customerNumber = input.nextInt();
        input.useDelimiter(" |/");
        int year = input.nextInt();
        int month = input.nextInt();
        int dayOfMonth = input.nextInt();
        input.reset();
        day = LocalDate.of(year, month, dayOfMonth);
        startHour = input.nextInt();
        endHour = input.nextInt();
        groupSize = input.nextInt();
    }
    
    public void writeTo(PrintWriter output) {
        output.print(eventNumber);
        output.print(" ");
        output.print(customerNumber);
        output.print(" ");
        output.print(day.getYear());
        output.print("/");
        output.print(day.getMonthValue());
        output.print("/");
        output.print(day.getDayOfMonth());
        output.print(" ");
        output.print(startHour);
        output.print(" ");
        output.print(endHour);
        output.print(" ");
        output.println(groupSize);
    }

    public String toString() {
        return eventNumber + "(" + groupSize + "):" + startHour + "-" + endHour;
    }
    
    @Override
    public int compareTo(Reservation o) {
        if(day.isBefore(o.day))
            return -1;
        if(day.isAfter(o.day))
            return 1;
        if(endHour <= o.startHour)
            return -1;
        else if(startHour >= o.endHour)
            return 1;
        return 0;
    }   
}
