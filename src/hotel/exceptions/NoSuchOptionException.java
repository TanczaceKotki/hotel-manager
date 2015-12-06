package hotel.exceptions;

public class NoSuchOptionException extends Exception {

    public NoSuchOptionException() {
        super("Error: Selected option does not exist");
    }

}
