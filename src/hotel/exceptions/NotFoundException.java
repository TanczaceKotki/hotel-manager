package hotel.exceptions;

public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super("Error: "+message);
    }


}
