package front.entities;

public enum UserRole {
    PLAYER,
    SPECTATOR,
    HOST,
    HOST_SPECTATOR,
    AI_EASY,
    AI_MEDIUM,
    AI_HARD,
    UNDEFINED;

    public static UserRole convert(String role) {
        switch (role) {
            case "player": return PLAYER;
            case "spectator": return SPECTATOR;
            case "host": return HOST;
            case "host_spectator": return HOST_SPECTATOR;
            case "ai_easy": return AI_EASY;
            case "ai_medium": return AI_MEDIUM;
            case "ai_hard": return AI_HARD;
            default: return UNDEFINED;
        }
    }
}
