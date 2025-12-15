public class User {
    private String username;
    private String password;
    private String role;
    private static User currentUser; // Add static field for current user
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Session management methods
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }
    
    public static void logout() {
        currentUser = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}