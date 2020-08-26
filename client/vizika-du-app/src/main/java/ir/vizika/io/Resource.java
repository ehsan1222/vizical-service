package ir.vizika.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Resource {

    String baseDownloadPath = new File(".").getAbsolutePath() + File.separator + "downloads";

    public boolean storeFile(InputStream content, String fileName) {
        File baseDirectory = new File(baseDownloadPath);

        if (!baseDirectory.isDirectory()) {
            baseDirectory.mkdirs();
        }

        String path = baseDirectory.getAbsolutePath().concat(File.separator).concat(fileName);

        System.out.println(baseDownloadPath);
        System.out.println(path);
        try {
            Files.write(Paths.get(path), content.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
//            return false;
        }
        return true;
    }
}
