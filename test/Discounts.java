import hotel.*;
import hotel.Room;
import hotel.Person;
import hotel.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Calendar;
import java.util.Date;

@RunWith(Suite.class)
@Suite.SuiteClasses(Search.class)
public class Discounts {

    @Test
    public void testAll() {
        Hotel hotel = Hotel.getInstance();

        hotel.earlyBookingDiscounts.add(new EarlyBookingDiscount(1, 5));
        hotel.earlyBookingDiscounts.add(new EarlyBookingDiscount(3, 10));
        hotel.earlyBookingDiscounts.add(new EarlyBookingDiscount(6, 15));

        // November discount
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 10, 1);
        Date discountBegin = cal.getTime();
        cal.set(2015, 11, 1);
        Date discountEnd = cal.getTime();
        hotel.seasonalDiscounts.add(new SeasonalDiscount(discountBegin, discountEnd, 10));

        Room room = new Room(1, 4, 80, Room.RoomStandard.HIGH);
        try {
            hotel.addRoom(room);
        } catch (Exception e) {}

        Person examplePerson = new Person(0, "Jan", "Kowalski");

        examplePerson.setDiscount(20);

        Calendar from =  Calendar.getInstance();
        from.set(2015, 10, 1);
        Calendar to =  Calendar.getInstance();
        to.set(2015, 10, 10);

        Reservation reservation = room.addReservation(from.getTime(), to.getTime(), examplePerson, room.getSeats());
        reservation.seats = 2;

        System.out.println(reservation.calculatePrice());
    }

}
