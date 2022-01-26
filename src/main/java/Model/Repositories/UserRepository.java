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
                new User( "Eleanor123", UserType.CLIENT),
                new User("Jason", UserType.CLIENT),
                new User("Chidi", UserType.CLIENT),
                new User("Tahani", UserType.CLIENT),
                new User("Michael", UserType.ADMINISTRATOR),
                new User("DiscoJanet", UserType.RENTER)
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
}
