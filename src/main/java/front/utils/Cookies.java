package front.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Cookies {
    private static final String path = "/preferences.bkgmn";

    private static Map<String, String> preferences;

    private Cookies() {}

    public static void init() {
        preferences = new HashMap<>();
        readCookies();
    }

    public static String getValue(String string) {
        return preferences.getOrDefault(string, "notExist");
    }

    public static void readCookies() {
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            String token = decrypt(reader.nextLine());
            preferences.put("token", token);
        }
        catch (FileNotFoundException e) {
            System.err.println("Cookies file does not exist");
        }
    }

    public static void modifyCookie(String cookie, String value) {
        preferences.put(cookie, value);
    }

    public static void saveCookies() {
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("Preferences file created");
            }
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(encrypt(preferences.get("token")));
            fileWriter.close();
        }
        catch (IOException e) {
            System.err.println("IO exception");
        }
    }

    private static String encrypt(String string) {
        return string;
    }

    private static String decrypt(String string) {
        return string;
    }
}
