package CSV.type;

import hotel.Reservation;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.util.Date;
import java.io.IOException;
import java.util.ArrayList;

public class ReservationData implements DataType<Reservation> {
    //  TODO: how to import data from CSV when Room & Person object lists are not available
    @Override
    public ArrayList<Reservation> importData(CSVParser parser) {
        ArrayList<Reservation> list = new ArrayList<>();
//        for(CSVRecord record : parser){
//            Reservation reservation = new Reservation(
//                    Integer.parseInt(record.get("id")),
//                    new Date(record.get("begin")),
//                    new Date(record.get("end")),
//                    Integer.parseInt(record.get("client")),
//                    Integer.parseInt(record.get("room")),
//                    Integer.parseInt(record.get("seats"))
//            );
//            list.add(reservation);
//        }
        return list;
    }

    @Override
    public void exportData(ArrayList<Reservation> list, CSVPrinter printer) throws IOException {
        printer.printRecord("id","begin","end","client", "room", "seats");
        for(Reservation r : list){
            ArrayList<String> reservationData = new ArrayList<>();
            reservationData.add(Integer.toString(r.getId()));
            reservationData.add(r.getBegin().toString());
            reservationData.add(r.getEnd().toString());
            reservationData.add(Integer.toString(r.getPersonId()));
            reservationData.add(Integer.toString(r.getRoomId()));
            reservationData.add(Integer.toString(r.getSeats()));
            printer.printRecord(reservationData);
        }
    }
}
