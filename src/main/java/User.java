public class User {
    private final int userID;
    private final String login;

    public User(int userID, String login) {
        this.userID = userID;
        this.login = login;
    }

    public int getUserID() {
        return userID;
    }

    public String getLogin() {
        return login;
    }
}
