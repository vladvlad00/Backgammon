package front.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Cookies {
    private static final String path = "/preferences.bkgmn";

    private static Map<String, List<String>> preferences;

    private Cookies() {}

    public static void init() {

    }

    public static void readCookies() {
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            StringBuilder readStrings = new StringBuilder();

            while (reader.hasNextLine()) {
                readStrings.append(reader.nextLine());
            }
            reader.close();

            String cookies = decrypt(readStrings.toString()).replace("{", "").replace("}", "");

            String[] cookiesArray = cookies.split(",\n");

            for(String c : cookiesArray) {
                List<String> arr = new ArrayList<>();
                String[] line = c.split(":");
                if(line[1].contains("[") && line[1].contains("]")) {
                    String[] arrElements = line[1].replace("[", "").replace("]", "").split(", ");
                    for(String arrElem : arrElements) {
                        arr.add(arrElem.replace("\"", ""));
                    }
                }
                else {
                    arr.add(line[1].replace("\"", ""));
                }
                preferences.put(line[0].replace("\"", ""), arr);
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("Cookies file does not exist");
        }
    }

    public static void modifyCookie(String cookie, List<String> value) {
        preferences.put(cookie, value);
    }

    public static void saveCookies() {

    }

    private static String encrypt(String string) {
        return string;
    }

    private static String decrypt(String string) {
        return string;
    }
}
