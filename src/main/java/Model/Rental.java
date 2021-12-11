package Model;

import Model.Exceptions.RentalException;

import java.util.Calendar;
import java.util.Date;

public class Rental {
    private final Client client;
    private final Renter renter;
    private final Record record;
    private final Date rentDate;
    private Date expectedReturnDate;
    private Date actualReturnDate;

    public Rental(Client client, Renter renter, Record record) {
        this.client = client;
        this.renter = renter;
        this.record = record;
        this.rentDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        this.expectedReturnDate = cal.getTime();
    }

    public Client getClient() {
        return client;
    }

    public Renter getRenter() {
        return renter;
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

    public void returnRecord() {
        this.actualReturnDate = new Date();
        this.record.setRented(false);
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
