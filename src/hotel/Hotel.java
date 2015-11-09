package hotel;

import CSV.CSV;

import java.io.IOException;
import java.util.ArrayList;

public class Hotel {

    CSV csv;
    //Singleton
    static Hotel hotelInstance;
    public static Hotel getInstance() throws IOException {
        if(!(hotelInstance != null)) {
            hotelInstance = new Hotel();
        }
        return hotelInstance;

    }

    ArrayList<hotel.Room> rooms;
    ArrayList<Discount> seasonDiscounts;
    ArrayList<Discount> presaleDiscounts;
    ArrayList<Reservation> reservations;

    public Hotel() throws IOException {
        rooms = new ArrayList<Room>();
        seasonDiscounts = new ArrayList<Discount>();
        presaleDiscounts = new ArrayList<Discount>();
        csv = new CSV();
    }

    //Liczba osób aktualnie przyebywaj¹cych w hotelu
    public int numOfPeople() {
        int people = 0;
        for(Room room: rooms) {
            Reservation reservation = room.getCurrentReservation();
            if(reservation != null)
                people += reservation.seats;
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
        ArrayList<Room> available = new ArrayList<Room>();
        for(Room room: rooms) {
            if(room.getSeats() >= r.seats && room.isAvailable(r))
                available.add(room);
        }
        return available;
    }


    public static void main(String [ ] args) throws IOException {
        System.out.println("main");
        Hotel hotel = Hotel.getInstance();




    }

}