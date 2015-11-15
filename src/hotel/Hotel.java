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
        if(!(hotelInstance != null)) {
            try {
                hotelInstance = new Hotel();
            } catch(IOException e) {
                System.err.println("Error: invalid CSV file");
            }
        }
        return hotelInstance;

    }

    ArrayList<hotel.Room> rooms;
    ArrayList<SeasonalDiscount> seasonalDiscounts;
    ArrayList<EarlyBookingDiscount> earlyBookingDiscounts;
    ArrayList<Reservation> reservations;

    public Hotel() throws IOException {
        rooms = new ArrayList<Room>();
        reservations = new ArrayList<Reservation>();
        seasonalDiscounts = new ArrayList<SeasonalDiscount>();
        earlyBookingDiscounts = new ArrayList<EarlyBookingDiscount>();
        csv = new CSV();
    }

    //Liczba osób aktualnie przyebywaj¹cych w hotelu
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
        ArrayList<Room> available = new ArrayList<Room>();
        for(Room room: rooms) {
            if(room.getSeats() >= r.seats && room.isAvailable(r))
                available.add(room);
        }
        return available;
    }


    public static void main(String [ ] args) throws IOException {

        Hotel hotel = Hotel.getInstance();

        //Mo¿na wykorzystaæ ten kod jako testowy
        //Docelowo zapisywane do CSV ----------------------------------------------------------
        hotel.earlyBookingDiscounts.add(new EarlyBookingDiscount(1, 5));
        hotel.earlyBookingDiscounts.add(new EarlyBookingDiscount(3, 10));
        hotel.earlyBookingDiscounts.add(new EarlyBookingDiscount(6, 15));

        //Zni¿ka na Listopad
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 10, 1);
        Date discountBegin = cal.getTime();
        cal.set(2015, 11, 1);
        Date discountEnd = cal.getTime();
        hotel.seasonalDiscounts.add(new SeasonalDiscount(discountBegin, discountEnd, 10));
        //-------------------------------------------------------------------------------------

        Room room = new Room(1, 4, 80, Room.RoomStandard.HIGH);
        hotel.addRoom(room);
        Person examplePerson = new Person(0, "Jan", "Kowalski");
        examplePerson.setDiscount(20);

        Calendar from =  Calendar.getInstance();
        from.set(2015, 10, 1);
        Calendar to =  Calendar.getInstance();
        to.set(2015, 10, 10);

        Reservation reservation = room.addReservation(from.getTime(), to.getTime(), examplePerson);
        reservation.seats = 2;

        System.out.println(reservation.calculatePrice());

    }

}