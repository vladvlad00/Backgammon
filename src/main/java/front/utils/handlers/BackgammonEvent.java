package front.utils.handlers;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import java.util.Map;

public class BackgammonEvent extends Event {
    public EventType<BackgammonEvent> REFRESH_LOBBY = new EventType<>("REFRESH_LOBBY");
    public EventType<BackgammonEvent> START_GAME = new EventType<>("START_GAME");
    public EventType<BackgammonEvent> ROLL_DICE = new EventType<>("ROLL_DICE");
    public EventType<BackgammonEvent> MAKE_MOVE = new EventType<>("MAKE_MOVE");

    private Map<String, String> options;

    public BackgammonEvent(EventType<? extends Event> eventType, Map<String, String> options) {
        super(eventType);
        this.options = options;
    }

    public BackgammonEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, Map<String, String> options) {
        super(o, eventTarget, eventType);
        this.options = options;
    }
}
