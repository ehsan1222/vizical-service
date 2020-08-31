package com.mxgraph.examples.swing.net;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

//@Log4j2
public class ServerInteraction {

    private final String BASE_URL = "http://localhost:8080/files";
    Logger logger = Logger.getLogger(ServerInteraction.class);


    public boolean checkFilenameExist(String filename) {
        try {
            String url = BASE_URL + "/" + filename;
            logger.info("start checking file exist....");

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTHORIZATION, getAuthorizeHeaderValue());
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            logger.info("send request to check filename exist to server");
            ResponseEntity<String> response = restTemplate.exchange(url, GET, httpEntity, String.class);
            logger.info("receive check filename exist response from server");
            if (response.getStatusCode() == OK) {
                logger.info("receive checkFilenameExist successfully");
                return true;
            } else if (response.getStatusCode() == NO_CONTENT) {
                logger.info("no content response in checkFilenameExist");
                return false;
            }
            logger.warn("invalid response code in checkFilenameExist method: " + response.getStatusCode());
        } catch (HttpClientErrorException e) {
            logger.warn("filename not exist");
            return false;
        }
        return false;
    }

    public boolean uploadFile(File file) {
        try {
            String url = BASE_URL + "/user";
            logger.info("start uploading file....");

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.add(AUTHORIZATION, getAuthorizeHeaderValue());


            MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
            ContentDisposition contentDisposition =
                    ContentDisposition.builder("form-data").name("file").filename(file.getName()).build();


            fileMap.add(CONTENT_DISPOSITION, contentDisposition.toString());
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileBytes, fileMap);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileEntity);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            logger.info("send file request to server");
            ResponseEntity<String> response = restTemplate.exchange(url, POST, requestEntity, String.class);
            logger.info("receive upload file response from server");
            if (response.getStatusCode() == OK) {
                logger.info("upload file successfully");
                return true;
            } else {
                logger.warn("upload failure, code: " + response.getStatusCode());
                return false;
            }
        } catch (IOException | HttpClientErrorException e) {
            logger.warn("error in upload file, error: " + e.getMessage());
            return false;
        }
    }

    public List<String> getJarFilenames() {
        String url = BASE_URL + "/admin";
        logger.info("start getJarFilenames from server process...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, getAuthorizeHeaderValue());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);


        RestTemplate restTemplate = new RestTemplate();
        logger.info("send get all jar filenames request to server");
        ResponseEntity<List<String>> response =
                restTemplate.exchange(url, GET, httpEntity, new ParameterizedTypeReference<>() {});
        logger.info("receive filenames response from server");
        if (response.getStatusCode() == OK) {
            logger.info("receive filenames successfully");
            return response.getBody();
        }
        logger.warn("error in getJarFilenames process. code: " + response.getStatusCode());
        return new ArrayList<>();
    }

    public byte[] downloadFile(String filename) {
        String url = BASE_URL + "/" + filename;
        logger.info("start download file process...");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, getAuthorizeHeaderValue());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        logger.info("send download file request to server");
        ResponseEntity<byte[]> response =
                restTemplate.exchange(url, GET, httpEntity, new ParameterizedTypeReference<>() {});
        logger.info("receive downloaded file response from server");
        if (response.getStatusCode() == OK) {
            logger.info("download file successfully");
            return response.getBody();
        }
        logger.warn("error in downloadFile process. code: " + response.getStatusCode());
        return null;
    }

    private String getAuthorizeHeaderValue() {
        return "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes());
    }

}
