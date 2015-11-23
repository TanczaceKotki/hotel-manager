package hotel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Reservation extends Interval {

    Room room;
    Date creationDate;
    Date originalBeginDate;
    public int seats;
    public Person person;
    float alreadyPaid;

//  TODO: where are seats?
    public Reservation(Date b, Date e, Room newRoom, Person newPerson) {
        super(b, e);
        originalBeginDate = b;
        person = newPerson;
        room = newRoom;
        creationDate = new Date();
        alreadyPaid = 0.0f;

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

    public int getRoomId() {
        return room.getNumber();
    }

    public float calculatePrice() {

        Calendar from = Calendar.getInstance();
        from.setTime(begin);
        Calendar to = Calendar.getInstance();
        to.setTime(end);

        float basePerDay = room.getBasePricePerDay();
        float price = 0;

        //Obliczanie ceny bazowej z uwzgl�dnieniem zni�ek sezonowych i liczby os�b
        TimeIgnoringComparator comparator = new TimeIgnoringComparator();

        //Filtrowanie zni�ek okresowych
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


        //Zni�ka dla sta�ych klient�w
        price -= (float)person.getDiscount()/100.0f * price;


        //Zni�ka EarlyBook
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