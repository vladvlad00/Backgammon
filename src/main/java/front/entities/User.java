package front.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

// TODO -> de facut singleton
public class User {
    private String username;
    private String password;
    @JsonIgnore
    private String token;

    @JsonIgnore
    private Boolean inRoom;

    private static User instance = null;

    private User() {}

    public void logout() {
        username = "";
        password = "";
        token = "";
    }

    public static User getInstance()
    {
        if (instance == null)
            instance = new User();
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() { return password; }

    public String getToken() {
        return token;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Boolean getInRoom() {
        return inRoom;
    }

    public void setInRoom(Boolean inRoom) {
        this.inRoom = inRoom;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }
}
