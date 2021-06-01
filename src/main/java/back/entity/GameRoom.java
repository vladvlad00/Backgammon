package back.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class GameRoom
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    String state;

    @OneToMany(mappedBy = "gameRoom")
    Set<User> users;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }
}
