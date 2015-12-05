package hotel;
import hotel.exceptions.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


class CommandReader {
    Scanner scanInput;
    DateFormat dateFormat;

    public CommandReader() {
        scanInput = new Scanner(System.in);
        dateFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
    }

    public String readString() {
        return scanInput.nextLine().trim();
    }

    public int readInt() throws IntExpectedException {
        try {
            return Integer.parseInt(scanInput.nextLine());
        } catch(Exception e) {
            throw new IntExpectedException();
        }
    }

    public float readFloat() throws FloatExpectedException {
        try {
            return Float.parseFloat(scanInput.nextLine());
        } catch(Exception e) {
            throw new FloatExpectedException();
        }

    }

    public Date readDate() throws DateFormatException {
        try {
            return dateFormat.parse(scanInput.nextLine());
        } catch (ParseException e) {
            throw new DateFormatException();
        }
    }

    public boolean readBoolean() throws UndefinedBooleanException {
        String value = readString();
        if(value.toUpperCase().equals("YES")) {
            return true;
        } else if(value.toUpperCase().equals("NO")) {
            return false;
        } else {
            throw new UndefinedBooleanException();
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
        commands.put("find room", new SearchRooms(commandReader));
        commands.put("update room", new UpdateRoom(commandReader));
        commands.put("remove room", new RemoveRoom(commandReader));
        commands.put("change room number", new ChangeRoomNumber(commandReader));


    }


    public void start() {

        Command command = null;

        while(true) {
            System.out.print("\n-->");
            String input = commandReader.readString();

            try {
                Command cmdObject = commands.get(input);
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
