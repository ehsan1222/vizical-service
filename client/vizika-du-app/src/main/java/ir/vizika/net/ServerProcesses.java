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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class ServerProcesses {

    public static final String HEADER_AUTHENTICATE = "Authorization";
    public static final String AUTHENTICATE_PREFIX = "Basic ";
    public static final String USER = "admin";
    public static final String PASSWORD = "admin";
    private final String baseUrl = "http://localhost:8080/files";

    public List<String> listOfFileNames() {
        List<String> result = new ArrayList<>();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(this.baseUrl);
            String encodedAuthenticate = encodeAuthenticateHeader(USER, PASSWORD);
            get.addHeader(HEADER_AUTHENTICATE, AUTHENTICATE_PREFIX + encodedAuthenticate);
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    InputStream content = response.getEntity().getContent();

                    Resource resource = new Resource();
                    result = resource.convertInputStreamToList(content);
                    return result;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean uploadFile(File file, String urlPath) {
        String url = baseUrl.concat("/").concat(urlPath);

        // incorrect file selected
        if (!file.isFile()) {
            return false;
        }

        String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        boolean isFileExisted         = isFileExistInServer(encodedFilename);
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
                String encodedAuthenticate = encodeAuthenticateHeader(USER, PASSWORD);
                post.addHeader(HEADER_AUTHENTICATE, AUTHENTICATE_PREFIX + encodedAuthenticate);

                FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.addPart("file", fileBody);

                HttpEntity entity = builder.build();
                post.setEntity(entity);
                try (CloseableHttpResponse response = httpClient.execute(post)) {

                    int statusCode = response.getStatusLine().getStatusCode();
//                    System.out.println(statusCode);

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
        String url = baseUrl.concat("/").concat(URLEncoder.encode(fileName, StandardCharsets.UTF_8));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            String encodedAuthenticate = encodeAuthenticateHeader(USER, PASSWORD);
            get.addHeader(HEADER_AUTHENTICATE, AUTHENTICATE_PREFIX + encodedAuthenticate);
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println(statusCode);
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
        // url must be: /files/{fileName}/check
        String url = baseUrl.concat("/").concat(fileName).concat("/").concat("check");

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            String encodedAuthenticate = encodeAuthenticateHeader(USER, PASSWORD);
            get.addHeader(HEADER_AUTHENTICATE, AUTHENTICATE_PREFIX + encodedAuthenticate);
            try (CloseableHttpResponse httpResponse = httpClient.execute(get)) {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    return true;
                } else if (statusCode == HttpStatus.SC_NO_CONTENT) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String encodeAuthenticateHeader(String username, String password) {
        String token = username + ":" + password;
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

}
