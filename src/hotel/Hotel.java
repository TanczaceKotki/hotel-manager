package hotel;

import java.util.ArrayList;

public class Hotel {

    ArrayList<Room> rooms;

    public Hotel() {
        rooms = new ArrayList<Room>();
    }

    //Liczba osób aktualnie przyebywaj¹cych w hotelu
    public int getLocatorsCount() {
        int locators = 0;
        for(Room room: rooms) {
            Reservation reservation = room.getCurrentReservation();
            if(reservation != null)
                locators += reservation.locatorsCount;
        }
        return locators;

    }

    void addRoom(Room room) {
        rooms.add(room);
    }

    void removeRoom(Room room) {
        rooms.remove(room);
    }


    public static void main(String[] argv) {
        Hotel hotel = new Hotel();

        Room newRoom = new Room(1, 4, 100, Room.RoomStandard.HIGH);
        Reservation newReservation = new Reservation();
        newReservation.locatorsCount = 3;
        System.out.println(newRoom.addReservation(newReservation));

    }

}