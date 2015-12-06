package hotel.exceptions;

public class NoValueException extends Exception {

    public NoValueException() {
        super("Error: Mandatory value not specified");
    }


}
