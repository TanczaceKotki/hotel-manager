package hotel;

import hotel.exceptions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

abstract class Command {
    CommandReader reader;
    public Command(CommandReader commandReader) {
        reader = commandReader;
    }

    public abstract void execute();
    public String getDescription() {return "description...";}

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
        System.out.println(message + " (yes/no)");
        try {
            return reader.readBoolean();
        } catch (NoValueException e) {
            return false;
        }
    }
}

//Rooms ------------------------------------------------------------------------------------------

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
            if(confirm("Are you sure that you want to add specified room to the registery?")) {

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
                if(confirm("Are you sure that you want to remove selcted room from the registery?")) {
                    hotel.removeRoom(room);
                    System.out.println("Room removed from the registery");
                }

            } else {
                throw new NotFoundException("No room with specified number found");
            }

        } catch (NumberFormatException | UndefinedBooleanException | NoValueException | NotFoundException e) {
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
                throw new NotFoundException("No room with specified number found");

            }

        } catch (NumberFormatException | UndefinedBooleanException | NoValueException | NoSuchOptionException | NotFoundException e) {
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
                throw new NotFoundException("No room with specified number found");
            }

        } catch (NumberFormatException | RoomAlreadyExistsException | NoValueException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}

//Customers ------------------------------------------------------------------------------------------

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

            if(confirm("Are you sure that you want to add new person to the registery?")) {
                Hotel hotel = Hotel.getInstance();
                int newId = Person.generateId();
                Person person = new Person(newId, firstname, surname, phoneNumber, email);
                hotel.addClient(person);
                System.out.println("Customer "+person.getId()+" added succesfully");
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
                throw new NotFoundException("No customer with specified id found");
            }


        } catch (NumberFormatException | NoValueException | UndefinedBooleanException | NotFoundException e) {
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
                if (confirm("Are you sure that you want to remove this customer from the registery?")) {
                    hotel.removeClient(customer);
                    System.out.println("Customer removed from the registery");
                }
            } else {
                throw new NotFoundException("No customer with specified id found");

            }

        } catch (NumberFormatException | NoValueException | UndefinedBooleanException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}


//Discounts ------------------------------------------------------------------------------------------

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
                System.out.println("\nIndex of discount to delete:");
                int index = reader.readInt();
                if(index >= 0 && index < info.length && confirm("Are you sure that you want to remove selected discount?")) {
                    hotel.removeSeasonalDiscount(index);
                    System.out.println("Discount removed from the list");
                } else {
                    throw new NotFoundException("No discount with specified index found");
                }
            } else {
                System.out.println("No discounts");
            }

        } catch (NumberFormatException | NoValueException | UndefinedBooleanException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}


class ListSeasonalDiscounts extends CommonCommand {

    public ListSeasonalDiscounts(CommandReader commandReader) {
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

            } else {
                System.out.println("No discounts");
            }





        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

    }

}




class AddEarlyBookDiscount extends CommonCommand {

    public AddEarlyBookDiscount(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {
            System.out.println("Percentage:");
            int percentage = reader.readInt();
            System.out.println("Months:");
            int months = reader.readInt();

            if(confirm("Do you want to add specified discount to the discount list?")) {
                Hotel hotel = Hotel.getInstance();
                hotel.addEarlyBookDiscount(new EarlyBookingDiscount(months, percentage));
                System.out.println("Discount added to the list");
            }

        } catch (NumberFormatException | NoValueException | UndefinedBooleanException e) {
            System.out.println(e.getMessage());
        }

    }

}


class RemoveEarlyBookDiscount extends CommonCommand {

    public RemoveEarlyBookDiscount(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {
            Hotel hotel = Hotel.getInstance();
            String[] info = hotel.getEarlyBookDiscountInfo();
            System.out.println("\nEarly book discounts list:");

            if(info.length > 0) {
                System.out.println("\nIndex of discount to delete:");
                int index = reader.readInt();
                if(index >= 0 && index < info.length && confirm("Are you sure that you want to remove selected discount?")) {
                    hotel.removeEarlyBookDiscount(index);
                    System.out.println("Discount removed from the list");
                } else {
                    throw new NotFoundException("no discount with specified index found");
                }
            } else {
                System.out.println("No discounts");
            }

        } catch (NumberFormatException | NoValueException | UndefinedBooleanException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}

class ListEarlyBookDiscounts extends CommonCommand {

    public ListEarlyBookDiscounts(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {
            Hotel hotel = Hotel.getInstance();
            String[] info = hotel.getEarlyBookDiscountInfo();
            System.out.println("\nEarly book discounts list:");

            if(info.length > 0) {
                for (int i=0; i<info.length; i++) {
                    System.out.println("---------");
                    System.out.println(info[i]);
                }
                System.out.print("---------");

            } else {
                System.out.println("No discounts");
            }





        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

    }

}

//Reservations ------------------------------------------------------------------------------------------

abstract class ReservationCommand extends CommonCommand {

    public ReservationCommand(CommandReader commandReader) {
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

    public int queryId() throws IntExpectedException, NoValueException {
        System.out.println("Reservation Id:");
        return reader.readInt();
    }

    public int queryPerson() throws IntExpectedException, NoValueException {
        System.out.println("Reservation owner (customer) Id:");
        return reader.readInt();
    }

}


class AddReservation extends ReservationCommand {

    public AddReservation(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {
            Hotel hotel = Hotel.getInstance();
            Person person;
            int roomId = queryNumber();
            Room room = hotel.getRoomByNumber(roomId);
            if(room != null) {
                int seats = querySeats();
                if(seats <= room.getSeats()) {

                    try {
                        person = hotel.getClientById( queryPerson());
                        if(!(person != null))
                            throw new NotFoundException("No customer with specified id found");

                    } catch (NoValueException e) {
                        person = null;
                    }

                    Date begin = queryDate("Reservation from:");
                    Date end = queryDate("Reservation to:");

                    if (confirm("Are you sore that you want ro add this reservation to the registery?")) {
                        Reservation reservation = room.addReservation(begin, end, person, seats);
                        if(reservation != null) {
                            System.out.println("Reservation: "+reservation.getId()+" made succesfully");
                        } else {
                            System.out.println("Specified reservation cannot be created. Probably reservation with colliding interval already exists in the system");
                        }
                        }

                } else {
                    System.out.println("Room does not have enough seats for this reservation");
                }
            } else {
                throw new NotFoundException("No room with specified id found");
            }


        } catch (NumberFormatException | NoValueException | DateFormatException | UndefinedBooleanException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}


class CancelReservation extends ReservationCommand {

    public CancelReservation(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {
            Hotel hotel = Hotel.getInstance();
            int id = queryId();
            Reservation reservation = hotel.getReservationById(id);
            if(reservation != null) {
                if (confirm("Are you sore that you want ro remove this reservation from the registery?")) {
                    hotel.removeReservation(reservation);
                    System.out.println("Reservation removed from the registery");
                }
            } else {
                throw new NotFoundException("No reservation with specified id found");
            }


        } catch (NumberFormatException | NoValueException | UndefinedBooleanException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }


}



class SearchReservation extends ReservationCommand {

    public SearchReservation(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {
            Hotel hotel = Hotel.getInstance();

            int roomId;
            try {
                roomId = queryNumber();
            } catch (NoValueException e) {
                roomId = -1;
            }

            int personId;
            try {
                personId = queryPerson();
            } catch (NoValueException e) {
                personId = 0;
            }

            int seats;
            try {
                seats = querySeats();
            } catch(NoValueException e) {
                seats = 0;
            }

            Date creationDateFrom, creationDateTo;
            try {
                creationDateFrom = queryDate("Creation date from: ");
                creationDateTo = queryDate("Creation date to: ");

            } catch (NoValueException e) {
                creationDateFrom = null;
                creationDateTo = null;
            }


            Date begin, end;
            try {
                begin = queryDate("Begin date: ");
                end = queryDate("End date:");

            } catch (NoValueException e) {
                begin = null;
                end = null;
            }

            Interval cdInterval = null;
            if(creationDateFrom != null)
                cdInterval = new Interval(creationDateFrom, creationDateTo);

            Interval collisionInterval = null;
            if(begin != null)
                collisionInterval = new Interval(begin, end);



            //Wyszukiwanie
            System.out.println("\nSearch results:");
            ArrayList<Reservation> reservations = hotel.findReservations(roomId, seats, cdInterval, personId, collisionInterval);

            if(reservations.size() > 0) {
                for (Reservation r : reservations) {
                    System.out.println("---------");
                    System.out.println(r.toString());
                }
                System.out.print("---------");
            } else {
                System.out.println("No results found");
            }


        } catch (NumberFormatException | DateFormatException e) {
            System.out.println(e.getMessage());
        }

    }


}


class PayReservation extends ReservationCommand {

    public PayReservation(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {

        try {
            Hotel hotel = Hotel.getInstance();
            int id = queryId();
            Reservation reservation = hotel.getReservationById(id);

            if(reservation != null) {
                System.out.println("Amount:");
                float amount = reader.readFloat();
                if (confirm("Payment confirmation")) {
                    reservation.pay(amount);
                }
            } else {
                throw new NotFoundException("No reservation with specified id found");
            }




        } catch (NumberFormatException | NoValueException | UndefinedBooleanException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }


}


class MoveReservation extends ReservationCommand {

    public MoveReservation(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        try {
            Hotel hotel = Hotel.getInstance();
            int id = queryId();
            Reservation reservation = hotel.getReservationById(id);

            if(reservation != null) {
                Date newBegin;
                try {
                    newBegin = queryDate("Reservation from: ");
                } catch (NoValueException e) {
                    newBegin = reservation.begin;
                }

                Date newEnd;
                try {
                    newEnd = queryDate("Reservation to: ");
                } catch (NoValueException e) {
                    newEnd = reservation.end;
                }

                if (confirm("Do you want to apply changes?")) {
                    if (reservation.changeDates(newBegin, newEnd)) {
                        System.out.println("Reservation moved succesfully");
                    } else {
                        System.out.println("Cannot move reservation due to collision");
                    }
                }

            } else {
                throw new NotFoundException("No reservation with specified id found");
            }



        } catch (NumberFormatException | NoValueException | DateFormatException | UndefinedBooleanException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}


class UpdateReservation extends ReservationCommand {

    public UpdateReservation(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        try {
            Hotel hotel = Hotel.getInstance();
            int id = queryId();
            Reservation reservation = hotel.getReservationById(id);

            if(reservation != null) {


                //Sczytuje room
                Room room = hotel.getRoomByNumber(reservation.getRoomId());
                Room newRoom = null;

                try {
                    System.out.println("New room number:");
                    int newRoomId = reader.readInt();
                    newRoom = hotel.getRoomByNumber(newRoomId);

                    if(newRoom == null) {
                        throw new NotFoundException("No room with specified id found");
                    }

                } catch (NoValueException e) {
                    newRoom = room;
                }


                int seats;
                try {
                    seats = querySeats();
                } catch (NoValueException e) {
                    seats = reservation.seats;
                }

                if(newRoom != null) {
                    if(room != newRoom) {
                        if (!newRoom.isAvailable(reservation))
                            throw new NotFoundException("Cannot change room due to collision with existing reservation");
                        if (newRoom.getSeats() < seats)
                            throw new NotFoundException("Room does not have enough seats for this reservation");
                    } else {
                        if (newRoom.getSeats() < seats && seats >= reservation.getSeats())
                            throw new NotFoundException("Room does not have enough seats for this reservation");
                    }
                }


                Person client;
                try {
                    int customerId = queryPerson();
                    client = hotel.getClientById(customerId);
                } catch (NoValueException e) {
                    client = null;
                }


                //Wprowadzanie zmian
                if (confirm("Do you want to apply changes?")) {

                    if (newRoom != null) {
                        reservation.room = newRoom;
                    }
                    if (client != null) {
                        reservation.person = client;
                    }
                    reservation.seats = seats;
                    System.out.println("Reservation data updated succesfully");
                }


            } else {
                throw new NotFoundException("No reservation with specified id found");
            }

        } catch (NumberFormatException | NoValueException | UndefinedBooleanException | NotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}

//Serialization, help ------------------------------------------------------------------------------------------

class SaveData extends Command {

    public SaveData(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        Hotel hotel = Hotel.getInstance();
        hotel.exportAllData();
        System.out.println("Data saved");

    }

}


class LoadData extends Command {

    public LoadData(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        Hotel hotel = Hotel.getInstance();
        hotel.importAllData();
        System.out.println("Data loaded");

    }

}

class PrintHelp extends Command {

    String help = "";

    public PrintHelp(CommandReader commandReader) {
        super(commandReader);
    }
    public PrintHelp(CommandReader commandReader, HashMap<String, Command> commands) {
        super(commandReader);
        for(String key : commands.keySet()) {
            help += key + " - "+commands.get(key).getDescription()+"\n\n";
        }
        help += "exit - exit application\n";
    }


    @Override
    public void execute() {
        System.out.println(help);
    }

}
