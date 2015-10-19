import static org.junit.Assert.assertEquals;

import hotel.Hotel;
import hotel.Reservation;
import hotel.Room;
import org.junit.Test;

import java.util.Date;

public class Search {
    Hotel hotel;

    Search(){
        hotel = new Hotel();
    }

    @org.junit.Test
    public void testFindAvailableRooms() throws Exception {
        Room newRoom = new Room(1, 4, 100, Room.RoomStandard.HIGH);
        assertEquals(null, newRoom.getCurrentReservation());
        hotel.addRoom(newRoom);
        Reservation newReservation = new Reservation(new Date(), new Date());
        newReservation.seats = 3;
        newRoom.addReservation(newReservation);
    }
}
