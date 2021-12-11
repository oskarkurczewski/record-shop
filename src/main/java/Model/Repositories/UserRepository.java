package Model.Repositories;

import Model.Exceptions.BasicException;
import Model.Exceptions.RentalException;
import Model.Record;
import Model.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public User getUserByID(int userid) {
        return users.stream()
                .filter( user -> userid == user.getUserID())
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

    public void appendUser(String login) throws BasicException {
        int lastId = users.get(users.size() - 1).getUserID();

        if (this.getUserByLogin(login) == null) {
            throw new BasicException("This login already exists");
        }

        User newUser = new User(lastId + 1, login);
        users.add(newUser);
    }

}
