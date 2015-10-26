package hotel;

import java.util.Date;

public class Discount extends Interval {

    int percentage;

    public Discount(Date b, Date e, int p) {
        super(b, e);
        percentage = p;

    }

}
