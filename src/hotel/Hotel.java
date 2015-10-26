package hotel;

import CSV.CSV;

import java.io.IOException;
import java.util.ArrayList;

public class Hotel {

    ArrayList<Room> rooms;
    CSV csv;

    public Hotel() throws IOException {
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
            if(room.seats >= r.seats && room.isAvailable(r))
                available.add(room);
        }
        return available;
    }

    public static void main(String [ ] args) throws IOException {
        System.out.println("main");
        Hotel hotel = new Hotel();
    }

}