package Model;

import Model.Exceptions.InputException;
import Model.Exceptions.PermissionException;
import Model.Exceptions.RentalException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID userID;
    private String login;
    private transient String password;
    private final UserType type;
    private Boolean active = true;

    private final transient List<Rental> rentals = new ArrayList<>();
    private final transient List<Rental> archiveRentals = new ArrayList<>();
    private final transient List<Record> cart = new ArrayList<>();

    public User(String login, String password, UserType type) {
        this.userID = UUID.randomUUID();
        this.login = login;
        this.password = password;
        this.type = type;
    }

    public UUID getUserID() {
        return userID;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isActive() {
        return active;
    }


    public void activate() throws InputException {
        if (this.active) {
            throw new InputException("User already activated");
        }
        this.active = true;
    }

    public void deactivate() throws InputException {
        if (!this.active) {
            throw new InputException("User already deactivated");
        }
        this.active = false;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserType getType() {
        return type;
    }

    // CART METHODS

    public List<Record> getCart() {
        return this.cart;
    }

    public void addToCart(Record record) throws RentalException {
        if (!this.active) {
            throw new RentalException("User is not active");
        }

        if (record.isRented()) {
            throw new RentalException("Record already rented");
        }
        cart.add(record);
    }

    public void removeFromCart(Record record) throws RentalException {
        if (!this.active) {
            throw new RentalException("User is not active");
        }

        if (!cart.contains(record)) {
            throw new RentalException("Record not in cart");
        }
        cart.remove(record);
    }

    public void clearCart() throws RentalException {
        if (!this.active) {
            throw new RentalException("User is not active");
        }

        cart.clear();
    }


    // RENTALS METHODS

    public List<Rental> getRentals() {
        return this.rentals;
    }

    public List<Rental> getArchiveRentals() {
        return archiveRentals;
    }

    public List<Rental> rentCart(User renter) throws PermissionException, InputException, RentalException {
        List<Rental> newRentals = new ArrayList<>();

        if (!this.active) {
            throw new RentalException("User is not active");
        }

        if (renter.getType() != UserType.RENTER) {
            throw new PermissionException("Indicated renter has no permissions to do this operation");
        }

        for (Record record : this.cart) {
            Rental newRent = new Rental(this, renter, record);
            rentals.add(newRent);
            newRentals.add(newRent);
        }

        this.clearCart();
        return newRentals;
    }

    public void clearRentals(User renter) throws PermissionException, RentalException {
        if (!this.active) {
            throw new RentalException("User is not active");
        }

        if (renter.getType() != UserType.RENTER) {
            throw new PermissionException("Indicated renter has no permissions to do this operation");
        }

        this.archiveRentals.addAll(this.rentals);
        rentals.clear();
    }

    public void extendRentReturnDays(User renter, int days) throws RentalException, PermissionException {
        if (!this.active) {
            throw new RentalException("User is not active");
        }

        if (renter.getType() != UserType.RENTER) {
            throw new PermissionException("Indicated renter has no permissions to do this operation");
        }

        for (Rental rental : this.rentals) {
            rental.extendReturnDays(days);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .append(userID, user.userID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(userID)
                .toHashCode();
    }

}
