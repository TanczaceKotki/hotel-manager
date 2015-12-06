package hotel;

import hotel.exceptions.*;

import java.util.ArrayList;
import java.util.Date;

abstract class Command {
    CommandReader reader;
    public Command(CommandReader commandReader) {
        reader = commandReader;
    }

    public abstract void execute();

}


abstract class CommonCommand extends Command {

    public CommonCommand(CommandReader commandReader) {
        super(commandReader);
    }

    public Date queryDate(String customInfo) throws DateFormatException, NoValueException {
        System.out.println(customInfo);
        return reader.readDate();
    }

    public boolean confirm(String message) throws UndefinedBooleanException, NoValueException {
        System.out.println(message);
        try {
            return reader.readBoolean();
        } catch (NoValueException e) {
            return false;
        }
    }
}

abstract class RoomCommand extends CommonCommand {

    public RoomCommand(CommandReader commandReader) {
        super(commandReader);
    }

    public int queryNumber() throws IntExpectedException, NoValueException {
        System.out.println("Room number:");
        return reader.readInt();
    }

    public int querySeats() throws IntExpectedException, NoValueException {
        System.out.println("Seats:");
        return reader.readInt();
    }

    public float queryPrice(String customInfo) throws FloatExpectedException, NoValueException {
        if(customInfo != null) {
            System.out.println(customInfo);
        } else {
            System.out.println("Price per day:");
        }

        return reader.readFloat();
    }

    public Room.RoomStandard queryStandard() throws NoSuchOptionException, NoValueException {
        String options = "";
        for (Room.RoomStandard option : Room.RoomStandard.values()) {
            options += ", "+option;
        }
        options = options.substring(2);
        System.out.println("Standard: (possible options: "+options+")");

        try {
            return Room.RoomStandard.valueOf(reader.readString().toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new NoSuchOptionException();
        }
    }

}


class AddRoom extends RoomCommand {

    public AddRoom(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        try {

            Hotel hotel = Hotel.getInstance();

            int number = queryNumber();
            Room room = hotel.getRoomByNumber(number);
            if(room != null)
                throw new RoomAlreadyExistsException();

            int seats = querySeats();
            float price = queryPrice(null);
            Room.RoomStandard standard = queryStandard();

            //Dodawanie
            if(confirm("Are you sure that you want to add specified room to the registery? (yes/no)")) {

                room = new Room(number, seats, price, standard);
                hotel.addRoom(room);
                System.out.println("Room was succesfully added to the registery");
            }

        } catch (NumberFormatException | RoomAlreadyExistsException | UndefinedBooleanException | NoValueException | NoSuchOptionException e) {
            System.out.println(e.getMessage());
        }

    }

}



class SearchRooms extends RoomCommand {

    public SearchRooms(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        try {

            int minSeats;
            try {
                minSeats = querySeats();
            } catch(NoValueException e) {
                minSeats = 0;
            }

            float maxPrice;
            try {
                maxPrice = queryPrice("Maximum price per day:");
            } catch(NoValueException e) {
                maxPrice = Float.MAX_VALUE;
            }

            Room.RoomStandard standard;
            try {
                standard = queryStandard();
            } catch(NoValueException e) {
                standard = null;
            }

            Date availableFrom = queryDate("Available from:");
            Date availableTo = queryDate("Available to:");

            //Wyszukiwanie
            System.out.println("\nSearch results:");
            Hotel hotel = Hotel.getInstance();
            ArrayList<Room> rooms = hotel.findRooms(new Interval(availableFrom, availableTo), minSeats, maxPrice, standard);
            if(rooms.size() > 0) {
                for (Room room : rooms) {
                    System.out.println("---------");
                    System.out.println(room.toString());
                }
                System.out.print("---------");
            } else {
                System.out.println("No results found");
            }

        } catch (NumberFormatException | DateFormatException | NoValueException | NoSuchOptionException e) {
            System.out.println(e.getMessage());
        }

    }

}



class RemoveRoom extends RoomCommand {

    public RemoveRoom(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        try {

            int number = queryNumber();
            Hotel hotel = Hotel.getInstance();
            Room room = hotel.getRoomByNumber(number);
            if(room != null) {
                if(confirm("Are you sure that you want to remove selcted room from the registery? (yes/no)")) {
                    hotel.removeRoom(room);
                }

            } else {
                System.out.println("No room with specified number found");
            }

        } catch (NumberFormatException | UndefinedBooleanException | NoValueException e) {
            System.out.println(e.getMessage());
        }

    }

}

class UpdateRoom extends RoomCommand {

    public UpdateRoom(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        try {

            int number = queryNumber();
            Hotel hotel = Hotel.getInstance();
            Room room = hotel.getRoomByNumber(number);

            if(room != null) {
                int seats;
                try {
                    seats = querySeats();
                } catch (NoValueException e) {
                    seats = room.getSeats();
                }

                float price;
                try {
                    price = queryPrice(null);
                } catch (NoValueException e) {
                    price = room.getBasePricePerDay();
                }

                Room.RoomStandard standard;
                try {
                    standard= queryStandard();
                } catch (NoValueException e) {
                    standard = room.getStandard();
                }
                //Update
                if(confirm("Do you want to apply changes?")) {
                    room.setSeats(seats);
                    room.setBasePricePerDay(price);
                    room.setStandard(standard);
                    System.out.println("Room data updated succesfully");
                }
            } else {
                System.out.println("No room with specified number found");

            }

        } catch (NumberFormatException | UndefinedBooleanException | NoValueException | NoSuchOptionException e) {
            System.out.println(e.getMessage());
        }

    }

}

class ChangeRoomNumber extends RoomCommand {

    public ChangeRoomNumber(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        try {

            int number = queryNumber();

            //Update
            Hotel hotel = Hotel.getInstance();
            Room room = hotel.getRoomByNumber(number);

            if(room != null) {
                System.out.println("New room number:");
                int newNumber = reader.readInt();
                room.changeNumber(newNumber);

            } else {
                System.out.println("No room with specified number found");
            }

        } catch (NumberFormatException | RoomAlreadyExistsException | NoValueException e) {
            System.out.println(e.getMessage());
        }

    }

}


class GetResidentsCount extends Command {

    public GetResidentsCount(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        Hotel hotel = Hotel.getInstance();
        System.out.println("Current residents: " + hotel.numOfPeople());

    }

}


// Customers

abstract class PersonCommand extends CommonCommand {

    public PersonCommand(CommandReader commandReader) {
        super(commandReader);
    }

    public String queryName() throws IntExpectedException, NoValueException {
        System.out.println("First name:");
        return reader.readString();
    }

    public String querySurname() throws IntExpectedException, NoValueException {
        System.out.println("Last name:");
        return reader.readString();
    }

    public String queryPhoneNumber() throws IntExpectedException, NoValueException {
        System.out.println("Phone number:");
        return reader.readString();
    }

    public String queryEmail() throws IntExpectedException, NoValueException {
        System.out.println("E-mail address:");
        return reader.readString();
    }

    public int queryId() throws IntExpectedException, NoValueException {
        System.out.println("Customer id:");
        return reader.readInt();
    }

}

class AddClient extends PersonCommand {

    public AddClient(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {

            String firstname = queryName();
            String surname = querySurname();
            String phoneNumber;
            try {
                phoneNumber = queryPhoneNumber();
            } catch(NoValueException e) {
                phoneNumber = "";
            }

            String email;
            try {
                email = queryEmail();
            } catch(NoValueException e) {
                email = "";
            }

            if(confirm("Are you sure that you want to add new person to the registery? (yes/no)")) {
                Hotel hotel = Hotel.getInstance();
                int newId = Person.generateId();
                Person person = new Person(newId, firstname, surname, phoneNumber, email);
                hotel.addClient(person);

            }

        } catch (NumberFormatException | NoValueException | UndefinedBooleanException e) {
            System.out.println(e.getMessage());
        }

    }

}

class SearchClients extends PersonCommand {

    public SearchClients(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        Hotel hotel = Hotel.getInstance();
        try {

            String firstname;
            try {
                firstname = queryName();
            } catch(NoValueException e) {
                firstname = "";
            }

            String surname;
            try {
                surname = querySurname();
            } catch(NoValueException e) {
                surname = "";
            }

            String phoneNumber;
            try {
                phoneNumber = queryPhoneNumber();
            } catch(NoValueException e) {
                phoneNumber = "";
            }

            String email;
            try {
                email = queryEmail();
            } catch(NoValueException e) {
                email = "";
            }

            //Wyszukiwanie
            System.out.println("\nSearch results:");
            ArrayList<Person> clients = hotel.findClients(firstname, surname, phoneNumber, email);
            if(clients.size() > 0) {
                for (Person client : clients) {
                    System.out.println("---------");
                    System.out.println(client.toString());
                }
                System.out.print("---------");
            } else {
                System.out.println("No results found");
            }



        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

    }

}


class UpdateClient extends PersonCommand {

    public UpdateClient(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        Hotel hotel = Hotel.getInstance();
        try {

            int id = queryId();
            Person client = hotel.getClientById(id);
            if(client != null) {

                String firstname;
                try {
                    firstname = queryName();
                } catch (NoValueException e) {
                    firstname = client.getFirstname();
                }

                String surname;
                try {
                    surname = querySurname();
                } catch (NoValueException e) {
                    surname = client.getSurname();
                }

                String phoneNumber;
                try {
                    phoneNumber = queryPhoneNumber();
                } catch (NoValueException e) {
                    phoneNumber = client.getTelephone();
                }

                String email;
                try {
                    email = queryEmail();
                } catch (NoValueException e) {
                    email = client.getEmail();
                }

                if(confirm("Do you want to apply changes?")) {
                    client.setFirstname(firstname);
                    client.setSurname(surname);
                    client.setTel(phoneNumber);
                    client.setEmail(email);
                    System.out.println("Customer data updated succesfully");
                }


            } else {
                System.out.println("No customer with specified id found");
            }


        } catch (NumberFormatException | NoValueException | UndefinedBooleanException e) {
            System.out.println(e.getMessage());
        }

    }

}


class RemoveClient extends PersonCommand {

    public RemoveClient(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        Hotel hotel = Hotel.getInstance();
        try {

            int id = queryId();
            Person customer = hotel.getClientById(id);

            if(customer != null) {
                if (confirm("Are you sure that you want to remove this customer from the registery? (yes/no)")) {
                    hotel.removeClient(customer);
                    System.out.println("Customer removed from the registery");
                }
            } else {
                System.out.println("No customer with specified id found");

            }

        } catch (NumberFormatException | NoValueException | UndefinedBooleanException e) {
            System.out.println(e.getMessage());
        }

    }

}



class AddSeasonalDiscount extends CommonCommand {

    public AddSeasonalDiscount(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {
            System.out.println("Percentage:");
            int percentage = reader.readInt();
            Date from = queryDate("From: ");
            Date to = queryDate("To: ");

            if(confirm("Do you want to add specified discount to the discount list?")) {
                Hotel hotel = Hotel.getInstance();
                hotel.addSeasonalDiscount(new SeasonalDiscount(from, to, percentage));
                System.out.println("Discount added to the list");
            }

        } catch (NumberFormatException | NoValueException | DateFormatException | UndefinedBooleanException e) {
            System.out.println(e.getMessage());
        }

    }

}

class RemoveSeasonalDiscount extends CommonCommand {

    public RemoveSeasonalDiscount(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {
            Hotel hotel = Hotel.getInstance();
            String[] info = hotel.getSeasonalDiscountInfo();
            System.out.println("\nSeasonal discounts list:");

            if(info.length > 0) {
                for (int i=0; i<info.length; i++) {
                    System.out.println("---------");
                    System.out.println(info[i]);
                }
                System.out.print("---------");

                System.out.println("\nId of discount to delete:");
                int index = reader.readInt();
                if(index >= 0 && index < info.length) {
                    hotel.removeSeasonalDiscount(index);
                    System.out.println("Discount removed from the list");
                } else {
                    System.out.println("No discount with specified id found");
                }
            } else {
                System.out.println("No discounts");
            }





        } catch (NumberFormatException | NoValueException e) {
            System.out.println(e.getMessage());
        }

    }

}