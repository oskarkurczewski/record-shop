package Model.Managers;

import Model.Exceptions.BasicException;
import Model.Repositories.UserRepository;
import Model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserManager {
    UserRepository repository = new UserRepository();

    public void toggleActive(int userid) {
        User user = repository.getUserByID(userid);
        user.setActive(!user.getActive());
    }

    public void setUserLogin(int userid, String newLogin) throws BasicException {
        User user = repository.getUserByID(userid);

        if (user.getLogin().equals(newLogin)) {
            throw new BasicException("This login is already set");
        }

        if (repository.getUserByLogin(newLogin) == null) {
            throw new BasicException("This login already exists");
        }

        user.setLogin(newLogin);
    }

    public User getUserByID(int userid) {
        return repository.getUserByID(userid);
    }

    public User getUserByLogin(String login) {
        return repository.getUserByLogin(login);
    }

    public List<User> getUsersByLogin(String login) {
        return repository.getUsersByLogin(login);
    }

    public void appendUser(String login) throws BasicException {
        repository.appendUser(login);
    }
}


