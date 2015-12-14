package CSV;

import hotel.EarlyBookingDiscount;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.ArrayList;

public class EBDiscountsData implements DataType<EarlyBookingDiscount> {
    @Override
    public ArrayList<EarlyBookingDiscount> importData(CSVParser parser) {
        ArrayList<EarlyBookingDiscount> list = new ArrayList<>();
        for(CSVRecord record : parser){
            EarlyBookingDiscount discount = new EarlyBookingDiscount(
                    Integer.parseInt(record.get("months")),
                    Integer.parseInt(record.get("percentage"))
            );
            list.add(discount);
        }
        return list;
    }

    @Override
    public void exportData(ArrayList<EarlyBookingDiscount> list, CSVPrinter printer) throws IOException {
        printer.printRecord("months", "percentage");
        for(EarlyBookingDiscount r : list){
            ArrayList<String> discountData = new ArrayList<>();
            discountData.add(Integer.toString(r.getMonths()));
            discountData.add(Integer.toString(r.getPercentage()));
            printer.printRecord(discountData);
        }
    }
}
