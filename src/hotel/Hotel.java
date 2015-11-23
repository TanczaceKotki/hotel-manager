package hotel;

import CSV.CSV;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Hotel {

    CSV csv;
    //Singleton
    static Hotel hotelInstance;
    public static Hotel getInstance() {
        if(hotelInstance == null) {
            try {
                hotelInstance = new Hotel();
            } catch(IOException e) {
                System.err.println("Error: invalid CSV file");
            }
        }
        return hotelInstance;

    }

    public ArrayList<hotel.Room> rooms;
    public ArrayList<SeasonalDiscount> seasonalDiscounts;
    public ArrayList<EarlyBookingDiscount> earlyBookingDiscounts;
    public ArrayList<Reservation> reservations;

    public Hotel() throws IOException {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        seasonalDiscounts = new ArrayList<>();
        earlyBookingDiscounts = new ArrayList<>();
        csv = new CSV();
    }

    //Liczba os�b aktualnie przyebywaj�cych w hotelu
    public int numOfPeople() {
        int people = 0;
        Date today = new Date();
        for(Reservation reservation: reservations) {
            if(reservation.contains(today)) {
                people += reservation.seats;
            }
        }
        return people;

    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    public ArrayList<Room> availableRooms(Reservation r) {
        ArrayList<Room> available = new ArrayList<>();
        for(Room room: rooms) {
            if(room.getSeats() >= r.seats && room.isAvailable(r))
                available.add(room);
        }
        return available;
    }
}