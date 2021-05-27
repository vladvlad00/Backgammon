package front.entities;

public class User {
    private static String name;
    private static String token;

    private User() {}

    public static void init(String nameArg, String tokenArg) {
        name = nameArg;
        token = tokenArg;
    }

    public static void logout() {
        name = "";
        token = "";
    }

    public static String getName() {
        return name;
    }

    public static String getToken() {
        return token;
    }
}
