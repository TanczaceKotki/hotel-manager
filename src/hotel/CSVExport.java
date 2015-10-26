package hotel;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CSVExport {
    public void roomList(ArrayList<Room> rList) throws IOException {
        CSVPrinter printer = new CSVPrinter(new FileWriter("rooms.csv"), CSVFormat.RFC4180.withHeader().withDelimiter(','));
        printer.printRecord("id","number","seats","standard","base_price");
        for(Room r : rList){
            ArrayList<Room> roomList = new ArrayList<>();
            roomList.add(r.getId());
            empData.add(r.getName());
            empData.add(emp.getRole());
            empData.add(emp.getSalary());
            printer.printRecord(empData);
        }
        //close the printer
        printer.close();
    }
    public void reservationList(ArrayList<Reservation> rList) {

    }
}
