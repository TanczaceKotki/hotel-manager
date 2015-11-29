package CSV.type;

import hotel.Reservation;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.ArrayList;

public class ReservationData implements DataType<Reservation> {
    @Override
    public ArrayList<Reservation> importData(CSVParser parser) {
        return null;
    }

    @Override
    public void exportData(ArrayList<Reservation> list, CSVPrinter printer) throws IOException {

    }
}
