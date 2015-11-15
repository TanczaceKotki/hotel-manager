package hotel;

public class Person {
    private int id;
    private String firstname;
    private String surname;
    private String telephone;
    private String email;
    private int discount;


    public Person(int id, String firstname,
                  String surname) {
        setValues(id, firstname, surname, "", "");
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

    public void setTel(String tel) {
        this.telephone = tel;
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

    public void setDiscount(int newDiscount) {
        discount = newDiscount;
    }

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
