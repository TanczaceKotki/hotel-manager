package hotel;

import hotel.exceptions.RoomAlreadyExistsException;

import java.util.Date;

public class Room {

    public enum RoomStandard {HIGH, LOW}

    private int number; //{get}
    private int seats; //{get, set}
    private float basePricePerDay; //{get, set}
    RoomStandard standard; //{get, set}

    public Room(int number, int seats, float basePricePerDay, RoomStandard s) {

        this.number = number;
        this.seats = seats;
        this.basePricePerDay = basePricePerDay;
        standard = s;

    }

    public boolean isAvailable(Interval interval) {
        for(Reservation reservation: Hotel.getInstance().reservations) {
            if(reservation.getRoomId() == this.number & reservation.collides(interval)) {
                return false;
            }
        }
        return true;
    }

    public void changeNumber(int newNumber) throws RoomAlreadyExistsException {
        Hotel hotel = Hotel.getInstance();
        if(hotel.getRoomByNumber(newNumber) != null) {
            throw new RoomAlreadyExistsException();
        } else {
            number = newNumber;
        }
    }

    //return true - powodzenie
    public Reservation addReservation(Date b, Date e, Person person, int seats) {
        Reservation reservation =  new Reservation(0, b, e, this, person, seats);
        if(isAvailable(reservation)) {
            Hotel.getInstance().reservations.add(reservation);
            return reservation;
        } else {
            return null;
        }
    }

    public String toString() {
        String repr = "";
        if(number >= 0) {
            repr += "Room number: " + number + "\n";
        } else {
            repr += "Room number: None\n";
        }
        repr += "Seats: "+seats+"\n";
        repr += "Standard: "+standard.toString()+"\n";
        repr += "Price per day: "+basePricePerDay;

        return repr;
    }

    public boolean cancelReservation(Reservation reservation) {
        //Mo�na usuwa� tylko rezerwacje dotycz�ce tego pokoju
        if(reservation.getRoomId() == this.number) {
            Hotel.getInstance().reservations.remove(reservation);
            return true;
        } else {
            return false;
        }
    }

    public Reservation getCurrentReservation() {
        Date today = new Date();
        for(Reservation reservation: Hotel.getInstance().reservations) {
            if(reservation.getRoomId() == number && reservation.contains(today)) {
                return reservation;
            }
        }
        return null;
    }

    void setSeats(int seats) {
        this.seats = seats;
    }

    void setBasePricePerDay(float basePricePerDay) {
        this.basePricePerDay = basePricePerDay;
    }

    void setStandard(RoomStandard standard) {
        this.standard = standard;
    }

    public int getNumber() {
        return number;
    }

    public int getSeats() {
        return seats;
    }

    public float getBasePricePerDay() {
        return basePricePerDay;
    }

    public RoomStandard getStandard() {
        return standard;
    }
}