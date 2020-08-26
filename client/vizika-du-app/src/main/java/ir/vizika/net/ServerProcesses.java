package ir.vizika.net;

import ir.vizika.io.Resource;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Connection {

    private String baseUrl = "http://localhost:8080/file";


    public boolean uploadFile(File file, String urlPath) {
        String url = baseUrl.concat("/").concat(urlPath);

        boolean isFileExisted         = isFileExistInServer(file.getName());
        boolean continueUploadProcess = true;

        if (isFileExisted) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("file exist on server:");
            System.out.println("1. Force Upload");
            System.out.println("2. Cancel Upload");
            String command = scanner.nextLine();
            if (command.equals("2")) {
                continueUploadProcess = false;
            }
        }

        if (continueUploadProcess) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(url);
                FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.addPart("file", fileBody);

                HttpEntity entity = builder.build();
                post.setEntity(entity);
                try (CloseableHttpResponse response = httpClient.execute(post)) {

                    int statusCode = response.getStatusLine().getStatusCode();
                    System.out.println(statusCode);

                    if (statusCode == HttpStatus.SC_OK) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean downloadFile(String fileName) {
        String url = baseUrl.concat("/").concat(fileName);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    InputStream content = response.getEntity().getContent();

                    Resource resource = new Resource();
                    // store file in downloads directory and if process is ok then true be return otherwise false return
                    return resource.storeFile(content, fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isFileExistInServer(String fileName) {
        // url must be: /file/{fileName}/check
        String url = baseUrl.concat("/").concat(fileName).concat("/").concat("check");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            try (CloseableHttpResponse httpResponse = httpClient.execute(get)) {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    return true;
                } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
