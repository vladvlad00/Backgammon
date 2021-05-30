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
            case "PLAYER": return PLAYER;
            case "SPECTATOR": return SPECTATOR;
            case "HOST": return HOST;
            case "HOST_SPECTATOR": return HOST_SPECTATOR;
            case "AI_EASY": return AI_EASY;
            case "AI_MEDIUM": return AI_MEDIUM;
            case "AI_HARD": return AI_HARD;
            default: return UNDEFINED;
        }
    }
}
