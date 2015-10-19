package hotel;

import java.util.ArrayList;
import java.util.Date;

public class Hotel {

    ArrayList<Room> rooms;

    public Hotel() {
        rooms = new ArrayList<Room>();
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


    public static void main(String[] argv) {
        Hotel hotel = new Hotel();

        Room newRoom = new Room(1, 4, 100, Room.RoomStandard.HIGH);
        Reservation newReservation = new Reservation(new Date(), new Date());
        newReservation.seats = 3;
        System.out.println(newRoom.addReservation(newReservation));

    }

}