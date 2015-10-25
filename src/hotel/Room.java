package hotel;

import java.util.ArrayList;
import java.util.List;

public class Room {

    public enum RoomStandard {HIGH, LOW}

    int id;
    int number;
    int seats;
    float basePricePerDay;
    RoomStandard standard;
    ArrayList<Reservation> reservations;

    public Room(int number, int seats, float basePricePerDay, RoomStandard s) {

        this.number = number;
        this.seats = seats;
        this.basePricePerDay = basePricePerDay;
        standard = s;

        reservations = new ArrayList<Reservation>();

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

    //Powinna sprawdza� sprawdzi� kolizj� z dniem dzisiejszym i zwr�ci� rezerwacj�
    //Implementacja tymczasowa - zwraca pierwsz� z brzegu
    public Reservation getCurrentReservation() {
        if(reservations.size() > 0) {
            return reservations.get(0);
        } else {
            return null;
        }
    }

}