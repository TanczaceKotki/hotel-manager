package CSV;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.ArrayList;

interface DataType <T> {
    ArrayList<T> importData(CSVParser parser);
    void exportData(ArrayList<T> list, CSVPrinter printer) throws IOException;
}
