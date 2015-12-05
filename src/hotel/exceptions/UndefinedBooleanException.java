package hotel.exceptions;

public class UndefinedBooleanException extends Exception {

    public UndefinedBooleanException() {
        super("Error: \"yes\" or \"no\" value expected");
    }


}