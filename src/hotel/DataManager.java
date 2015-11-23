package hotel;

import CSV.CSVExport;
import CSV.CSVImport;

public class DataManager {
    CSVImport csvImport;
    CSVExport csvExport;

    DataManager() {
        csvImport = new CSVImport();
        csvExport = new CSVExport();
    }


}
