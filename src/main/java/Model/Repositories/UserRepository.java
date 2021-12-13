package Model.Repositories;

import Model.*;
import Model.Exceptions.BasicException;
import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;

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

    public User getUserByID(String userid) throws NotFoundException {
        User user = users.stream()
                .filter( u -> userid.equals(u.getUserID().toString()))
                .findFirst()
                .orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
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

}
