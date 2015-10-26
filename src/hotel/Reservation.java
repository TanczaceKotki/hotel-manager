package hotel;

import java.util.Date;

public class Reservation extends Interval {

    int roomId;
    Date creationDate;
    public boolean paid = false;
    public int seats;

    public Reservation(Date b, Date e, int id) {
        super(b, e);
        roomId = id;
        creationDate = new Date();
    }

    public void setAsPaid() {paid = true;}

    public float calculatePrice() {
        return 5.0f;
    }

}