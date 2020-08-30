package com.mxgraph.examples.swing.net;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Log4j2
public class ServerInteraction {

    private final String BASE_URL = "http://localhost:8080/files";

    public boolean checkFilenameExist(String filename) {
        try {
            String url = BASE_URL + "/" + filename;
            log.info("start checking file exist....");

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTHORIZATION, getAuthorizeHeaderValue());
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            log.info("send request to check filename exist to server");
            ResponseEntity<String> response = restTemplate.exchange(url, GET, httpEntity, String.class);
            log.info("receive check filename exist response from server");
            if (response.getStatusCode() == OK) {
                log.info("receive checkFilenameExist successfully");
                return true;
            } else if (response.getStatusCode() == NO_CONTENT) {
                log.info("no content response in checkFilenameExist");
                return false;
            }
            log.warn("invalid response code in checkFilenameExist method: " + response.getStatusCode());
        } catch (HttpClientErrorException e) {
            log.warn("filename not exist");
            return false;
        }
        return false;
    }

    public boolean uploadFile(File file) {
        try {
            String url = BASE_URL + "/admin";
            log.info("start uploading file....");

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

            log.info("send file request to server");
            ResponseEntity<String> response = restTemplate.exchange(url, POST, requestEntity, String.class);
            log.info("receive upload file response from server");
            if (response.getStatusCode() == OK) {
                log.info("upload file successfully");
                return true;
            } else {
                log.warn("upload failure, code: " + response.getStatusCode());
                return false;
            }
        } catch (IOException | HttpClientErrorException e) {
            log.warn("error in upload file, error: " + e.getMessage());
            return false;
        }
    }

    public List<String> getJarFilenames() {
        String url = BASE_URL + "/admin";
        log.info("start getJarFilenames from server process...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, getAuthorizeHeaderValue());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);


        RestTemplate restTemplate = new RestTemplate();
        log.info("send get all jar filenames request to server");
        ResponseEntity<List<String>> response =
                restTemplate.exchange(url, GET, httpEntity, new ParameterizedTypeReference<>() {});
        log.info("receive filenames response from server");
        if (response.getStatusCode() == OK) {
            log.info("receive filenames successfully");
            return response.getBody();
        }
        log.warn("error in getJarFilenames process. code: " + response.getStatusCode());
        return new ArrayList<>();
    }

    public byte[] downloadFile(String filename) {
        String url = BASE_URL + "/" + filename;
        log.info("start download file process...");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, getAuthorizeHeaderValue());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        log.info("send download file request to server");
        ResponseEntity<byte[]> response =
                restTemplate.exchange(url, GET, httpEntity, new ParameterizedTypeReference<>() {});
        log.info("receive downloaded file response from server");
        if (response.getStatusCode() == OK) {
            log.info("download file successfully");
            return response.getBody();
        }
        log.warn("error in downloadFile process. code: " + response.getStatusCode());
        return null;
    }

    private String getAuthorizeHeaderValue() {
        return "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes());
    }

}
