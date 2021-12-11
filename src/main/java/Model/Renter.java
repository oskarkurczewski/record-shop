package Model;

public class Renter extends User {
    public Renter(int userID, String login) {
        super(userID, login);
    }

    public void rent(Client client) {
        client.rentCart(this);
    }

    public void clearRentalsForClient(Client client) {
        client.clearRentals(this);
    }
}
