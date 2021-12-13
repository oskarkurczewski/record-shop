package Model.Managers;

import Model.Exceptions.NotFoundException;
import Model.Exceptions.RentalException;
import Model.Record;
import Model.Repositories.RecordRepository;

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

    public void appendRecord(Record record){
        repository.appendRecord(record);
    }

    public void removeRecord(String recordid) throws RentalException, NotFoundException {
        repository.removeRecord(recordid);
    }

}
