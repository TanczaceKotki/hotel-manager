package hotel;

public class Person {
    private int id;
    private String firstname;
    private String surname;
    private String telephone;
    private String email;

    public Person(int id, String firstname,
                  String surname) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.telephone = "";
        this.email = "";
    }

    public Person(int id, String firstname,
                  String surname, String telephone) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.telephone = telephone;
        this.email = "";
    }

    public Person(int id, String firstname,
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
