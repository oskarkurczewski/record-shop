package Model.Managers;

import Model.Exceptions.BasicException;
import Model.Repositories.UserRepository;
import Model.User;
import Model.UserType;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserManager {
    UserRepository repository = new UserRepository();

    public void toggleActive(String userid) {
        User user = repository.getUserByID(userid);
        user.setActive(!user.getActive());
    }

    public void setUserLogin(String userid, String newLogin) throws BasicException {
        User user = repository.getUserByID(userid);

        if (user.getLogin().equals(newLogin)) {
            throw new BasicException("This login is already set");
        }

        if (repository.getUserByLogin(newLogin) == null) {
            throw new BasicException("This login already exists");
        }

        user.setLogin(newLogin);
    }

    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    public User getUserByID(String userid) {
        return repository.getUserByID(userid);
    }

    public User getUserByLogin(String login) {
        return repository.getUserByLogin(login);
    }

    public List<User> getUsersByLogin(String login) {
        return repository.getUsersByLogin(login);
    }

    public void appendUser(User user) throws BasicException {
        repository.appendUser(user);
    }
}


