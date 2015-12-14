package hotel;

import hotel.exceptions.RoomAlreadyExistsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class RoomTest {

    Hotel hotel;
    Room room;


    @Before
    public void setUp() throws Exception {
        hotel = hotel.getInstance();
        room = new Room(9999, 5, 10, Room.RoomStandard.HIGH);
        hotel.addRoom(room);
    }

    @After
    public void tearDown() throws Exception {
        hotel.removeRoom(room);
    }

    @Test
    public void testAddReservation() throws Exception {

        Calendar b = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 6);
        Interval reservationTime = new Interval(b.getTime(), e.getTime());
        b.add(Calendar.DATE, 2);
        e.add(Calendar.DATE, 6);
        Interval collidingTime = new Interval(b.getTime(), e.getTime());

        Reservation r1 = room.addReservation(reservationTime.getBegin(), reservationTime.getEnd(), null, 2);
        Reservation r2 = room.addReservation(reservationTime.getBegin(), reservationTime.getEnd(), null, 3);

        r1 = hotel.getReservationById(r1.getId());

        assertNotNull(r1);
        assertNull(r2);

        room.cancelReservation(r1);

    }

    @Test
    public void testCancelReservation() throws Exception {

        Calendar b = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 6);

        Reservation r1 = room.addReservation(b.getTime(), e.getTime(), null, 2);
        assertNotNull(hotel.getReservationById(r1.getId()));
        assertTrue(room.cancelReservation(r1));
        assertNull(hotel.getReservationById(r1.getId()));

    }

    @Test
    public void testChangeNumber() throws Exception {

        Room anotherRoom = new Room(9998, 5, 10, Room.RoomStandard.HIGH);
        hotel.addRoom(anotherRoom);

        //Number already taken
        try {
            room.changeNumber(anotherRoom.getNumber());
            assertEquals(true, false);
        } catch(RoomAlreadyExistsException e) {}

        //Ok
        room.changeNumber(9997);
        assertEquals(room.getNumber(), 9997);

        hotel.removeRoom(anotherRoom);
    }

    @Test
    public void testIsAvailable() throws Exception {

        Calendar b = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 6);
        Interval reservationTime = new Interval(b.getTime(), e.getTime());

        b.add(Calendar.DATE, 2);
        e.add(Calendar.DATE, 6);
        Interval collidingTime = new Interval(b.getTime(), e.getTime());

        b.add(Calendar.DATE, 20);
        e.add(Calendar.DATE, 30);
        Interval avalileableTime = new Interval(b.getTime(), e.getTime());

        Reservation reservation = room.addReservation(reservationTime.getBegin(), reservationTime.getEnd(), null, 2);
        assertFalse(room.isAvailable(collidingTime));
        assertTrue(room.isAvailable(avalileableTime));

        room.cancelReservation(reservation);

    }

    @Test
    public void testGetCurrentReservation() throws Exception {


        assertNull(room.getCurrentReservation());
        Calendar b = Calendar.getInstance();
        b.add(Calendar.DATE, -3);
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 3);
        Reservation reservation = room.addReservation(b.getTime(), e.getTime(), null, 2);
        assertNotNull(room.getCurrentReservation());

        room.cancelReservation(reservation);

    }



}