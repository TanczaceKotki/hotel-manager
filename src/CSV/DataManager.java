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
    public ArrayList<Room> rooms;
    public ArrayList<Person> clients;
    public ArrayList<Reservation> reservations;

    public DataManager() {
        csv = new CSV();
    }

    public ArrayList<Room> roomImport() {
        rooms = new ArrayList<>();
        File file = new File("src/data/rooms.csv");
        try {
            rooms = csv.importData(file, roomData);
        } catch (IOException e) {}
        return rooms;
    }
    public ArrayList<Person> personImport() {
        clients = new ArrayList<>();
        File file = new File("src/data/clients.csv");
        try {
            clients = csv.importData(file, clientData);
        } catch (IOException e) {}
        return clients;
    }
    public ArrayList<Reservation> reservationImport(ArrayList<Room> roomArrayList, ArrayList<Person> personArrayList) {
        reservations = new ArrayList<>();
        File file = new File("src/data/current_log.csv");
        reservationData = new ReservationData(roomArrayList, personArrayList);
        try {
            reservations = csv.importData(file, reservationData);
        } catch (IOException e) {}
        return reservations;
    }

    public void roomExport(ArrayList<Room> roomArrayList) {
        File file = new File("src/data/rooms.csv");
        try {
            csv.exportData(roomArrayList, file, roomData);
        } catch (IOException e) {
            System.out.print(e.toString());
        }
    }
    public void personExport(ArrayList<Person> personArrayList) {
        File file = new File("src/data/clients.csv");
        try {
            csv.exportData(personArrayList, file, clientData);
        } catch (IOException e) {}
    }
    public void reservationExport(
            ArrayList<Reservation> reservationArrayList,
            ArrayList<Room> roomArrayList,
            ArrayList<Person> personArrayList
    ) {
        File file = new File("src/data/current_log.csv");
        reservationData = new ReservationData(roomArrayList, personArrayList);
        try {
            csv.exportData(reservationArrayList, file, reservationData);
        } catch (IOException e) {}
    }

}
