package Model.Repositories;

import Model.Exceptions.BasicException;
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
                    new Record("Moral Panic", "Nothing but Thieves", "23-10-2020"),
                    new Record("Solar Power", "Lorde", "20-08-2021")
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

    public Record getRecordByID(String recordid) {
        return records.stream()
                .filter( record -> recordid.equals(record.getRecordID().toString()))
                .findFirst()
                .orElse(null);
    }


    public void appendRecord(String title, String artist, String releaseDate) throws BasicException {
        Record newRecord = new Record(title, artist, releaseDate);
        records.add(newRecord);
    }

    public void removeRecord(String recordid) throws RentalException {
        Record record = this.getRecordByID(recordid);

        if (record.isRented()) {
            throw new RentalException("Can't remove - this record is rented.");
        }

        records.remove(record);
    }

    public void modifyRecord(String recordid, String title, String artist, String releaseDate) throws BasicException {
        Record record = this.getRecordByID(recordid);

        if (title != null) {
            record.setTitle(title);
        }

        if (artist != null) {
            record.setArtist(artist);
        }

        if (releaseDate != null) {
            try {
                record.setReleaseDate(releaseDate);
            } catch (ParseException e) {
                throw new BasicException("Wrong date format");
            }
        }
    }
}
