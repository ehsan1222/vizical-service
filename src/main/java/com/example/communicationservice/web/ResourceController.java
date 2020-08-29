package com.example.communicationservice.web;

import com.example.communicationservice.services.StorageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(
        path = "/files"
)
@Log4j2
public class ResourceController {

    @Autowired
    private StorageService storageService;

    @GetMapping
    public ResponseEntity<List<String>> listOfFiles() {
        List<String> listOfFiles = storageService.listOfFiles();
        return new ResponseEntity<>(listOfFiles, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<?> userUploadFile(@RequestParam("file") MultipartFile file) {
        storageService.userSaveFile(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<String>> listOfAdminFiles() {
        List<String> listOfAdminFiles = storageService.getAdminList();
        return new ResponseEntity<>(listOfAdminFiles, HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> adminUploadFile(@RequestParam("file") MultipartFile file) {
        storageService.adminSaveFile(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
        Resource file = storageService.getFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }

    @GetMapping("/{fileName}/check")
    public ResponseEntity<?> checkFileExist(@PathVariable("fileName") String fileName) {
        boolean checkExist = storageService.checkFileExist(fileName);
        if (checkExist) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
