import java.util.ArrayList;

public class Authentication {

    private ArrayList<User> users;

    public Authentication() {
        users = new ArrayList<>();
        loadDefaultUsers();
    }

    private void loadDefaultUsers() {
        users.add(new User("admin", "admin123")); // sample user
    }

    public User validateLogin(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) &&
                user.getPassword().equals(password)) {
                return user; // login success
            }
        }
        return null; // login failed
    }
}