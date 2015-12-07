package CSV;

import hotel.Person;
import hotel.Reservation;
import hotel.Room;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.util.Date;
import java.io.IOException;
import java.util.ArrayList;

class ReservationData implements DataType<Reservation> {

    ArrayList<Person> persons;
    ArrayList<Room> rooms;

    public ReservationData(ArrayList<Person> persons, ArrayList<Room> rooms) {
        updateClientRoomLists(persons, rooms);
    }

    public void updateClientRoomLists(ArrayList<Person> persons, ArrayList<Room> rooms) {
        this.persons = persons;
        this.rooms = rooms;
    }
    @Override
    public ArrayList<Reservation> importData(CSVParser parser) {
        ArrayList<Reservation> list = new ArrayList<>();
        for(CSVRecord record : parser){
            Reservation reservation = new Reservation(
                    Integer.parseInt(record.get("id")),
                    new Date(record.get("begin")),
                    new Date(record.get("end")),
                    rooms.stream().filter(x -> x.getNumber() == Integer.parseInt(record.get("room"))).findFirst().get(),
                    persons.stream().filter(x -> x.getId() == Integer.parseInt(record.get("client"))).findFirst().get(),
                    Integer.parseInt(record.get("seats"))
            );
            list.add(reservation);
        }
        return list;
    }

    @Override
    public void exportData(ArrayList<Reservation> list, CSVPrinter printer) throws IOException {
        printer.printRecord("id","begin","end","client", "room", "seats");
        for(Reservation r : list){
            ArrayList<String> reservationData = new ArrayList<>();
            reservationData.add(r.getId().toString());
            reservationData.add(r.getBegin().toString());
            reservationData.add(r.getEnd().toString());
            reservationData.add(Integer.toString(r.getPersonId()));
            reservationData.add(Integer.toString(r.getRoomId()));
            reservationData.add(Integer.toString(r.getSeats()));
            printer.printRecord(reservationData);
        }
    }
}
