package Model;

import Model.Exceptions.RentalException;

import java.util.ArrayList;
import java.util.List;

public class Client extends User {
    private final List<Rental> rentals = new ArrayList<Rental>();
    private final List<Record> cart = new ArrayList<Record>();

    public Client(int userID, String login) {
        super(userID, login);
    }

    // CART METHODS

    public List<Record> getCart() {
        return this.cart;
    }

    public void addToCart(Record record) throws RentalException {
        if (record.isRented()) {
            throw new RentalException("Record already rented");
        }
        cart.add(record);
    }

    public void removeFromCart(Record record) throws RentalException {
        if (!cart.contains(record)) {
            throw new RentalException("Record not in cart");
        }
        cart.remove(record);
    }

    public void clearCart() {
        cart.clear();
    }


    // RENTALS METHODS

    public List<Rental> getRentals() {
        return this.rentals;
    }

    public void rentCart(Renter renter) {
        for (Record record : this.cart) {
            Rental newRent = new Rental(this, renter, record);
            rentals.add(newRent);
        }
    }

    public void clearRentals(Renter renter) {
        rentals.clear();
    }

    public void extendRentReturnDays(int days) throws RentalException {
        for (Rental rental : this.rentals) {
                rental.extendReturnDays(days);
        }
    }
}
