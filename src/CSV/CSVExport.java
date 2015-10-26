package CSV;

import hotel.Reservation;
import hotel.Room;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class CSVExport {
    public void roomList(ArrayList<Room> rList) throws IOException {
        CSVPrinter printer = new CSVPrinter(new FileWriter("rooms.csv"), CSVFormat.RFC4180.withHeader().withDelimiter(','));
        printer.printRecord("id","number","seats","standard","base_price");
        for(Room r : rList){
            ArrayList<String> roomData = new ArrayList<>();
            roomData.add(Integer.toString(r.getId()));
            roomData.add(Integer.toString(r.getNumber()));
            roomData.add(Integer.toString(r.getSeats()));
            roomData.add(r.getStandard().name());
            roomData.add(Float.toString(r.getBasePricePerDay()));
            printer.printRecord(roomData);
        }
        //close the printer
        printer.close();
    }
    public void reservationList(ArrayList<Reservation> rList) {

    }
}
