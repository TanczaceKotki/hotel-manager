package CSV;

import hotel.SeasonalDiscount;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class SDiscountData implements DataType<SeasonalDiscount> {
    @Override
    public ArrayList<SeasonalDiscount> importData(CSVParser parser) {
        ArrayList<SeasonalDiscount> list = new ArrayList<>();
        for(CSVRecord record : parser){
            SeasonalDiscount discount = new SeasonalDiscount(
                    new Date(record.get("begin")),
                    new Date(record.get("end")),
                    Integer.parseInt(record.get("percentage"))
            );
            list.add(discount);
        }
        return list;
    }

    @Override
    public void exportData(ArrayList<SeasonalDiscount> list, CSVPrinter printer) throws IOException {
        printer.printRecord("begin", "end", "percentage");
        for(SeasonalDiscount r : list){
            ArrayList<String> discountData = new ArrayList<>();
            discountData.add(r.getBegin().toString());
            discountData.add(r.getEnd().toString());
            discountData.add(Integer.toString(r.getPercentage()));
            printer.printRecord(discountData);
        }
    }
}
