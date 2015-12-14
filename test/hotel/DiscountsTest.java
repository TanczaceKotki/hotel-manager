package hotel;

import com.sun.org.apache.xml.internal.serializer.utils.SerializerMessages_zh_CN;
import hotel.*;
import hotel.Room;
import hotel.Person;
import hotel.Reservation;
import hotel.exceptions.DateFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

public class DiscountsTest {

    Hotel hotel;
    Room room;

    @Before
    public void setUp() throws Exception {
        hotel = Hotel.getInstance();
        room = new Room(99999, 4, 20, Room.RoomStandard.HIGH);
        int reservationDays = 7;
        removeEarlyBookDiscounts();
        removeSeasonalDiscounts();
    }

    @After
    public void searDown() throws Exception {
        hotel.removeRoom(room);
    }

    void removeEarlyBookDiscounts() {
        try {
            hotel.removeEarlyBookDiscount(0);
        } catch (Exception e) {}
    }
    void removeSeasonalDiscounts() {
        try {
            hotel.removeSeasonalDiscount(0);
        } catch (Exception e) {}
    }

    @Test
    public void testEarlyBookDiscount() {

        EarlyBookingDiscount oneMonth = new EarlyBookingDiscount(1, 5);
        EarlyBookingDiscount twoMonths = new EarlyBookingDiscount(2, 10);
        hotel.addEarlyBookDiscount(oneMonth);
        hotel.addEarlyBookDiscount(twoMonths);

        //Adding reservation
        int reservationDays = 10;

        //No discounts - 14 days before
        Calendar b = Calendar.getInstance();
        b.add(Calendar.DATE, 14);
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, reservationDays-1 + 14);
        Reservation reservation = room.addReservation(b.getTime(), e.getTime(), null, 2);
        assertEquals(reservation.calculatePrice(), reservation.getSeats() * room.getBasePricePerDay() * reservationDays, 0.01);
        room.cancelReservation(reservation);

        //One month discount - 40 days before
        b = Calendar.getInstance();
        b.add(Calendar.DATE, 40);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, reservationDays-1 + 40);
        reservation = room.addReservation(b.getTime(), e.getTime(), null, 2);
        assertEquals(reservation.calculatePrice(), reservation.getSeats() * room.getBasePricePerDay() * reservationDays * (100.0-oneMonth.getPercentage()) / 100.0f, 0.01);
        room.cancelReservation(reservation);


        //Two or more months discount - 80 days before
        b = Calendar.getInstance();
        b.add(Calendar.DATE, 80);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, reservationDays-1 + 80);
        reservation = room.addReservation(b.getTime(), e.getTime(), null, 2);
        assertEquals(reservation.calculatePrice(), reservation.getSeats() * room.getBasePricePerDay() * reservationDays * (100.0-twoMonths.getPercentage()) / 100.0f, 0.01);
        room.cancelReservation(reservation);

        removeEarlyBookDiscounts();
    }

    @Test
    public void testSeasonalDiscount() {

        //Two weeks from now
        Calendar b = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        e.add(Calendar.DATE, 14);
        SeasonalDiscount firstDiscount = new SeasonalDiscount(b.getTime(), e.getTime(), 10);

        //Two weeks after current week
        b.add(Calendar.DATE, 7);
        e.add(Calendar.DATE, 7);
        SeasonalDiscount secondDiscount = new SeasonalDiscount(b.getTime(), e.getTime(), 15);

        //Registering discounts
        hotel.addSeasonalDiscount(firstDiscount);
        hotel.addSeasonalDiscount(secondDiscount);

        //Some tests may require modification after changing this value
        int reservationDays = 7;

        //First discount
        b = Calendar.getInstance();
        e = Calendar.getInstance();
        e.add(Calendar.DATE, reservationDays-1);
        Reservation reservation = room.addReservation(b.getTime(), e.getTime(), null, 3);
        assertEquals(reservation.calculatePrice(), reservationDays * reservation.getSeats() * room.getBasePricePerDay() * (100-firstDiscount.getPercentage()) / 100, 0.01);
        room.cancelReservation(reservation);

        //Second discount
        b = Calendar.getInstance();
        b.add(Calendar.DATE, 14);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, (reservationDays-1) + 14);
        reservation = room.addReservation(b.getTime(), e.getTime(), null, 3);
        assertEquals(reservation.calculatePrice(), reservationDays * reservation.getSeats() * room.getBasePricePerDay() * (100-secondDiscount.getPercentage()) / 100, 0.01);
        room.cancelReservation(reservation);

        //Both discounts - 3 days in first, 4 in second second
        b = Calendar.getInstance();
        b.add(Calendar.DATE, 4);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, (reservationDays-1) + 4);
        reservation = room.addReservation(b.getTime(), e.getTime(), null, 3);
        float expectedPrice = 3 * reservation.getSeats() * room.getBasePricePerDay() * (100 - firstDiscount.getPercentage()) / 100;
        expectedPrice += 4 * reservation.getSeats() * room.getBasePricePerDay() * (100 - secondDiscount.getPercentage()) / 100;
        assertEquals(reservation.calculatePrice(), expectedPrice, 0.01);
        room.cancelReservation(reservation);

        //No discounts
        b = Calendar.getInstance();
        b.add(Calendar.DATE, 30);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, (reservationDays-1) + 30);
        reservation = room.addReservation(b.getTime(), e.getTime(), null, 3);
        assertEquals(reservation.calculatePrice(), reservationDays * reservation.getSeats() * room.getBasePricePerDay(), 0.01);
        room.cancelReservation(reservation);

        //5 days without discount, 2 days in first discount
        b = Calendar.getInstance();
        b.add(Calendar.DATE, -5);
        e = Calendar.getInstance();
        e.add(Calendar.DATE, (reservationDays-1) - 5);
        reservation = room.addReservation(b.getTime(), e.getTime(), null, 3);
        expectedPrice = 5 * reservation.getSeats() * room.getBasePricePerDay();
        expectedPrice += 2 * reservation.getSeats() * room.getBasePricePerDay() * (100 - firstDiscount.getPercentage()) / 100;
        assertEquals(reservation.calculatePrice(), expectedPrice, 0.01);
        room.cancelReservation(reservation);

        removeSeasonalDiscounts();
    }

    @Test
    public void testPersonalDiscount() {

        Person person = new Person(0, "Adam", "Nowak");
        person.setDiscount(0);
        int reservationDays = 7;

        //No discount
        Calendar b = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        b.add(Calendar.DATE, reservationDays-1);
        Reservation reservation = room.addReservation(b.getTime(), e.getTime(), person, 1);
        assertEquals(reservation.calculatePrice(), reservation.getSeats() * room.getBasePricePerDay() * (100 - person.getDiscount()) / 100, 0.01);

        //10% discount
        person.setDiscount(0);
        assertEquals(reservation.calculatePrice(), reservation.getSeats() * room.getBasePricePerDay() * (100 - person.getDiscount()) / 100, 0.01);

        room.cancelReservation(reservation);
        room.cancelReservation(reservation);

    }

    @Test
    public void testCombinedDiscounts() {

        int reservationDays = 7;
        //Seasonal discount
        Calendar b = Calendar.getInstance();
        b.add(Calendar.MONTH, 2);
        Calendar e = Calendar.getInstance();
        e.add(Calendar.MONTH, 2);
        e.add(Calendar.DATE, reservationDays-1);
        SeasonalDiscount seasonalDiscount = new SeasonalDiscount(b.getTime(), e.getTime(), 10);
        hotel.addSeasonalDiscount(seasonalDiscount);

        //EarlyBook discount
        EarlyBookingDiscount oneMonth = new EarlyBookingDiscount(1, 5);
        hotel.addEarlyBookDiscount(oneMonth);

        //regular client discount
        Person person = new Person(0, "Adam", "Nowak");
        person.setDiscount(10);
        Reservation reservation = room.addReservation(b.getTime(), e.getTime(), person, 1);

        float expectedPrice = reservation.getSeats() * room.getBasePricePerDay() * reservationDays;

        //Discounts multiplication
        expectedPrice *= ((100.0-seasonalDiscount.getPercentage()) / 100.0);
        expectedPrice *= (100.0-oneMonth.getPercentage()) / 100.0;
        expectedPrice *= (100.0-person.getDiscount()) / 100.0;
        assertEquals(reservation.calculatePrice(), expectedPrice, 0.01);
        room.cancelReservation(reservation);

    }

}
