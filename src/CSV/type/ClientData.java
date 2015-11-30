package CSV.type;

import hotel.Person;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.ArrayList;

public class ClientData implements DataType<Person>{
    @Override
    public ArrayList<Person> importData(CSVParser parser) {
        ArrayList<Person> list = new ArrayList<>();
        for(CSVRecord record : parser){
            Person person = new Person(
                    Integer.parseInt(record.get("id")),
                    record.get("firstname"),
                    record.get("surname"),
                    record.get("telephone"),
                    record.get("email")
            );
            list.add(person);
        }
        return list;
    }

    @Override
    public void exportData(ArrayList<Person> list, CSVPrinter printer) throws IOException {
        printer.printRecord("number","seats","standard","base_price");
        for(Person p : list){
            ArrayList<String> clientData = new ArrayList<>();
            clientData.add(Integer.toString(p.getId()));
            clientData.add(p.getFirstname());
            clientData.add(p.getSurname());
            clientData.add(p.getTelephone());
            clientData.add(p.getEmail());
            printer.printRecord(clientData);
        }
    }
}
