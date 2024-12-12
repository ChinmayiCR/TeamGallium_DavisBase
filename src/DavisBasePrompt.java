import common.CatalogDatabaseHelper;
import common.DatabaseConstants;
import query.Handler;

import java.util.Scanner;

import static query.Parser.isExit;
import static query.Parser.parseCommand;


public class DavisBasePrompt {

    private static Scanner scanner = new Scanner(System.in).useDelimiter(";");

    public static void main(String[] args) {

        CatalogDatabaseHelper.InitializeDatabase();
        splashScreen();

        while(!isExit) {
            System.out.print(DatabaseConstants.PROMPT);
            String command = scanner.next().replace("\n", "").replace("\r", " ").trim().toLowerCase();
            parseCommand(command);
        }
    }

    private static void splashScreen() {
        System.out.println(Handler.line("-",80));
        System.out.println("Welcome to DavisBase"); // Display the string.
        System.out.println("\nType 'help;' to display supported commands.");
        System.out.println(Handler.line("-",80));
    }
}