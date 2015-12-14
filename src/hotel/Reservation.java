package hotel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Reservation extends Interval {

    private int id;
    Room room;
    Date creationDate;
    Date originalBeginDate;
    public int seats;
    public Person person;
    float alreadyPaid;

    static int generateId() {
        UUID idOne = UUID.randomUUID();
        String str = ""+idOne;
        int uid = str.hashCode();
        String filterStr = ""+uid;
        str = filterStr.replaceAll("-", "");
        int id = Integer.parseInt(str);
        return id;
    }


    public Reservation(int id, Date b, Date e, Room room, Person person, int seats) {
        super(b, e);
        if(id == 0) {
            this.id = generateId();
        } else {
            this.id = id;
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

        setDates(b, e);
        return true;
    }

    public Integer getId() {
        return id;
    }

    public int getRoomId() {
        if(room != null) {
            return room.getNumber();
        } else {
            return -1;
        }
    }

    public int getPersonId() {
        if(person != null) {
            return person.getId();
        } else {
            return 0;
        }
    }

    public int getSeats() {
        return seats;
    }

    public float calculatePrice() {

        if(room != null) {
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
            for (SeasonalDiscount seasonalDiscount : Hotel.getInstance().seasonalDiscounts) {
                if (seasonalDiscount.collides(this))
                    collidingDiscounts.add(seasonalDiscount);
            }


            while (comparator.compare(from, to) <= 0) {

                float dayPrice = basePerDay * seats;
                int sdValue = 0;
                for (SeasonalDiscount seasonalDiscount : collidingDiscounts) {
                    if (seasonalDiscount.contains(from.getTime())) {
                        if (seasonalDiscount.getPercentage() > sdValue)
                            sdValue = seasonalDiscount.getPercentage();
                    }

                }

                price += dayPrice * (float) (100 - sdValue) / 100.0f;
                from.add(Calendar.DATE, 1);
            }


            // Discount for regular customers
            if (person != null) {
                price -= (float) person.getDiscount() / 100.0f * price;
            }

            // EarlyBook discount
            Calendar creation = Calendar.getInstance();
            creation.setTime(creationDate);
            Calendar discountTo = Calendar.getInstance();

            int ebDiscountValue = 0;
            for (EarlyBookingDiscount earlyBookingDiscount : Hotel.getInstance().earlyBookingDiscounts) {

                discountTo.setTime(originalBeginDate);
                discountTo.add(Calendar.MONTH, -earlyBookingDiscount.getMonths());
                if (comparator.compare(creation, discountTo) <= 0) {

                    if (earlyBookingDiscount.getPercentage() > ebDiscountValue)
                        ebDiscountValue = earlyBookingDiscount.getPercentage();
                }
            }

            price -= (float) ebDiscountValue / 100.0f * price;

            return price;

        } else {

            return alreadyPaid;
        }
    }

    public void pay(float amount) {
        alreadyPaid += amount;
        if(person != null)
            person.updateDiscount();

    }

    public float leftToPay() {
        return calculatePrice() - alreadyPaid;
    }

    public String toString() {
        String repr = "Id: "+ id;
        if(room != null) {
            repr += " | Room number: " + room.getNumber();
        } else {
            repr += " | Room number: None";
        }
        repr += " | Creation date: " + CommandReader.dateFormat.format(creationDate);
        repr += " | From: " + CommandReader.dateFormat.format(begin);
        repr += " | To: " + CommandReader.dateFormat.format(end);
        repr += " | Seats: " + seats;
        if(person != null) {
            repr += " | Owner: " + person.getId() + ", " + person.getFirstname() + " " + person.getSurname();
        } else {
            repr += " | Owner: " + "None";
        }
        repr += " | Paid: " + alreadyPaid +"/"+ calculatePrice();

        return repr;
    }


}