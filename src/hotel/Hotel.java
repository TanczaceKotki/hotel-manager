package hotel;

import hotel.exceptions.RoomAlreadyExistsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

    public ArrayList<hotel.Room> rooms;
    public ArrayList<SeasonalDiscount> seasonalDiscounts;
    public ArrayList<EarlyBookingDiscount> earlyBookingDiscounts;
    public ArrayList<Reservation> reservations;
    public ArrayList<Person> clients;




    public Hotel() throws IOException {
        rooms = new ArrayList<Room>();
        reservations = new ArrayList<Reservation>();
        seasonalDiscounts = new ArrayList<SeasonalDiscount>();
        earlyBookingDiscounts = new ArrayList<EarlyBookingDiscount>();
        clients = new ArrayList<Person>();
        dataManager = new DataManager();
        initializeUserInterface();

    }

    // current num of accommodated people
    //Interfejs uzytkownika
    ConsoleInterface userInterface;

    void initializeUserInterface() {
        userInterface = new ConsoleInterface();

    }



    //Customers ------------------------------------------------

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

    public void addClient(Person client) {
        clients.add(client);
    }

    public void removeClient(Person client) {
        clients.remove(client);
    }

    public ArrayList<Person> findClients(String firstname, String surname, String phoneNumber, String email) {
        ArrayList<Person> results = new ArrayList<Person>();
        String pn = phoneNumber;
        pn.replaceAll("\\s+","");

        for(Person client : clients) {
            boolean nameCondition = client.getFirstname().contains(firstname);
            boolean surnameCondition = client.getSurname().contains(surname);
            boolean phoneCondition = client.getTelephone().contains(pn);
            boolean emailCondition = client.getEmail().contains(email);

            if(nameCondition && surnameCondition && phoneCondition && emailCondition)
                results.add(client);
        }
        return results;
    }

    public Person getClientById(int id) {
        for(Person client : clients) {
            if(client.getId() == id)
                return client;
        }
        return null;
    }


    //Rooms ------------------------------------------------
    public void addRoom(Room room) throws RoomAlreadyExistsException{
        if(rooms.stream().filter(t -> t.getNumber() == room.getNumber()).count() == 0) {
            rooms.add(room);
        } else {
            throw new RoomAlreadyExistsException();
        }
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    public ArrayList<Room> availableRooms(Interval interval) {
        ArrayList<Room> available = new ArrayList<>();
        for(Room room: rooms) {
            if(room.isAvailable(interval))
                available.add(room);
        }
        return available;
    }

    public ArrayList<Room> findRooms(Interval availableIn, int seats, float maxPrice, Room.RoomStandard standard) {
        ArrayList<Room> available = availableRooms(availableIn);
        ArrayList<Room> found = new ArrayList<Room>();


        for(Room room: available) {
            boolean standardCondition = true;
            if(standard != null) {
                standardCondition = (standard == room.standard);
            }

            if(room.getSeats() >= seats && standardCondition && room.getBasePricePerDay() <= maxPrice) {
                found.add(room);
            }
        }
        return found;
    }

    public Room getRoomByNumber(int number) {
        for(Room room: rooms) {
            if(room.getNumber() == number) {
                return room;
            }
        }
        return null;
    }

    //Reservations ------------------------------------------------
    public Reservation getReservationById(int id) {
        for(Reservation reservation: reservations) {
            if(reservation.getId() == id) {
                return reservation;
            }
        }
        return null;
    }


    //Discounts ------------------------------------------------
    public void addSeasonalDiscount(SeasonalDiscount sd) {
        seasonalDiscounts.add(sd);
    }
    public void removeSeasonalDiscount(int index) {
        seasonalDiscounts.remove(index);
    }

    public String[] getSeasonalDiscountInfo() {
        String[] info = new String[seasonalDiscounts.size()];
        for(int i=0; i< seasonalDiscounts.size(); i++) {
            info[i] = "Id: "+i+"\n"+  seasonalDiscounts.get(i).toString();
        }
        return info;
    }

    public void addEarlyBookDiscount(EarlyBookingDiscount ebd) {
        earlyBookingDiscounts.add(ebd);
    }
    public void removeEarlyBookDiscount(EarlyBookingDiscount ebd) {
        earlyBookingDiscounts.remove(ebd);
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


        //Uruchamianie interfejsu
        hotel.userInterface.start();

    }

}