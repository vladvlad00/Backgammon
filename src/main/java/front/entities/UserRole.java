package front.entities;

public enum UserRole {
    PLAYER,
    SPECTATOR,
    HOST,
    AI,
    UNDEFINED;

    public static UserRole convert(String role) {
        switch (role) {
            case "player": return PLAYER;
            case "spectator": return SPECTATOR;
            case "host": return HOST;
            case "ai": return AI;
            default: return UNDEFINED;
        }
    }
}
