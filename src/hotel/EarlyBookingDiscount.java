package hotel;

public class EarlyBookingDiscount {

    private int months;
    int percentage;

    public EarlyBookingDiscount(int newMonths, int newPercentage) {
        months = newMonths;
        percentage = newPercentage;

    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int newMonths) {
        months = newMonths;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int newPercentage) {
        percentage = newPercentage;
    }

}
