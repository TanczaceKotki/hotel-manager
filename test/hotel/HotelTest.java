package hotel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class HotelTest {

    Hotel hotel;
    Room room1, room2, room3, room4;
    Person client1, client2, client3, client4;
    Reservation r1, r2, r3, r4, r5, r6, r7;
    Interval today;
    @Before
    public void setUp() throws Exception {

        //Rooms
        hotel = hotel.getInstance();
        room1 = new Room(9999, 4, 22, Room.RoomStandard.HIGH);
        room2 = new Room(9998, 3, 16, Room.RoomStandard.LOW);
        room3 = new Room(9997, 5, 18, Room.RoomStandard.LOW);
        room4 = new Room(9996, 2, 25, Room.RoomStandard.HIGH);

        hotel.addRoom(room1);
        hotel.addRoom(room2);
        hotel.addRoom(room3);
        hotel.addRoom(room4);

        //Clients
        client1 = new Person(Person.generateId(), "Adam", "Nowak", "374756345", "nowak@gmail.com");
        client2 = new Person(Person.generateId(), "Anna", "Kowalska", "485962024", "kowalska@gmail.com");
        client3 = new Person(Person.generateId(), "Mike", "Smith", "473918495", "mike@gmail.com");
        client4 = new Person(Person.generateId(), "Chuck", "Norris", "123456789", "gmail@chucknorris.com");


        hotel.addClient(client1);
        hotel.addClient(client2);
        hotel.addClient(client3);
        hotel.addClient(client4);

        //Reservations
        Calendar b = Calendar.getInstance();
        b.add(Calendar.DATE, -2);
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 7);
        r1 = room1.addReservation(b.getTime(), e.getTime(), client1, 2);
        r1.pay(r1.leftToPay());

        b = Calendar.getInstance();
        b.add(Calendar.DATE, 50);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, 55);
        r2 = room2.addReservation(b.getTime(), e.getTime(), null, 3);

        b = Calendar.getInstance();
        b.add(Calendar.DATE, -12);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, 1);
        r3 = room3.addReservation(b.getTime(), e.getTime(), client2, 5);
        r3.pay(r3.leftToPay());

        b = Calendar.getInstance();
        b.add(Calendar.DATE, 52);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, 58);
        r4 = room4.addReservation(b.getTime(), e.getTime(), client2, 2);

        b = Calendar.getInstance();
        b.add(Calendar.DATE, 15);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, 20);
        r5 = room1.addReservation(b.getTime(), e.getTime(), client3, 4);

        b = Calendar.getInstance();
        b.add(Calendar.DATE, -12);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, -4);
        r6 = room2.addReservation(b.getTime(), e.getTime(), null, 2);

        b = Calendar.getInstance();
        b.add(Calendar.DATE, 3);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, 13);
        r7 = room4.addReservation(b.getTime(), e.getTime(), client4, 1);

        today = new Interval(Calendar.getInstance().getTime(), Calendar.getInstance().getTime());

    }

    @After
    public void tearDown() throws Exception {

        hotel.removeRoom(room1);
        hotel.removeRoom(room2);
        hotel.removeRoom(room3);
        hotel.removeRoom(room4);

        hotel.removeClient(client1);
        hotel.removeClient(client2);
        hotel.removeClient(client3);
        hotel.removeClient(client4);

        hotel.removeReservation(r1);
        hotel.removeReservation(r2);
        hotel.removeReservation(r3);
        hotel.removeReservation(r4);
        hotel.removeReservation(r5);
        hotel.removeReservation(r6);
        hotel.removeReservation(r7);
    }

    @Test
    public void testGetInstance() throws Exception {
        Hotel instance = Hotel.getInstance();
        assertEquals(instance, hotel);
    }

    @Test
    public void testNumOfPeople() throws Exception {
        //Current reservations: r1, r3 (7 seats total)
        assertEquals(hotel.numOfPeople(), r1.getSeats() + r3.getSeats());
    }

    @Test
    public void testFindClients() throws Exception {

        ArrayList<Person> results;
        //@gmail users
        results = hotel.findClients("", "", "", "@gmail");
        assertEquals(3, results.size());

        //by name
        results = hotel.findClients("Anna", "", "", "");
        assertEquals(1, results.size());

        //by name and surename
        results = hotel.findClients("Chuck", "Nor", "", "");
        assertEquals(1, results.size());

        //by name and phone number
        results = hotel.findClients("Mike", "", "473918495", "");
        assertEquals(1, results.size());

    }

    @Test
    public void testAvailableRooms() throws Exception {

        //Available for today - all except rooms for reservations: r1 and r3
        ArrayList<Room> results = hotel.availableRooms(today);
        assertEquals(2, results.size());
        assertFalse(results.contains(room1));
        assertFalse(results.contains(room3));
    }

    @Test
    public void testFindRooms() throws Exception {

        //Searching by available interval
        // room1 and room3 should be available
        Calendar b = Calendar.getInstance();
        b.add(Calendar.DATE, 49);
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 60);
        ArrayList<Room> results = hotel.findRooms(new Interval(b.getTime(), e.getTime()), 0, 10000.0f, null);
        assertEquals(2, results.size());
        assertTrue(results.contains(room1));
        assertTrue(results.contains(room3));

        //searching by standard
        results = hotel.findRooms(null, 0, 10000.0f, Room.RoomStandard.HIGH);
        assertEquals(2, results.size());
        results = hotel.findRooms(null, 0, 10000.0f, Room.RoomStandard.LOW);
        assertEquals(2, results.size());
        //any
        results = hotel.findRooms(null, 0, 10000.0f, null);
        assertEquals(4, results.size());

        //searching by min seats
        results = hotel.findRooms(null, 5, 10000.0f, null);
        assertEquals(1, results.size());
        assertEquals(results.get(0), room3);
        results = hotel.findRooms(null, 4, 10000.0f, null);
        assertEquals(2, results.size());
        results = hotel.findRooms(null, 3, 10000.0f, null);
        assertEquals(3, results.size());
        results = hotel.findRooms(null, 2, 10000.0f, null);
        assertEquals(4, results.size());
        results = hotel.findRooms(null, 1, 10000.0f, null);
        assertEquals(4, results.size());

        //max price
        results = hotel.findRooms(null, 0, 20.0f, null);
        assertEquals(2, results.size());
        assertTrue(results.contains(room2));
        assertTrue(results.contains(room3));
        results = hotel.findRooms(null, 0, 24.0f, null);
        assertEquals(3, results.size());
        assertTrue(results.contains(room1));
        assertTrue(results.contains(room2));
        assertTrue(results.contains(room3));

    }

    @Test
    public void testFindReservations() throws Exception {

        //by roomNumber
        //room2 - r2, r6
        ArrayList<Reservation> results = hotel.findReservations(room2.getNumber(), -1, null, -1, null);
        assertEquals(2, results.size());
        assertTrue(results.contains(r2));
        assertTrue(results.contains(r6));

        //bySeats
        results = hotel.findReservations(-1, 2, null, -1, null);
        assertEquals(3, results.size());
        assertTrue(results.contains(r1));
        assertTrue(results.contains(r4));
        assertTrue(results.contains(r6));

        //by creationdate
        results = hotel.findReservations(-1, -1, today, -1, null);
        assertEquals(7, results.size());
        Date tmp = results.get(0).creationDate;
        Calendar newCreationDate = Calendar.getInstance();
        newCreationDate.add(Calendar.DATE, -3);
        results.get(0).creationDate = newCreationDate.getTime();
        results = hotel.findReservations(-1, -1, today, -1, null);
        assertEquals(6, results.size());
        results.get(0).creationDate = tmp;

        //by Client
        results = hotel.findReservations(-1, -1, null, client2.getId(), null);
        assertEquals(2, results.size());
        assertTrue(results.contains(r3));
        assertTrue(results.contains(r4));
        //seats = 1
        results = hotel.findReservations(-1, 1, null, client4.getId(), null);
        assertEquals(1, results.size());
        assertTrue(results.contains(r7));

        //by colliding Interval
        Calendar b = Calendar.getInstance();
        b.add(Calendar.DATE, -20);
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 20);

        results = hotel.findReservations(-1, -1, null, -1, new Interval(b.getTime(), e.getTime()));
        assertEquals(5, results.size());
        assertFalse(results.contains(r2));
        assertFalse(results.contains(r4));


    }



}