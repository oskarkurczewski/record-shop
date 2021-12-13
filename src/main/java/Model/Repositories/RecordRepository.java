package Model.Repositories;

import Model.Exceptions.BasicException;
import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;
import Model.Exceptions.RentalException;
import Model.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordRepository {
    private List<Record> records;

    public RecordRepository() {
        this.records  =  new ArrayList<>();
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


    public void appendRecord(Record record) {
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
