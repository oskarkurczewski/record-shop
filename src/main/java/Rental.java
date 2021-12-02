import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rental {
    private final Client client;
    private final Renter renter;
    private final Date rentDate;
    private Date returnDate;

    public Rental(Client client, Renter renter, String returnDate) throws BasicException {
        this.client = client;
        this.renter = renter;
        this.rentDate = new Date();

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            this.returnDate = simpleDateFormat.parse(returnDate);
        } catch (ParseException e) {
            throw new BasicException("Incorrect date format");
        }

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

    public Date getReturnDate() {
        return returnDate;
    }

    public void updateReturnDate(String newReturnDateString) throws BasicException {
        Date newReturnDate;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            newReturnDate = simpleDateFormat.parse(newReturnDateString);
        } catch (ParseException e) {
            throw new BasicException("Incorrect date format");
        }

        if (this.rentDate.after(newReturnDate)) {
            throw new BasicException("Given date is before rent date");
        }

        this.returnDate = newReturnDate;
    }
}
