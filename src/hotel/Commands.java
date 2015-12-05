package hotel;

import hotel.exceptions.DateFormatException;
import hotel.exceptions.RoomAlreadyExistsException;
import hotel.exceptions.UndefinedBooleanException;

import java.util.ArrayList;
import java.util.Date;

abstract class Command {
    CommandReader reader;
    public Command(CommandReader commandReader) {
        reader = commandReader;
    }

    public abstract void execute();

}

abstract class RoomCommand extends Command {

    public RoomCommand(CommandReader commandReader) {
        super(commandReader);
    }

    public int queryNumber() {
        System.out.println("Room number:");
        return reader.readInt();
    }

    public int querySeats() {
        System.out.println("Seats:");
        return reader.readInt();
    }

    public float queryPrice(String customInfo) {
        if(customInfo != null) {
            System.out.println(customInfo);
        } else {
            System.out.println("Price per day:");
        }

        return reader.readFloat();
    }

    public Date queryDate(String customInfo) throws DateFormatException {
        System.out.println(customInfo);
        return reader.readDate();
    }

    public Room.RoomStandard queryStandard() {
        String options = "";
        for (Room.RoomStandard option : Room.RoomStandard.values()) {
            options += ", "+option;
        }
        options = options.substring(2);
        System.out.println("Standard: (possible options: "+options+")");

        return Room.RoomStandard.valueOf(reader.readString().toUpperCase());

    }

    public boolean confirm(String message) throws UndefinedBooleanException {
        System.out.println(message);
        return reader.readBoolean();
    }

}


class AddRoom extends RoomCommand {

    public AddRoom(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        try {

            int number = queryNumber();
            int seats = querySeats();
            float price = queryPrice(null);
            Room.RoomStandard standard = queryStandard();

            //Dodawanie
            if(confirm("Are you sure that you want to add specified room to the registery? (yes/no)")) {
                Hotel hotel = Hotel.getInstance();
                Room room = new Room(number, seats, price, standard);
                hotel.addRoom(room);
                System.out.println("Room was succesfully added to the registery");
            }

        } catch (NumberFormatException | RoomAlreadyExistsException | UndefinedBooleanException e) {
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

            int minSeats = querySeats();
            float maxPrice = queryPrice("Maximum price per day:");
            Room.RoomStandard standard = queryStandard();
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

        } catch (NumberFormatException | DateFormatException e) {
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

        } catch (NumberFormatException | UndefinedBooleanException e) {
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
                int seats = querySeats();
                float price = queryPrice(null);
                Room.RoomStandard standard = queryStandard();

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

        } catch (NumberFormatException | UndefinedBooleanException e) {
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

        } catch (NumberFormatException | RoomAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

    }

}


class GetResidetsCount extends Command {

    public GetResidetsCount(CommandReader commandReader) {
        super(commandReader);
    }

    @Override
    public void execute() {
        Hotel hotel = Hotel.getInstance();
        System.out.println("Current residents: " + hotel.numOfPeople());

    }

}