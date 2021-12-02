import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Repository {
    private final List<Record> records =  new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    private User getUserByID(int id) {
        return users.stream()
                .filter( user -> id == user.getUserID())
                .findFirst()
                .orElse(null);
    }

    private User getUserByLogin(String login) {
        return users.stream()
                .filter( user -> login.equals(user.getLogin()))
                .findFirst()
                .orElse(null);
    }

    private Record getRecordByID(int id) {
        return records.stream()
                .filter( record -> id == record.getRecordID())
                .findFirst()
                .orElse(null);
    }

    private void appendUser(String login) throws BasicException {
        int lastId = users.get(users.size() - 1).getUserID();

        if (this.getUserByLogin(login) == null) {
            throw new BasicException("This login already exists");
        }

        User newUser = new User(lastId + 1, login);
        users.add(newUser);
    }

    private void appendRecord(String title, String artist, Date releaseDate) {
        int lastId = records.get(records.size() - 1).getRecordID();

        Record newRecord = new Record(lastId + 1, title, artist, releaseDate);
        records.add(newRecord);
    }

}
