package hotel;

import CSV.DataManager;
import hotel.exceptions.RoomAlreadyExistsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class Hotel {

    //Singleton
    private static Hotel hotelInstance;
    public ArrayList<SeasonalDiscount> seasonalDiscounts;
    public ArrayList<EarlyBookingDiscount> earlyBookingDiscounts;
    public ArrayList<Reservation> reservations;
    private DataManager dataManager;
    private ArrayList<Room> rooms;
    private ArrayList<Person> clients;
    private int regClientDiscount = 10;
    private int regClientThreshold  = 20;

    public int getCustomerDiscount() {
        return regClientDiscount;
    }
    public int getCustomerDiscountThreshold() {
        return regClientThreshold;
    }

    // User interface
    private ConsoleInterface userInterface;


    private Hotel() throws IOException {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        seasonalDiscounts = new ArrayList<>();
        earlyBookingDiscounts = new ArrayList<>();
        clients = new ArrayList<>();
        dataManager = new DataManager();
        initializeUserInterface();
        importAllData();

    }

    public static Hotel getInstance() {
        if (hotelInstance == null) {
            try {
                hotelInstance = new Hotel();
            } catch (IOException e) {
                System.err.println("Error: invalid CSV file");
            }
        }
        return hotelInstance;

    }

    public static void main(String[] args) throws IOException {

        Hotel hotel = Hotel.getInstance();

//        //Mo?na wykorzysta? ten kod jako testowy
//        //Docelowo zapisywane do CSV ----------------------------------------------------------
//        hotel.earlyBookingDiscounts.add(new EarlyBookingDiscount(1, 5));
//        hotel.earlyBookingDiscounts.add(new EarlyBookingDiscount(3, 10));
//        hotel.earlyBookingDiscounts.add(new EarlyBookingDiscount(6, 15));
//
//        //Zni?ka na Listopad
//        Calendar cal = Calendar.getInstance();
//        cal.set(2015, 10, 1);
//        Date discountBegin = cal.getTime();
//        cal.set(2015, 11, 1);
//        Date discountEnd = cal.getTime();
//        hotel.seasonalDiscounts.add(new SeasonalDiscount(discountBegin, discountEnd, 10));
//        //-------------------------------------------------------------------------------------
//
//
        //Uruchamianie interfejsu
        hotel.userInterface.start();

    }


    //Customers ------------------------------------------------

    private void initializeUserInterface() {
        userInterface = new ConsoleInterface();

    }

    // Current num of accommodated people
    public int numOfPeople() {
        int people = 0;
        Date today = new Date();
        for (Reservation reservation : reservations) {
            if (reservation.contains(today)) {
                people += reservation.seats;
            }
        }
        return people;

    }

    public void addClient(Person client) {
        clients.add(client);
    }

    public void removeClient(Person client) {
        ArrayList<Reservation> clientReservations = findReservations(-1, 0, null, client.getId(), null);
        for (Reservation r : clientReservations) {
            r.person = null;
        }
        clients.remove(client);
    }

    public ArrayList<Person> findClients(String firstname, String surname, String phoneNumber, String email) {
        ArrayList<Person> results = new ArrayList<Person>();
        String pn = phoneNumber;
        pn.replaceAll("\\s+", "");

        for (Person client : clients) {
            boolean nameCondition = client.getFirstname().contains(firstname);
            boolean surnameCondition = client.getSurname().contains(surname);
            boolean phoneCondition = client.getTelephone().contains(pn);
            boolean emailCondition = client.getEmail().contains(email);

            if (nameCondition && surnameCondition && phoneCondition && emailCondition)
                results.add(client);
        }
        return results;
    }

    public Person getClientById(int id) {
        for (Person client : clients) {
            if (client.getId() == id)
                return client;
        }
        return null;
    }

    //Rooms ------------------------------------------------
    public void addRoom(Room room) throws RoomAlreadyExistsException {
        if (rooms.stream().filter(t -> t.getNumber() == room.getNumber()).count() == 0) {
            rooms.add(room);
        } else {
            throw new RoomAlreadyExistsException();
        }
    }

    public void removeRoom(Room room) {
        ArrayList<Reservation> roomReservations = findReservations(room.getNumber(), 0, null, 0, null);
        for (Reservation r : roomReservations) {
            r.room = null;
        }
        rooms.remove(room);
    }

    public ArrayList<Room> availableRooms(Interval interval) {
        //return rooms.stream().filter(room -> room.isAvailable(interval)).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Room> available = new ArrayList<Room>();
        for(Room room : rooms) {
            if (room.isAvailable(interval)) {
                available.add(room);
            }
        }
        return available;
    }

    public ArrayList<Room> findRooms(Interval availableIn, int seats, float maxPrice, Room.RoomStandard standard) {
        ArrayList<Room> available;
        if(availableIn != null) {
            available = availableRooms(availableIn);
        } else {
            available = rooms;
        }
        ArrayList<Room> found = new ArrayList<>();


        for (Room room : available) {
            boolean standardCondition = true;
            if (standard != null) {
                standardCondition = (standard == room.standard);
            }

            if (room.getSeats() >= seats && standardCondition && room.getBasePricePerDay() <= maxPrice) {
                found.add(room);
            }
        }
        return found;
    }

    public Room getRoomByNumber(int number) {
        for (Room room : rooms) {
            if (room.getNumber() == number) {
                return room;
            }
        }
        return null;
    }

    //Reservations ------------------------------------------------
    public Reservation getReservationById(int id) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == id) {
                return reservation;
            }
        }
        return null;
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

    public ArrayList<Reservation> findReservations(int roomId, int seats, Interval creationDateInterval, int personId, Interval collisionInterval) {

        ArrayList<Reservation> result = new ArrayList<>();

        for (Reservation r : reservations) {

            boolean roomCondition = true;
            if (roomId >= 0)
                roomCondition = r.getRoomId() == roomId;

            boolean seatsCondition = true;
            if (seats > 0)
                seatsCondition = r.getSeats() == seats;

            boolean personCondition = true;
            if (personId > 0)
                personCondition = r.getPersonId() == personId;

            boolean createCondition = true;
            if (creationDateInterval != null)
                createCondition = creationDateInterval.contains(r.creationDate);

            boolean collisionCondition = true;
            if (collisionInterval != null)
                collisionCondition = collisionInterval.collides(r);


            if (roomCondition & seatsCondition & personCondition & createCondition & collisionCondition) {
                result.add(r);
            }
        }

        return result;

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
        for (int i = 0; i < seasonalDiscounts.size(); i++) {
            info[i] = "Index: " + i + "\n" + seasonalDiscounts.get(i).toString();
        }
        return info;
    }

    public void addEarlyBookDiscount(EarlyBookingDiscount ebd) {
        earlyBookingDiscounts.add(ebd);
    }

    public void removeEarlyBookDiscount(int index) {
        earlyBookingDiscounts.remove(index);
    }

    public String[] getEarlyBookDiscountInfo() {
        String[] info = new String[earlyBookingDiscounts.size()];
        for (int i = 0; i < earlyBookingDiscounts.size(); i++) {
            info[i] = "Index: " + i + "\n" + earlyBookingDiscounts.get(i).toString();
        }
        return info;
    }

    public void importAllData() {
        rooms = dataManager.roomImport();
        clients = dataManager.personImport();
        reservations = dataManager.reservationImport(rooms, clients);
    }

    public void exportAllData() {
        dataManager.roomExport(rooms);
        dataManager.personExport(clients);
        dataManager.reservationExport(reservations, rooms, clients);
    }

}