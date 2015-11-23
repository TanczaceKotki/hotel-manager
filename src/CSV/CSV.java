package CSV;

import hotel.Reservation;
import hotel.Room;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class CSV {
    private CSVImport i;
    private CSVExport e;
    public CSV() {
        i = new CSVImport();
        e = new CSVExport();
    }

    public ArrayList<Room> loadRooms() {
        try {
            return i.roomList();
        } catch (IOException e1) {
            e1.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<Room> loadRooms(Reader reader) {
        try {
            return i.roomList();
        } catch (IOException e1) {
            e1.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<Reservation> loadReservations() {
        try {
            return i.reservationList();
        } catch (IOException e1) {
            e1.printStackTrace();
            return new ArrayList<>();
        }
    }

}
