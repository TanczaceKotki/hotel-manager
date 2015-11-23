package CSV;

import hotel.Reservation;
import hotel.Room;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class CSVImport {
    public ArrayList<Room> roomList (File file) throws IOException {
        ArrayList <Room> roomList = new ArrayList<>();
        CSVParser parser = getParser(file);
        for(CSVRecord record : parser){
            Room room = new Room(
                    Integer.parseInt(record.get("number")),
                    Integer.parseInt(record.get("seats")),
                    Float.parseFloat(record.get("base_price")),
                    Room.RoomStandard.valueOf(record.get("standard"))
            );
            roomList.add(room);
        }
        //close the parser
        parser.close();
        return roomList;
    }

    public ArrayList<Reservation> reservationList (File file) throws IOException {
        ArrayList <Reservation> rList = new ArrayList<>();
        CSVParser parser = getParser(file);
        for(CSVRecord record : parser){
            //0zmieni� nulle na obiekty klasy Room i klasy Person
            Reservation r = new Reservation(
                    new Date(record.get("begin")),
                    new Date(record.get("end")),
                    null,
                    null);
            rList.add(r);
        }
        //close the parser
        parser.close();
        return rList;
    }

    private CSVParser getParser(File file) throws IOException {
        //create the CSVFormat object
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        //initialize the CSVParser object
        return new CSVParser(new FileReader(file), format);
    }
}