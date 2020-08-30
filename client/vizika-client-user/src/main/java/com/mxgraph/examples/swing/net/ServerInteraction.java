package com.mxgraph.examples.swing.net;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class ServerInteraction {

    private final String BASE_URL = "http://localhost:8080/files";

    public boolean checkFilenameExist(String filename) {
        String url = BASE_URL + "/" + filename;
        log.info("start checking file exist....");

        RestTemplate restTemplate = new RestTemplate();
        log.info("send request to check filename exist in server");
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        log.info("receive check filename exist response from server");
        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return false;
        }
        log.warn("invalid response code in checkFilenameExist method: " + response.getStatusCode());
        return false;
    }

}
