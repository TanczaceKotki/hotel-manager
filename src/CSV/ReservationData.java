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

    public ReservationData(ArrayList<Room> rooms, ArrayList<Person> persons) {
        updateLists(rooms, persons);
    }

    public void updateLists(ArrayList<Room> rooms, ArrayList<Person> persons) {
        this.persons = persons;
        this.rooms = rooms;
    }
    @Override
    public ArrayList<Reservation> importData(CSVParser parser) {
        Room roomTmp;
        Person clientTmp;
        ArrayList<Reservation> list = new ArrayList<>();
        for(CSVRecord record : parser){
            String roomIdTmp = record.get("room");
            if (roomIdTmp != null && !roomIdTmp.equals("0")) {
                roomTmp = rooms.stream().filter(x -> x.getNumber() == Integer.parseInt(roomIdTmp)).findFirst().get();
            } else {
                roomTmp = null;
            }

            String clientIdTmp = record.get("client");
            if (clientIdTmp != null && !clientIdTmp.equals("0")) {
                clientTmp = persons.stream().filter(x -> x.getId() == Integer.parseInt(clientIdTmp)).findFirst().get();
            } else {
                clientTmp = null;
            }

            Reservation reservation = new Reservation(
                    Integer.parseInt(record.get("id")),
                    new Date(record.get("begin")),
                    new Date(record.get("end")),
                    roomTmp,
                    clientTmp,
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
