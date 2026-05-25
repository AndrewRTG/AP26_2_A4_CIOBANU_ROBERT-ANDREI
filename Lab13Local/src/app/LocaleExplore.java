package app;

import com.DisplayLocales;
import com.Info;
import com.SetLocale;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocaleExplore {
    private static Locale currentLocale = Locale.getDefault();
    private static ResourceBundle messages = ResourceBundle.getBundle("res.Messages", currentLocale);

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void setCurrentLocale(Locale locale) {
        currentLocale = locale;
        messages = ResourceBundle.getBundle("res.Messages", currentLocale);
    }

    public static String message(String key) {
        return messages.getString(key);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(message("prompt") + " ");
            String line = scanner.nextLine();

            if (line == null || line.isBlank()) {
                continue;
            }

            if (line.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = line.trim().split("\\s+");
            String command = tokens[0].toLowerCase();

            switch (command) {
                case "locales" -> new DisplayLocales().execute(tokens);
                case "set" -> new SetLocale().execute(tokens);
                case "info" -> new Info().execute(tokens);
                default -> System.out.println(message("invalid"));
            }
        }
    }
}