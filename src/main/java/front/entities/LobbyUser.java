package front.entities;

public class LobbyUser {
    private String username;
    private UserRole role;

    public LobbyUser(String username, String role) {
        this.username = username;
        this.role = UserRole.convert(role);
    }

    public void changeRole(String role) {
        this.role = UserRole.convert(role);
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }
}
