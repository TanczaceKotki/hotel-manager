package hotel;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class PersonTest {

    @Test
    public void testUpdateDiscount() throws Exception {

        Hotel hotel = Hotel.getInstance();
        Person person = new Person(0, "Adam", "Nowak", "442654268", "nowak@gmail.com");
        hotel.addClient(person);

        assertEquals(person.getDiscount(), 0);
        Room room = new Room(9999, 5, 10, Room.RoomStandard.HIGH);

        int days = Math.max(hotel.getCustomerDiscountThreshold()-5, 0);
        Calendar b = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, days);
        Reservation r1 = room.addReservation(b.getTime(), e.getTime(), person, 2);
        r1.pay(r1.leftToPay());
        assertEquals(person.getDiscount(), 0);

        b = Calendar.getInstance();
        b.add(Calendar.DATE, days+5);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, days+12);
        Reservation r2 = room.addReservation(b.getTime(), e.getTime(), person, 2);
        r2.pay(r2.leftToPay());
        assertEquals(person.getDiscount(), hotel.getCustomerDiscount());

        room.cancelReservation(r1);
        room.cancelReservation(r2);
        hotel.removeRoom(room);
        hotel.removeClient(person);

    }

}