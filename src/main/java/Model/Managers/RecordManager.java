package Model.Managers;

import Model.Exceptions.NotFoundException;
import Model.Exceptions.RentalException;
import Model.Record;
import Model.Repositories.RecordRepository;
import utils.EntitySigner;

import java.text.ParseException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class RecordManager {
    RecordRepository repository = new RecordRepository();

    public Record getRecordByID(String recordid) throws NotFoundException {
        return repository.getRecordByID(recordid);
    }

    public List<Record> getAllRecords() {
        return repository.getAllRecords();
    }

    public synchronized void appendRecord(Record record){
        repository.appendRecord(record);
    }

    public synchronized void removeRecord(String recordid) throws RentalException, NotFoundException {
        repository.removeRecord(recordid);
    }

    public synchronized void modifyRecord(Record record, String title, String artist, String releaseDate, String etag) throws ParseException {

        if (! EntitySigner.verifyEntity(etag, record)) {
            throw new IllegalArgumentException("You are trying to modify the wrong record!");
        }

        if (title.length() > 0) {
            record.setTitle(title);
        }

        if (artist.length() > 0) {
            record.setArtist(artist);
        }

        if (releaseDate.length() > 0) {
            record.setReleaseDate(releaseDate);
        }
    }

}
