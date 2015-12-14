package hotel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class ReservationTest {

    Hotel hotel;
    Room room;

    @Before
    public void setUp() throws Exception {
        hotel = Hotel.getInstance();
        room = new Room(9999, 5, 10, Room.RoomStandard.HIGH);
        hotel.addRoom(room);

    }

    @After
    public void tearDown() throws Exception {
        hotel.removeRoom(room);
    }

    @Test
    public void testChangeDates() throws Exception {

        //First reservation (r1)
        Calendar b = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 6);
        Reservation r1 = room.addReservation(b.getTime(), e.getTime(), null, 2);

        //Colliding (r2)
        b = Calendar.getInstance();
        b.add(Calendar.DATE, 3);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, 4);
        Interval collidingTime = new Interval(b.getTime(), e.getTime());
        Reservation r2 = room.addReservation(collidingTime.getBegin(), collidingTime.getEnd(), null, 2);
        assertNull(r2);

        //Not colliding (r3)
        b = Calendar.getInstance();
        b.add(Calendar.DATE, 7);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, 12);
        Reservation r3 = room.addReservation(b.getTime(), e.getTime(), null, 2);
        assertNotNull(r3);

        //Unable to change - collision
        b = Calendar.getInstance();
        b.add(Calendar.DATE, 8);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, 11);
        assertFalse(r1.changeDates(b.getTime(), e.getTime()));

        //Change success
        b = Calendar.getInstance();
        b.add(Calendar.DATE, -10);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, -5);
        assertTrue(r1.changeDates(b.getTime(), e.getTime()));

        //r2 could be created now
        r2 = room.addReservation(collidingTime.getBegin(), collidingTime.getEnd(), null, 2);

        room.cancelReservation(r1);
        room.cancelReservation(r2);
        room.cancelReservation(r3);

    }

    @Test
    public void testPayAndLeftToPay() throws Exception {

        Calendar b = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 6);
        Reservation r1 = room.addReservation(b.getTime(), e.getTime(), null, 2);

        assertEquals(r1.calculatePrice(), r1.leftToPay(), 0.01);
        r1.pay(100.0f);
        assertEquals(r1.leftToPay(), r1.calculatePrice()-100.0f, 0.01);
        r1.pay(r1.calculatePrice()-100.0f);
        assertEquals(r1.leftToPay(), 0.0f, 0.01);
        r1.pay(-20.0f);
        assertEquals(r1.leftToPay(), 20.0f, 0.01);

        room.cancelReservation(r1);

    }


}