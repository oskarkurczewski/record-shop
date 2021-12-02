import java.util.ArrayList;
import java.util.List;

public class Client extends User {
    private final List<Rental> rentals = new ArrayList<Rental>();

    public Client(int userID, String login) {
        super(userID, login);
    }

    public List<Rental> getRentals() {
        return rentals;
    }

}
