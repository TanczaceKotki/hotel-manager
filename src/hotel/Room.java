package hotel;

import java.util.ArrayList;
import java.util.List;

class Room {

    public enum RoomStandard {HIGH, LOW};

    int number;
    int maxLocators;
    float basePricePerDay;
    RoomStandard standard;
    ArrayList<Reservation> reservations;

    public Room(int _number, int _maxLocators, float _basePricePerDay, RoomStandard _standard) {

        number = _number;
        maxLocators = _maxLocators;
        basePricePerDay = _basePricePerDay;
        standard = _standard;

        reservations = new ArrayList<Reservation>();

    }

    public boolean checkReservation(Interval interval) {
        for(Interval reservation: reservations) {
            if(reservation.collides(interval))
                return false;
        }
        return true;
    }

    //Mo¿e wyj¹tek? mo¿liwe kilka przyczyn niepowodzenia
    //return true - powodzenie
    public boolean addReservation(Reservation reservation) {
        if(checkReservation(reservation)) {
            if(reservation.locatorsCount <= this.maxLocators) {
                reservations.add(reservation);
                return true;
            } else {
                return false;
            }
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

}