package Model;

import Model.Exceptions.InputException;
import Model.Exceptions.PermissionException;
import Model.Exceptions.RentalException;

import java.util.Calendar;
import java.util.Date;

public class Rental {
    private final String clientID;
    private final String renterID;
    private final String recordID;

    private transient final User client;
    private transient final User renter;
    private transient final Record record;

    private final Date rentDate;
    private Date expectedReturnDate;
    private Date actualReturnDate;

    public Rental(User client, User renter, Record record) throws PermissionException, InputException {
        if (renter.getType() != UserType.RENTER) {
            throw new PermissionException("Indicated renter has no permissions to do this operation");
        }

        this.clientID = client.getUserID().toString();
        this.renterID = renter.getUserID().toString();
        this.recordID = record.getRecordID().toString();

        this.client = client;
        this.renter = renter;
        this.record = record;

        this.rentDate = new Date();
        this.record.rent(this);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        this.expectedReturnDate = cal.getTime();
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

    public Date getRentDate() {
        return rentDate;
    }

    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public Record getRecord() {
        return record;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    public int callCops() { return 997; }

    public void returnRecord() throws InputException {
        this.actualReturnDate = new Date();
        this.record.release();
    }

    public void extendReturnDays(int days) throws RentalException {
        if (days <= 0) {
            throw new RentalException("Wrong number of days");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.expectedReturnDate);
        cal.add(Calendar.DAY_OF_MONTH, days);

        this.expectedReturnDate = cal.getTime();
    }


}
