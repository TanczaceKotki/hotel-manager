package hotel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Person {

    private int id;
    private String firstname;
    private String surname;
    private String telephone;
    private String email;
    private int discount;

    static int generateId() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public Person(int id, String firstname,
                  String surname) {
        setValues(id, firstname, surname, "", "");
    }

    void updateDiscount() {
        if(discount != 0) {
            discount = Hotel.getInstance().getCustomerDiscount();
        } else {
            Hotel hotel = Hotel.getInstance();
            ArrayList<Reservation> reservations = hotel.findReservations(-1, 0, null, id, null);
            int days = 0;
            for(Reservation r : reservations) {
                if(r.alreadyPaid > 0.0f) {
                    long diff = (r.end.getTime() - r.begin.getTime());
                    days += TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                }
            }
            if(days >= hotel.getCustomerDiscountThreshold()) {
                discount = hotel.getCustomerDiscount();
            }

        }

    }


    public Person(int id, String firstname,
                  String surname, String telephone) {
        setValues(id, firstname, surname, telephone, "");
    }

    public Person(int id, String firstname,
                  String surname, String telephone, String email) {
        setValues(id, firstname, surname, telephone, email);
    }

    private void setValues(int id, String firstname,
                           String surname, String telephone, String email) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;

    }

    public String toString() {
        String repr = "ID: "+ id;
        repr += "\nFirst name: " + firstname;
        repr += "\nLast name: " + surname;
        repr += "\nPhone number: " + telephone;
        repr += "\nE-mail: " + email;
        return repr;
    }

    public void setFirstname(String name) {
        firstname = name;
    }

    public void setSurname(String sName) {
        surname = sName;
    }

    public void setTel(String tel) {
        this.telephone = tel;
        tel.replaceAll("\\s+","");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int newDiscount) {discount = newDiscount;}

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }
}
