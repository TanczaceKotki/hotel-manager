package CSV;

import hotel.Person;
import hotel.Reservation;
import hotel.Room;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataManager {
    CSV csv;
    ClientData clientData = new ClientData();
    RoomData roomData = new RoomData();
    ReservationData reservationData;
    ArrayList<Room> rooms;
    ArrayList<Person> clients;
    ArrayList<Reservation> reservations;

    public DataManager() {
        csv = new CSV();
    }

    public ArrayList<Room> defaultRoomImport() {
        File file = new File("./data/rooms.csv");
        try {
            rooms = csv.<Room>importData(file, roomData);
        } catch (IOException e) {}
        return rooms;
    }
    public ArrayList<Person> defaultPersonImport() {
        File file = new File("./data/rooms.csv");
        try {
            clients = csv.<Person>importData(file, clientData);
        } catch (IOException e) {}
        return clients;
    }
    public ArrayList<Reservation> defaultReservationImport() {
        File file = new File("./data/rooms.csv");
        reservationData = new ReservationData(clients, rooms);
        try {
            reservations = csv.<Reservation>importData(file, reservationData);
        } catch (IOException e) {}
        return reservations;
    }

}
