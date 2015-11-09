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

    public Room(int number, int seats, float basePricePerDay, RoomStandard s) {

        this.number = number;
        this.seats = seats;
        this.basePricePerDay = basePricePerDay;
        standard = s;


    }


    public boolean isAvailable(Interval interval) {
        //
        return false;
    }

    //return true - powodzenie
    public boolean addReservation(Reservation reservation) {
        return false;
    }

    public void cancelReservation(Reservation reservation) {

    }

    //Powinna sprawdzac kolizje z dniem dzisiejszym i zwrocic rezerwacje lub null
    //Implementacja tymczasowa - zwraca pierwsza z brzegu
    public Reservation getCurrentReservation() {
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