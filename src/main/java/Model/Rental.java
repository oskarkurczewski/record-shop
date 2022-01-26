package Model;

import Model.Exceptions.InputException;
import Model.Exceptions.PermissionException;
import Model.Exceptions.RentalException;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Rental {
    private final UUID rentalID;
    private final String clientID;
    private final String renterID;
    private final String recordID;

    private final User client;
    private final User renter;
    private final Record record;

    private final LocalDateTime rentDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualReturnDate;

    public Rental(User client, User renter, Record record) throws PermissionException, InputException {
        if (renter.getType() != UserType.RENTER) {
            throw new PermissionException("Indicated renter has no permissions to do this operation");
        }

        this.rentalID = UUID.randomUUID();
        this.clientID = client.getUserID().toString();
        this.renterID = renter.getUserID().toString();
        this.recordID = record.getRecordID().toString();

        this.client = client;
        this.renter = renter;
        this.record = record;

        this.rentDate = LocalDateTime.now();
        this.record.rent(this);

        this.expectedReturnDate = this.rentDate.plusDays(7);
    }

        public User getClient() {
            return client;
        }

        public User getRenter() {
            return renter;
        }

    public String getClientID() {
        return clientID;
    }

    public String getRenterID() {
        return renterID;
    }

    public String getRecordID() {
        return recordID;
    }

    public LocalDateTime getRentDate() {
        return rentDate;
    }

    public LocalDateTime getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public Record getRecord() {
        return record;
    }

    public LocalDateTime getActualReturnDate() {
        return actualReturnDate;
    }

    public int callCops() { return 997; }

    public void returnRecord() throws InputException {
        this.actualReturnDate = LocalDateTime.now();
        this.record.release();
    }

    public void extendReturnDays(int days) throws RentalException {
        if (days <= 0) {
            throw new RentalException("Wrong number of days");
        }

        this.expectedReturnDate = expectedReturnDate.plusDays(days);
    }


    public Object getRentalID() {
        return this.rentalID;
    }
}
