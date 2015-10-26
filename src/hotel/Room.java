package hotel;

import java.util.ArrayList;
import java.util.List;

public class Room {

    public enum RoomStandard {HIGH, LOW}

    private int id;
    private int number;
    private int seats;
    private float basePricePerDay;
    RoomStandard standard;
    ArrayList<Reservation> reservations;

    public Room(int number, int seats, float basePricePerDay, RoomStandard s) {

        this.number = number;
        this.seats = seats;
        this.basePricePerDay = basePricePerDay;
        standard = s;

        reservations = new ArrayList<>();

    }


    public boolean isAvailable(Interval interval) {
        for (Interval reservation: reservations) {
            if(!reservation.collides(interval))
               return true;
        }
        return false;
    }

    //return true - powodzenie
    public boolean addReservation(Reservation reservation) {
        if(isAvailable(reservation) && reservation.seats <= seats) {
            reservations.add(reservation);
            return true;
        } else {
            return false;
        }
    }

    public void cancelReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

    //Powinna sprawdzaæ sprawdziæ kolizjê z dniem dzisiejszym i zwróciæ rezerwacjê
    //Implementacja tymczasowa - zwraca pierwsz¹ z brzegu
    public Reservation getCurrentReservation() {
        if(reservations.size() > 0) {
            return reservations.get(0);
        } else {
            return null;
        }
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