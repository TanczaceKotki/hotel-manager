package hotel;

import java.util.Date;

public class Reservation extends Interval {

    public int seats;

    Reservation(Date b, Date e) {
        super(b, e);
    }
}