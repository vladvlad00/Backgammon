package back.entity;

import javax.persistence.*;

@Entity
public class GameRoom
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    @OneToOne
    @JoinColumn(name = "player1")
    User player1;

    @OneToOne
    @JoinColumn(name = "player2")
    User player2;

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

    public User getPlayer1()
    {
        return player1;
    }

    public void setPlayer1(User player1)
    {
        this.player1 = player1;
    }

    public User getPlayer2()
    {
        return player2;
    }

    public void setPlayer2(User player2)
    {
        this.player2 = player2;
    }
}
