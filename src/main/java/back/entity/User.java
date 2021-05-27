package back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class User implements UserDetails
{
    @Id
    String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = false)
    String password;

    String role;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "gameRoomId")
    GameRoom gameRoom;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public GameRoom getGameRoom()
    {
        return gameRoom;
    }

    public void setGameRoom(GameRoom gameRoom)
    {
        this.gameRoom = gameRoom;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return new ArrayList<>();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
