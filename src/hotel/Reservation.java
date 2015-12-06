package hotel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Reservation extends Interval {

    private int id;
    Room room;
    Date creationDate;
    Date originalBeginDate;
    public int seats;
    public Person person;
    float alreadyPaid;

    static int generateId() {
        return (int) (System.currentTimeMillis() / 1000L);
    }


    public Reservation(int id, Date b, Date e, Room room, Person person, int seats) {
        super(b, e);
        if(id == 0) {
            this.id = id;
        } else {
            this.id = generateId();
        }
        originalBeginDate = b;
        this.person = person;
        this.room = room;
        creationDate = new Date();
        alreadyPaid = 0.0f;
        this.seats = seats;

    }

    public boolean changeDates(Date b, Date e) {
        Interval tmp = new Interval(b, e);
        for(Reservation reservation: Hotel.getInstance().reservations) {
            if(reservation != this && reservation.collides(tmp))
                return false;
        }

        this.begin = b;
        this.end = e;
        return true;
    }

    public Integer getId() {
        return id;
    }

    public int getRoomId() {
        return room.getNumber();
    }
    public int getPersonId() {
        return person.getId();
    }
    public int getSeats() {
        return seats;
    }


    public float calculatePrice() {

        Calendar from = Calendar.getInstance();
        from.setTime(begin);
        Calendar to = Calendar.getInstance();
        to.setTime(end);

        float basePerDay = room.getBasePricePerDay();
        float price = 0;

        // Price calculator - base price plus seasons, seats bonus costs
        TimeIgnoringComparator comparator = new TimeIgnoringComparator();

        // Seasons discounts filter
        ArrayList<SeasonalDiscount> collidingDiscounts = new ArrayList<>();
        for(SeasonalDiscount seasonalDiscount : Hotel.getInstance().seasonalDiscounts) {
            if(seasonalDiscount.collides(this))
                collidingDiscounts.add(seasonalDiscount);
        }


        while(comparator.compare(from, to) <= 0) {

            float dayPrice = basePerDay * seats;
            int sdValue = 0;
            for(SeasonalDiscount seasonalDiscount : collidingDiscounts) {
                if(seasonalDiscount.contains(from.getTime())) {
                    if(seasonalDiscount.getPercentage() > sdValue)
                        sdValue = seasonalDiscount.getPercentage();
                }

            }

            price += dayPrice * (float)(100 - sdValue)/100.0f;
            from.add(Calendar.DATE, 1);
        }


        // Discount for regular customers
        if(person != null) {
            price -= (float) person.getDiscount() / 100.0f * price;
        }

        // EarlyBook discount
        Calendar creation = Calendar.getInstance();
        creation.setTime(creationDate);
        Calendar discountTo = Calendar.getInstance();

        int ebDiscountValue = 0;
        for(EarlyBookingDiscount earlyBookingDiscount: Hotel.getInstance().earlyBookingDiscounts) {

            discountTo.setTime(originalBeginDate);
            discountTo.add(Calendar.MONTH, -earlyBookingDiscount.getMonths());

            if(comparator.compare(creation, discountTo) <= 0) {
                if(earlyBookingDiscount.getPercentage() > ebDiscountValue)
                    ebDiscountValue = earlyBookingDiscount.getPercentage();
            }
        }

        price -= (float)ebDiscountValue/100.0f * price;

        return price;
    }

    public void pay(float amount) {
        alreadyPaid += amount;
    }

    public float leftToPay() {
        return calculatePrice() - alreadyPaid;
    }


}