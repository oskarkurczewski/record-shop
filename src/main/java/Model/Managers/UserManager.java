package Model.Managers;

import Model.Exceptions.BasicException;
import Model.Exceptions.InputException;
import Model.Exceptions.NotFoundException;
import Model.Repositories.UserRepository;
import Model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserManager {
    UserRepository repository = new UserRepository();

    public void setUserLogin(String userid, String newLogin) throws InputException, NotFoundException {
        User user = repository.getUserByID(userid);

        if (user.getLogin().equals(newLogin)) {
            throw new InputException("This login is already set");
        }

        if (repository.getUserByLogin(newLogin) == null) {
            throw new InputException("This login already exists");
        }

        user.setLogin(newLogin);
    }

    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    public User getUserByID(String userid) throws NotFoundException {
        return repository.getUserByID(userid);
    }

    public User getUserByLogin(String login) {
        return repository.getUserByLogin(login);
    }

    public List<User> getUsersByLogin(String login) {
        return repository.getUsersByLogin(login);
    }

    public void appendUser(User user) throws InputException {
        repository.appendUser(user);
    }

    public void removeUser(String userid) throws BasicException {
        repository.removeUser(userid);
    }

}


