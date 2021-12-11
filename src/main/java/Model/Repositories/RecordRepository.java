package Model.Repositories;

import Model.Exceptions.BasicException;
import Model.Exceptions.RentalException;
import Model.Record;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RecordRepository {
    private final List<Record> records =  new ArrayList<>();

    public Record getRecordByID(int recordid) {
        return records.stream()
                .filter( record -> recordid == record.getRecordID())
                .findFirst()
                .orElse(null);
    }


    public void appendRecord(String title, String artist, String releaseDate) throws BasicException {
        int lastId = records.get(records.size() - 1).getRecordID();

        try {
            Record newRecord = new Record(lastId + 1, title, artist, releaseDate);
            records.add(newRecord);
        } catch (ParseException e) {
            throw new BasicException("Wrong date format");
        }
    }

    public void removeRecord(int recordid) throws RentalException {
        Record record = this.getRecordByID(recordid);

        if (record.isRented()) {
            throw new RentalException("Can't remove - this record is rented.");
        }

        records.remove(record);
    }

    public void modifyRecord(int recordid, String title, String artist, String releaseDate) throws BasicException {
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
