package hotel;

import java.util.Date;

public class SeasonalDiscount extends Interval {

    private int percentage;

    public SeasonalDiscount(Date b, Date e, int p) {
        super(b, e);
        percentage = p;

    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int newPercentage) {
        percentage = newPercentage;
    }


}
