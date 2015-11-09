package hotel;

import java.util.Date;

public class Reservation extends Interval {

    int roomId;
    Date creationDate;
    public boolean paid = false;
    public int seats;

    public Reservation(Date b, Date e, Room room) {
        super(b, e);
        roomId = room.getId();
        creationDate = new Date();
    }

    public void setAsPaid() {paid = true;}

    public float calculatePrice() {
        return 5.0f;
    }

}