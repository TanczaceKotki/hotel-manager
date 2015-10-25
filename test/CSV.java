import hotel.Interval;
import hotel.Room;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSV {
    public static ArrayList<Room> getRoomList () throws IOException {
        ArrayList <Room> roomList = new ArrayList<Room>();
        //Create the CSVFormat object
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        //initialize the CSVParser object
        CSVParser parser = new CSVParser(new FileReader("rooms.csv"), format);
        for(CSVRecord record : parser){
            Room room = new Room(
                    Integer.parseInt(record.get("number")),
                    Integer.parseInt(record.get("seats")),
                    Float.parseFloat(record.get("base_price")),
                    Room.RoomStandard.valueOf(record.get("standard")));
        }
        //close the parser
        parser.close();

        return roomList;
    }
}
