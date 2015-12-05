package hotel.exceptions;

import java.text.ParseException;

public class DateFormatException extends ParseException {

    public DateFormatException() {
        super("Error: cannot read date from specified text \nExpected format: DD/MM/YYYY", 0);

    }

}
