package front.entities;

import java.util.List;

public class Lobby {
    private String name;
    private Long id;
    private Integer playerNum;
    private Integer spectatorNum;
    private List<LobbyUser> users;

    public Lobby(String name, Long id, List<LobbyUser> users) {
        this.name = name;
        this.users = users;
        this.id = id;
        this.separateUsers();
    }

    private void separateUsers() {
        playerNum = users.size();
        spectatorNum = 0;
        users.forEach(u -> {
            if(u.getRole().equals(UserRole.SPECTATOR)) {
                ++spectatorNum;
                --playerNum;
            }
        });
    }

    public void updateUsers(List<LobbyUser> users) {
        this.users = users;
        this.separateUsers();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Integer getPlayerNum() {
        return playerNum;
    }

    public Integer getSpectatorNum() {
        return spectatorNum;
    }

    public List<LobbyUser> getUsers() {
        return users;
    }
}
