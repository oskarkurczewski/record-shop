package Model.Repositories;

import Model.*;
import Model.Exceptions.BasicException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    private final List<User> users;

    public UserRepository() {
        User[] arr = {
                new User( "Eleanor", UserType.CLIENT),
                new User("Jason", UserType.CLIENT),
                new User("Chidi", UserType.ADMINISTRATOR),
                new User("Tahani", UserType.RENTER)
        };
        this.users = new ArrayList<>(Arrays.asList(arr));
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserByID(String userid) {
        return users.stream()
                .filter( user -> userid.equals(user.getUserID().toString()))
                .findFirst()
                .orElse(null);
    }

    public User getUserByLogin(String login) {
        return users.stream()
                .filter( user -> login.equals(user.getLogin()))
                .findFirst()
                .orElse(null);
    }

    public List<User> getUsersByLogin(String login) {
        return users.stream()
                .filter( user -> login.contains(user.getLogin()))
                .collect(Collectors.toList());
    }

    public void appendUser(User user) throws BasicException {
        if (this.getUserByLogin(user.getLogin()) != null) {
            throw new BasicException("This login already exists");
        }

        users.add(user);
    }

}
