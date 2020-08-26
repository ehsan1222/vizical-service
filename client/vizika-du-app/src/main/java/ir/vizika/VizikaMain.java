import java.util.Map;
import java.util.Scanner;

public class VizikaMain {

    private static Map<String, String> users = Map.of("user", "password", "admin", "admin");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter username: ");
        String username = scanner.nextLine().trim();
        System.out.println();
        System.out.println("Please enter password: ");
        String password = scanner.nextLine().trim();

        if (authenticateUser(username, password)) {
            label:
            while(true) {
                System.out.println("Choose your operation number:");
                System.out.println("1. Upload file");
                System.out.println("2. Download file");
                System.out.println("3. Exit");
                String command = scanner.nextLine().trim();
                switch (command) {
                    case "1":
                        System.out.println("Please enter your path:");
                        String filePath = scanner.nextLine();
                        break;
                    case "2":
                        System.out.println("Please enter filename:");
                        String filename = scanner.nextLine();

                        break;
                    case "3":
                        break label;
                }
            }
        }
    }

    private static boolean authenticateUser(String username, String password) {
        return users.get(username) != null && users.get(username).equals(password);
    }
}
