package front.entities;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private String name;
    private Long id;
    private Long playerNum;
    private Long spectatorNum;
    private Long aiNum;
    private List<LobbyUser> users;
    private String state;

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
        aiNum = users.stream().filter(u -> u.getRole().equals(UserRole.AI_HARD) || u.getRole().equals(UserRole.AI_MEDIUM) || u.getRole().equals(UserRole.AI_EASY)).count();
        playerNum = users.stream().filter(u -> u.getRole().equals(UserRole.PLAYER) || u.getRole().equals(UserRole.HOST)).count() + aiNum;
        spectatorNum = users.stream().filter(u -> u.getRole().equals(UserRole.SPECTATOR) || u.getRole().equals(UserRole.HOST_SPECTATOR)).count();
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

    public List<String> getPlayers() {
        List<String> names = new ArrayList<>();
        for(LobbyUser u : users) {
            if(u.getRole().equals(UserRole.AI_HARD) || u.getRole().equals(UserRole.AI_MEDIUM) || u.getRole().equals(UserRole.AI_EASY) || u.getRole().equals(UserRole.PLAYER) || u.getRole().equals(UserRole.HOST)) {
                names.add(u.getUsername());
            }
        }
        return names;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Long getPlayerNum() {
        return playerNum == null ? 0 : playerNum;
    }

    public Long getSpectatorNum() {
        return spectatorNum == null ? 0 : spectatorNum;
    }

    public Long getAINum() {
        return aiNum == null ? 0 : aiNum;
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

    public void setUsers(List<LobbyUser> users)
    {
        this.users = users;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
