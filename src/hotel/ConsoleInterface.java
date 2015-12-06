package hotel;
import hotel.exceptions.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


class CommandReader {
    Scanner scanInput;
    static DateFormat dateFormat;

    public CommandReader() {
        scanInput = new Scanner(System.in);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    }

    public String readString() throws NoValueException {
        String input = scanInput.nextLine().trim();
        if(input.equals("")) {
            throw new NoValueException();
        } else {
            return input;
        }
    }

    public int readInt() throws IntExpectedException, NoValueException {
        String input = scanInput.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch(Exception e) {
            if(input.equals("")) {
                throw new NoValueException();
            } else {
                throw new IntExpectedException();
            }
        }
    }

    public float readFloat() throws FloatExpectedException, NoValueException {
        String input = scanInput.nextLine().trim();
        try {
            return Float.parseFloat(input);
        } catch(Exception e) {
            if(input.equals("")) {
                throw new NoValueException();
            } else {
                throw new FloatExpectedException();
            }
        }

    }

    public Date readDate() throws DateFormatException, NoValueException {
        String input = scanInput.nextLine().trim();
        try {
            return dateFormat.parse(input);
        } catch (ParseException e) {
            if (input.equals("")) {
                throw new NoValueException();
            } else {
                throw new DateFormatException();
            }
        }

    }

    public boolean readBoolean() throws UndefinedBooleanException, NoValueException {
        String input = scanInput.nextLine().trim();
        if(input.toUpperCase().equals("YES")) {
            return true;
        } else if(input.toUpperCase().equals("NO")) {
            return false;
        } else {
            if (input.equals("")) {
                throw new NoValueException();
            } else {
                throw new UndefinedBooleanException();
            }
        }
    }

    public void dispose() {
        scanInput.close();
    }
}


public class ConsoleInterface {

    HashMap<String, Command> commands;
    CommandReader commandReader;


    public ConsoleInterface() {

        commandReader = new CommandReader();
        commands = new HashMap<String, Command>();
        commands.put("add room", new AddRoom(commandReader));
        commands.put("find rooms", new SearchRooms(commandReader));
        commands.put("update room", new UpdateRoom(commandReader));
        commands.put("remove room", new RemoveRoom(commandReader));
        commands.put("change room number", new ChangeRoomNumber(commandReader));

        commands.put("residents count", new GetResidentsCount(commandReader));

        commands.put("add customer", new AddClient(commandReader));
        commands.put("remove customer", new RemoveClient(commandReader));
        commands.put("update customer", new UpdateClient(commandReader));
        commands.put("find customers", new SearchClients(commandReader));

        commands.put("add seasonal discount", new AddSeasonalDiscount(commandReader));
        commands.put("list seasonal discounts", new ListSeasonalDiscounts(commandReader));
        commands.put("remove seasonal discount", new RemoveSeasonalDiscount(commandReader));

        commands.put("add early book discount", new AddEarlyBookDiscount(commandReader));
        commands.put("list early book discounts", new ListEarlyBookDiscounts(commandReader));
        commands.put("remove early book discount", new RemoveEarlyBookDiscount(commandReader));

        commands.put("add reservation", new AddReservation(commandReader));
        commands.put("find reservations", new SearchReservation(commandReader));
        commands.put("cancel reservation", new CancelReservation(commandReader));
        commands.put("pay reservation", new PayReservation(commandReader));
        commands.put("move reservation", new MoveReservation(commandReader));
        commands.put("update reservation", new UpdateReservation(commandReader));

        commands.put("save data", new SaveData(commandReader));
        commands.put("load data", new LoadData(commandReader));
        commands.put("help", new PrintHelp(commandReader));



    }


    public void start() {

        Command command = null;

        System.out.println("Welcome to hotel manager.\ntype \"help\" command to display all possible commands with their descriptions.");

        while(true) {
            System.out.print("\n-->");
            String input;

            try {
                input = commandReader.readString();
            } catch(NoValueException e) {
                continue;
            }

            try {
                Command cmdObject = commands.get(input.toLowerCase());
                cmdObject.execute();

            } catch (NullPointerException e) {
                if(input.equals("exit")) {
                    commandReader.dispose();
                    break;
                } else {
                    System.out.println("Unknown command: " + input);
                }
            }

        }


    }

}


