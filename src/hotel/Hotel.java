package hotel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Hotel {

    DataManager dataManager;
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

    public ArrayList<Room> rooms;
    public ArrayList<SeasonalDiscount> seasonalDiscounts;
    public ArrayList<EarlyBookingDiscount> earlyBookingDiscounts;
    public ArrayList<Reservation> reservations;

    public Hotel() throws IOException {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        seasonalDiscounts = new ArrayList<>();
        earlyBookingDiscounts = new ArrayList<>();
        dataManager = new DataManager();
    }

    // current num of accommodated people
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