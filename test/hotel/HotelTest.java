package hotel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Morr on 2015-12-14.
 */
public class HotelTest {

    Hotel hotel;
    Room room1, room2, room3, room4;
    Person client1, client2, client3;
    Reservation r1, r2, r3, r4, r5, r6, r7;

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
        client3 = new Person(Person.generateId(), "Chuck", "Norris", "123456789", "gmail@chucknorris.com");

        hotel.addClient(client1);
        hotel.addClient(client2);
        hotel.addClient(client3);

        //Reservations



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


    }

    @Test
    public void testGetInstance() throws Exception {
        Hotel instance = Hotel.getInstance();
        assertEquals(instance, hotel);
    }

    @Test
    public void testNumOfPeople() throws Exception {

    }

    @Test
    public void testFindClients() throws Exception {

    }

    @Test
    public void testAvailableRooms() throws Exception {

    }

    @Test
    public void testFindRooms() throws Exception {

    }

    @Test
    public void testFindReservations() throws Exception {

    }
}