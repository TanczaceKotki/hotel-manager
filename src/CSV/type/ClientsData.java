package CSV.type;

import hotel.Person;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.ArrayList;

public class ClientsData implements DataType<Person>{
    @Override
    public ArrayList<Person> importData(CSVParser parser) {
        return null;
    }

    @Override
    public void exportData(ArrayList<Person> list, CSVPrinter printer) throws IOException {

    }
}
