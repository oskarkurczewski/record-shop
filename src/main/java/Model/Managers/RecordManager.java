package Model.Managers;

import Model.Exceptions.BasicException;
import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;
import Model.Exceptions.RentalException;
import Model.Record;
import Model.Repositories.RecordRepository;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@ApplicationScoped
public class RecordManager {
    RecordRepository repository = new RecordRepository();

    public Record getRecordByID(String recordid) throws NotFoundException {
        return repository.getRecordByID(recordid);
    }

    public List<Record> getAllRecords() {
        return repository.getAllRecords();
    }

    public void appendRecord(Record record) throws BasicException {
        repository.appendRecord(record);
    }

    public void removeRecord(String recordid) throws RentalException, NotFoundException {
        repository.removeRecord(recordid);
    }

    public void modifyRecord(Record record) throws BasicException {
        Record recordFound = this.getRecordByID(record.getRecordID().toString());

        String title = record.getTitle();
        if (!title.equals("")) {
            recordFound.setTitle(title);
        }

        String artist = record.getArtist();
        if (!artist.equals("")) {
            recordFound.setArtist(artist);
        }

        Date releaseDate = record.getReleaseDate();
        if (releaseDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String releaseDateString = formatter.format(record.getReleaseDate());
            try {
                record.setReleaseDate(releaseDateString);
            } catch (ParseException e) {
                throw new BasicException("Wrong date format");
            }
        }
    }
}
