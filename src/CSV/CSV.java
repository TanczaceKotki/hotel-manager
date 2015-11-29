package CSV;

import CSV.type.DataType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSV {
    static CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
    public <T> ArrayList<T> importData (File file, DataType<T> dataType) throws IOException {
        FileReader reader = new FileReader(file);
        CSVParser parser = new CSVParser(reader, format);
        ArrayList<T> list = dataType.importData(parser);

        parser.close();
        reader.close();
        return list;
    }

    public <T> void exportData (ArrayList<T> list, File file, DataType<T> dataType) throws IOException {
        FileWriter writer = new FileWriter(file);
        CSVPrinter printer = new CSVPrinter(writer, format);
        dataType.exportData(list, printer);

        //close the printer
        printer.close();
        writer.close();
    }
}
