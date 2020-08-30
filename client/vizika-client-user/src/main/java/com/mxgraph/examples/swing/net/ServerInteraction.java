package com.mxgraph.examples.swing.net;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Log4j2
public class ServerInteraction {

    public static final String AUTHORIZATION_NAME = "Authorization";
    public static final String AUEHTNTICATE_PREFIX = "Basic ";
    private final String BASE_URL = "http://localhost:8080/files";

    public boolean checkFilenameExist(String filename) {
        String url = BASE_URL + "/" + filename;
        log.info("start checking file exist....");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        String encodedAuthHeader = getEncodedAuthenticationHeader();
        headers.add(AUTHORIZATION_NAME, AUEHTNTICATE_PREFIX + encodedAuthHeader);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        log.info("send request to check filename exist to server");
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        log.info("receive check filename exist response from server");
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("receive checkFilenameExist successfully");
            return true;
        } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.info("no content response in checkFilenameExist");
            return false;
        }
        log.warn("invalid response code in checkFilenameExist method: " + response.getStatusCode());
        return false;
    }

    public List<String> getJarFilenames() {
        String url = BASE_URL + "/admin";
        log.info("start getJarFilenames from server process...");

        HttpHeaders headers = new HttpHeaders();
        String encodedAuthHeader = getEncodedAuthenticationHeader();
        headers.add(AUTHORIZATION_NAME, AUEHTNTICATE_PREFIX + encodedAuthHeader);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);


        RestTemplate restTemplate = new RestTemplate();
        log.info("send get all jar filenames request to server");
        ResponseEntity<List<String>> response =
                restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});
        log.info("receive filenames response from server");
        if (response.getStatusCode() == HttpStatus.OK) {
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
        String encodedAuthHeader = getEncodedAuthenticationHeader();
        headers.add(AUTHORIZATION_NAME, AUEHTNTICATE_PREFIX + encodedAuthHeader);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        log.info("send download file request to server");
        ResponseEntity<byte[]> response =
                restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});
        log.info("receive downloaded file response from server");
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("download file successfully");
            return response.getBody();
        }
        log.warn("error in downloadFile process. code: " + response.getStatusCode());
        return null;
    }

    private String getEncodedAuthenticationHeader() {
        String username = "user";
        String password = "password";
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

}
