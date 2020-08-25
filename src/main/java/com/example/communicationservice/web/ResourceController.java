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

@RestController
@RequestMapping(
        path = "/file"
)
@Log4j2
public class ResourceController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/user/upload")
    public void textUploadFile(@RequestParam("file") MultipartFile file) {
        storageService.userUploadFile(file);
    }

    @PostMapping("/admin/upload")
    public void jarUploadFile(@RequestParam("file") MultipartFile file) {
        storageService.adminUploadFile(file);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
        Resource file = storageService.download(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }

    @GetMapping("/{fileName}/check")
    public ResponseEntity<?> checkFileExist(@PathVariable("fileName") String fileName) {
        boolean checkExist = storageService.isFileExist(fileName);
        if (checkExist) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
