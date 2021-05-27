package back.repository;

import back.entity.GameRoom;
import org.springframework.data.repository.CrudRepository;

public interface GameRoomRepository extends CrudRepository<GameRoom, Long>
{
}
