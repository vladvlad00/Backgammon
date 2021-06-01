# Backgammon

This is an implementation of the game "Backgammon". We used a client-server approach to let multiple sessions (lobbies) co-exist. Therefore any number of players can play against each other in 1v1 games. In any game there can also be any number of spectators. They can watch the game, but they have no effect on it.

This project was developed by [Andrei Zaborilă](https://github.com/Andreizabo) and [Vlad Teodorescu](https://github.com/vladvlad00).

## Front-end

The front-end part of the project refers strictly to the client interface. 
  
  
  

```java
public class Message
{
    private String command;
    private Map<String, String> options;
    //...
}
```