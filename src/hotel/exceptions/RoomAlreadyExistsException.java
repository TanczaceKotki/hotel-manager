package hotel.exceptions;

public class RoomAlreadyExistsException extends Exception {

    public String message;
    public RoomAlreadyExistsException() {
        this.message = "Error: Room with specified number already exists";
    }

    @Override
    public String getMessage(){
        return message;
    }

}
