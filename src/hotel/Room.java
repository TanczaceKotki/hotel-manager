package hotel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room {

    public enum RoomStandard {HIGH, LOW}

    private int id;
    private int number;
    private int seats;
    private float basePricePerDay;
    RoomStandard standard;

    public Room(int number, int seats, float basePricePerDay, RoomStandard s) {

        this.number = number;
        this.seats = seats;
        this.basePricePerDay = basePricePerDay;
        standard = s;


    }

    public boolean isAvailable(Interval interval) {
        for(Reservation reservation: Hotel.getInstance().reservations) {
            if(reservation.collides(interval) && reservation.getRoomId() == this.id) {
                return false;
            }
        }
        return true;
    }

    //return true - powodzenie
    public Reservation addReservation(Date b, Date e, Person newPerson) {
        Reservation reservation =  new Reservation(b, e, this, newPerson);
        if(isAvailable(reservation)) {
            Hotel.getInstance().reservations.add(reservation);
            return reservation;
        } else {
            return null;
        }
    }

    public boolean cancelReservation(Reservation reservation) {
        //Mo¿na usuwaæ tylko rezerwacje dotycz¹ce tego pokoju
        if(reservation.getRoomId() == this.id) {
            Hotel.getInstance().reservations.remove(reservation);
            return true;
        } else {
            return false;
        }
    }

    public Reservation getCurrentReservation() {
        Date today = new Date();
        for(Reservation reservation: Hotel.getInstance().reservations) {
            if(reservation.getRoomId() == id && reservation.contains(today)) {
                return reservation;
            }
        }
        return null;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setBasePricePerDay(float basePricePerDay) {
        this.basePricePerDay = basePricePerDay;
    }

    public RoomStandard getStandard() {
        return standard;
    }

    public void setStandard(RoomStandard standard) {
        this.standard = standard;
    }

    public int getId() {
        return id;
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
}