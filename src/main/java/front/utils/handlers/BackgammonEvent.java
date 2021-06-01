package front.utils.handlers;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import java.util.Map;

public class BackgammonEvent extends Event {
    public static final EventType<BackgammonEvent> REFRESH_LOBBY = new EventType<>("REFRESH_LOBBY"); //LobbySearcherPanel
    public static final EventType<BackgammonEvent> START_GAME = new EventType<>("START_GAME"); //LobbySearcherPanel
    public static final EventType<BackgammonEvent> ROLL_DICE = new EventType<>("ROLL_DICE");
    public static final EventType<BackgammonEvent> MAKE_MOVE = new EventType<>("MAKE_MOVE");
    public static final EventType<BackgammonEvent> NEXT_TURN = new EventType<>("NEXT_TURN");
    public static final EventType<BackgammonEvent> UPDATE_SPECTATORS = new EventType<>("UPDATE_SPECTATORS"); //TODO: use
    public static final EventType<BackgammonEvent> DELETE_LOBBY = new EventType<>("DELETE_LOBBY");

    private Map<String, String> options;

    public BackgammonEvent(EventType<? extends Event> eventType, Map<String, String> options) {
        super(eventType);
        this.options = options;
    }

    public BackgammonEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, Map<String, String> options) {
        super(o, eventTarget, eventType);
        this.options = options;
    }

    public Map<String, String> getOptions() {
        return options;
    }
}
