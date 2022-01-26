package Model.Repositories;

import Model.*;
import Model.Exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    private final List<User> users;

    public UserRepository() {
        User[] arr = {
                new User( "Eleanor123", "P@ssw0rd", UserType.CLIENT),
                new User("Jason", "P@ssw0rd", UserType.CLIENT),
                new User("Chidi", "P@ssw0rd", UserType.CLIENT),
                new User("Tahani", "P@ssw0rd", UserType.CLIENT),
                new User("Michael", "P@ssw0rd", UserType.ADMINISTRATOR),
                new User("DiscoJanet", "P@ssw0rd", UserType.RENTER)
        };
        this.users = new ArrayList<>(Arrays.asList(arr));
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserByID(String userid) throws NotFoundException {
        User user = users.stream()
                .filter( u -> u.getUserID().toString().equals(userid))
                .findFirst()
                .orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public User getUserByLogin(String login) {
        return users.stream()
                .filter( user -> user.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }

    public List<User> getUsersByLogin(String login) {
        return users.stream()
                .filter( user -> user.getLogin().contains(login))
                .collect(Collectors.toList());
    }

    public void appendUser(User user) throws InputException {
        if (this.getUserByLogin(user.getLogin()) != null) {
            throw new InputException("This login already exists");
        }

        users.add(user);
    }

    public void removeUser(String userid) throws BasicException {
        User user = this.getUserByID(userid);

        users.remove(user);
    }

    public void extendRentReturnDays(String renterId, String userId, int days) throws PermissionException, RentalException, NotFoundException {
       User renter = this.getUserByID(renterId);
       User user = this.getUserByID(userId);
       user.extendRentReturnDays(renter, days);
    }

    public User findByLoginPasswordActive(String login, String passwordAsString) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login)
                        && user.getPassword().equals(passwordAsString)
                        && user.isActive())
                .findAny()
                .orElse(null);
    }
}
