package Model.Managers;

import Model.Exceptions.BasicException;
import Model.Exceptions.RentalException;
import Model.Record;
import Model.Repositories.RecordRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RecordManager {
    RecordRepository repository = new RecordRepository();

    public Record getRecordByID(String recordid) {
        return repository.getRecordByID(recordid);
    }


    public void appendRecord(String title, String artist, String releaseDate) throws BasicException {
        repository.appendRecord(title, artist, releaseDate);
    }

    public void removeRecord(int recordid) throws RentalException {
        removeRecord(recordid);
    }

    public void modifyRecord(int recordid, String title, String artist, String releaseDate) throws BasicException {
        modifyRecord(recordid, title, artist, releaseDate);
    }
}
