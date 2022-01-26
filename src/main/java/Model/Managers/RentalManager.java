package Model.Managers;

import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;
import Model.Exceptions.RentalException;
import Model.Rental;
import Model.Repositories.RentalRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RentalManager {
    RentalRepository repository = new RentalRepository();

    public Rental getRentalByID(String rentalID) throws NotFoundException {
        return repository.getRentalByID(rentalID);
    }

    public List<Rental> getAllRentals() {
        return repository.getAllRentals();
    }

    public List<Rental> getAllArchiveRentals() {
        return repository.getAllArchiveRentals();
    }

    public synchronized void appendRental(Rental rental){
        repository.appendRental(rental);
    }

    public synchronized void appendRentals(List<Rental> rents){
        repository.appendRentals(rents);
    }

    public synchronized void archiveRental(String rentalID) throws RentalException, NotFoundException, InputException {
        repository.archiveRental(rentalID);
    }

    public synchronized void archiveRentals(List<Rental> rents) throws RentalException, NotFoundException, InputException {
        repository.archiveRentals(rents);
    }

}
