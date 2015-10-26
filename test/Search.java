import hotel.Hotel;
import hotel.Reservation;
import hotel.Room;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class Search {
    Hotel hotel;

    Search() throws IOException {
        hotel = new Hotel();
    }

    @org.junit.Test
    public void testFindAvailableRooms() throws Exception {
        Room newRoom = new Room(1, 4, 100, Room.RoomStandard.HIGH);
        assertNull(newRoom.getCurrentReservation());
        hotel.addRoom(newRoom);
        Reservation r1 = new Reservation(new Date(), new Date());
        r1.seats = 3;
        Reservation r2 = new Reservation(new Date(), new Date());
        r2.seats = 5;
        assertSame(new ArrayList<Room>().add(newRoom), hotel.availableRooms(r1));
        assertSame(new ArrayList<Room>(), hotel.availableRooms(r2));
        assertTrue(hotel.availableRooms(r1).get(0).addReservation(r1));
    }
}
