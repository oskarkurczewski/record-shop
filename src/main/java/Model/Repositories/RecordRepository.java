package Model.Repositories;

import Model.Exceptions.BasicException;
import Model.Exceptions.NotFoundException;
import Model.Exceptions.RentalException;
import Model.Record;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordRepository {
    private List<Record> records;

    public RecordRepository() {
        try {
            Record[] arr = {
                    new Record("Solar Panic", "Nothing but Lorde", "23-10-2020"),
                    new Record("Moral Power", "Thieves", "20-08-2021")
            };

            this.records  =  new ArrayList<>(Arrays.asList(arr));
        } catch (BasicException e) {
            this.records = new ArrayList<>();
            System.out.println("Zły format dat przy inicjalizacji danych");
        }
    }

    public List<Record> getAllRecords() {
        return records;
    }

    public Record getRecordByID(String recordid) throws NotFoundException {
        Record record = records.stream()
                .filter( r -> recordid.equals(r.getRecordID().toString()))
                .findFirst()
                .orElse(null);
        if (record == null) {
            throw new NotFoundException("Record not found");
        }
        return record;
    }


    public void appendRecord(Record record) throws BasicException {
        records.add(record);
    }

    public void removeRecord(String recordid) throws RentalException, NotFoundException {
        Record record = this.getRecordByID(recordid);

        if (record.isRented()) {
            throw new RentalException("Can't remove - this record is rented.");
        }

        records.remove(record);
    }
}
