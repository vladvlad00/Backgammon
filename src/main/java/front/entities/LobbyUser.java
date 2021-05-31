package front.entities;

public class LobbyUser {
    private String username;
    private UserRole role;
    private Boolean white;

    public LobbyUser()
    {
    }

    public LobbyUser(String username, String role) {
        this.username = username;
        this.role = UserRole.convert(role);
    }

    public void changeRole(String role) {
        this.role = UserRole.convert(role);
    }

    public Boolean getWhite() {
        return white;
    }

    public void setWhite(Boolean white) {
        this.white = white;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setRole(UserRole role)
    {
        this.role = role;
    }
}
