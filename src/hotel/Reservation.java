package hotel;

import java.util.Date;

public class Reservation extends Interval {

    public boolean paid = false;
    public int seats;

    public Reservation(Date b, Date e) {
        super(b, e);
    }

}