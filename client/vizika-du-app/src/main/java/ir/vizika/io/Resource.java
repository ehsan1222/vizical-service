package ir.vizika.io;

import org.json.JSONArray;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Resource {

    String baseDownloadPath = new File(".").getAbsolutePath() + File.separator + "downloads";

    public boolean storeFile(InputStream content, String fileName) {
        File baseDirectory = new File(baseDownloadPath);

        if (!baseDirectory.isDirectory()) {
            baseDirectory.mkdirs();
        }

        String path = baseDirectory.getAbsolutePath().concat(File.separator).concat(fileName);

        try {
            Files.write(Paths.get(path), content.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
//            return false;
        }
        return true;
    }

    public List<String> convertInputStreamToList(InputStream inputStream) {
        String jsonString = convertStreamToString(inputStream);
        JSONArray jsonArray = convertStringToJsonObject(jsonString);
        List<String> files = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            files.add(jsonArray.getString(i));
        }
        return files;
    }

    private String convertStreamToString(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }

    private JSONArray convertStringToJsonObject(String json) {
        return new JSONArray(json);
    }

}
