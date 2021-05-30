package front.entities;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private String name;
    private Long id;
    private Integer playerNum;
    private Integer spectatorNum;
    private List<LobbyUser> users;

    public Lobby()
    {
    }

    public Lobby(String name, Long id, List<LobbyUser> users) {
        this.name = name;
        this.users = users;
        this.id = id;
        this.separateUsers();
    }

    public void separateUsers() {
        playerNum = users.size();
        spectatorNum = 0;
        users.forEach(u -> {
            if(u.getRole().equals(UserRole.SPECTATOR) || u.getRole().equals(UserRole.HOST_SPECTATOR)) {
                ++spectatorNum;
                --playerNum;
            }
        });
    }

    public void updateUsers(List<LobbyUser> users) {
        this.users = users;
        this.separateUsers();
    }

    public UserRole getRoleOfUser(String name) {
        for(LobbyUser lobbyUser : users) {
            if(lobbyUser.getUsername().equals(name)) {
                return lobbyUser.getRole();
            }
        }
        return UserRole.UNDEFINED;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Integer getPlayerNum() {
        return playerNum == null ? 0 : playerNum;
    }

    public Integer getSpectatorNum() {
        return spectatorNum == null ? 0 : spectatorNum;
    }

    public List<LobbyUser> getUsers() {
        return users == null ? new ArrayList<>() : users;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setPlayerNum(Integer playerNum)
    {
        this.playerNum = playerNum;
    }

    public void setSpectatorNum(Integer spectatorNum)
    {
        this.spectatorNum = spectatorNum;
    }

    public void setUsers(List<LobbyUser> users)
    {
        this.users = users;
    }
}
