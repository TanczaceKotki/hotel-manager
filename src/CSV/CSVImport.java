package CSV;

import hotel.Reservation;
import hotel.Room;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

class CSVImport {
    public ArrayList<Room> roomList () throws IOException {
        ArrayList <Room> roomList = new ArrayList<>();
        CSVParser parser = getParser("rooms.csv");
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

    public ArrayList<Reservation> reservationList () throws IOException {
        ArrayList <Reservation> rList = new ArrayList<>();
        CSVParser parser = getParser("archive.csv");
        for(CSVRecord record : parser){
            Reservation r = new Reservation(new Date(record.get("begin")), new Date(record.get("end"));
            rList.add(r);
        }
        //close the parser
        parser.close();
        return rList;
    }

    private CSVParser getParser(String filename) throws IOException {
        //Create the CSVFormat object
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        //initialize the CSVParser object
        return new CSVParser(new FileReader(filename), format);
    }
}
