package ir.vizika;

import ir.vizika.net.ServerProcesses;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class VizikaMain {

//    private static Map<String, String> users =
//            Map.of("user", "password", "admin", "admin");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ServerProcesses serverProcesses = new ServerProcesses();
        File file;

        label:
        while (true) {
            System.out.println("Choose your operation number:");
            System.out.println("1. Upload mxe file");
            System.out.println("2. Upload jar file");
            System.out.println("3. Download file");
            System.out.println("4. Show list of files");
            System.out.println("5. Exit");
            String command = scanner.nextLine().trim(), filePath;
            switch (command) {
                case "1":
                    System.out.println("Please enter your path:");
                    filePath = scanner.nextLine();
                    file = new File(filePath);

                    if (serverProcesses.uploadFile(file, "user")) {
                        System.out.println("Upload successful!");
                    } else {
                        System.out.println("Upload failed");
                    }
                    break;
                case "2":
                    System.out.println("Please enter your path:");
                    filePath = scanner.nextLine();
                    file = new File(filePath);

                    if (serverProcesses.uploadFile(file, "admin")) {
                        System.out.println("Upload successful!");
                    } else {
                        System.out.println("Upload failed");
                    }
                    break;
                case "3":
                    System.out.println("Please enter filename:");
                    String fileName = scanner.nextLine();

                    if (serverProcesses.downloadFile(fileName)) {
                        System.out.println("Download successful!");
                    } else {
                        System.out.println("Download failed");
                    }
                    break;
                case "4":
                    List<String> filesNames = serverProcesses.listOfFileNames();
                    printFilesNames(filesNames);
                    break;
                case "5":
                    break label;
            }
            System.out.println();
        }

    }

    private static void printFilesNames(List<String> filesNames) {
        int maxLength = filesNames.stream()
                .mapToInt(String::length)
                .max().getAsInt();

        System.out.print("| ");
        String setBoldText = "\033[0;1m";
        String setPlainText = "\033[0;0m";
        System.out.print(setBoldText + "File names" + setPlainText);
        for (int i = 0; i < maxLength - "File names".length(); i++) {
            System.out.print(" ");
        }
        System.out.println(" |");

        for (String filesName : filesNames) {
            System.out.print("+");
            for (int j = 0; j < maxLength + 2; j++) {
                System.out.print("-");
            }
            System.out.print("+");
            System.out.println();

            System.out.print("| ");
            System.out.print(filesName);
            for (int j = 0; j < maxLength - filesName.length(); j++) {
                System.out.print(" ");
            }
            System.out.println(" |");
        }
        System.out.print("+");
        for (int j = 0; j < maxLength + 2; j++) {
            System.out.print("-");
        }
        System.out.print("+");
        System.out.println();
        System.out.println();
    }

//    private static boolean authenticateUser(String username, String password) {
//        return users.get(username) != null && users.get(username).equals(password);
//    }
}
