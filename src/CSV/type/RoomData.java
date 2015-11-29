package CSV.type;

import hotel.Room;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.ArrayList;

public class RoomData implements DataType<Room>{
    @Override
    public ArrayList<Room> importData(CSVParser parser) {
        ArrayList<Room> list = new ArrayList<>();
        for(CSVRecord record : parser){
            Room room = new Room(
                    Integer.parseInt(record.get("number")),
                    Integer.parseInt(record.get("seats")),
                    Float.parseFloat(record.get("base_price")),
                    Room.RoomStandard.valueOf(record.get("standard"))
            );
            list.add(room);
        }
        return list;
    }

    @Override
    public void exportData(ArrayList<Room> list, CSVPrinter printer) throws IOException{
        printer.printRecord("number","seats","standard","base_price");
        for(Room r : list){
            ArrayList<String> roomData = new ArrayList<>();
            roomData.add(Integer.toString(r.getNumber()));
            roomData.add(Integer.toString(r.getSeats()));
            roomData.add(r.getStandard().name());
            roomData.add(Float.toString(r.getBasePricePerDay()));
            printer.printRecord(roomData);
        }
    }
}
