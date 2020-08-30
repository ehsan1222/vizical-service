package com.example.communicationservice.services;

import com.example.communicationservice.exceptions.FileNotExistException;
import com.example.communicationservice.models.ActionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class StorageService {

    @Value("${userDirectory}")
    private String userDirectory;

    @Value("${adminDirectory}")
    private String adminDirectory;

    private final LogService logService;

    public StorageService(LogService logService) {
        this.logService = logService;
    }

    public List<String> listOfFiles() {
        List<String> fileNames = new ArrayList<>(getUserList());
        fileNames.addAll(getAdminList());
        logService.addNewLog(ActionType.FILE_SHOW);
        return fileNames;
    }

    private List<String> getUserList() {
        List<String> fileNames = new ArrayList<>();
        try {
            if (new File(userDirectory).isDirectory()) {
                Files.walk(Paths.get(userDirectory))
                        .filter(Files::isRegularFile)
                        .forEach(path -> fileNames.add(path.getFileName().toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    public List<String> getAdminList() {
        List<String> fileNames = new ArrayList<>();

        try {
            if (new File(adminDirectory).isDirectory()) {
                Files.walk(Paths.get(adminDirectory))
                        .filter(Files::isRegularFile)
                        .forEach(path -> fileNames.add(path.getFileName().toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logService.addNewLog(ActionType.FILE_SHOW);
        return fileNames;
    }

    public void userSaveFile(MultipartFile file) {
        checkFileExtensionIsPermitted(file, "mxe");

        String absolutePath = createDirectoryIfNotExist(userDirectory);

        saveFile(file, absolutePath);
        logService.addNewLog(ActionType.MXE_FILE_UPLOAD);
    }

    public void adminSaveFile(MultipartFile file) {
        checkFileExtensionIsPermitted(file, "jar");

        String absolutePath = createDirectoryIfNotExist(adminDirectory);

        saveFile(file, absolutePath);
        logService.addNewLog(ActionType.JAR_FILE_UPLOAD);
    }

    public Resource getFile(String fileName) {
        String fileExtension = getFileExtension(fileName);
        if (fileExtension.equals("mxe")) {
            Resource fileFromLocalStorage = getFile(fileName, userDirectory);
            logService.addNewLog(ActionType.FILE_DOWNLOAD);
            return fileFromLocalStorage;
        } else if (fileExtension.equals("jar")) {
            Resource fileFromLocalStorage = getFile(fileName, adminDirectory);
            logService.addNewLog(ActionType.FILE_DOWNLOAD);
            return fileFromLocalStorage;
        } else {
            throw new IllegalArgumentException("invalid file extension " + fileExtension);
        }
    }

    public boolean checkFileExist(String fileName) {
        String fileExtension = getFileExtension(fileName);
        if (fileExtension.equals("mxe")) {
            boolean isExisted = checkFileExist(fileName, userDirectory);
            logService.addNewLog(ActionType.CHECK_FILE_EXIST);
            return isExisted;
        } else if (fileExtension.equals("jar")) {
            boolean isExisted = checkFileExist(fileName, adminDirectory);
            logService.addNewLog(ActionType.CHECK_FILE_EXIST);
            return isExisted;
        } else {
            return false;
        }
    }

    private boolean checkFileExist(String fileName, String baseDirectory) {
        String absolutePath =
                new File(baseDirectory).getAbsolutePath().concat(File.separator).concat(fileName);

        return new File(absolutePath).isFile();
    }

    private void checkFileExtensionIsPermitted(MultipartFile file, String... extensions) {
        // TODO: change check file extension algorithm to find type of file with not extension
        if(file == null || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("invalid file");
        }
        int lastIndexOf = file.getOriginalFilename().lastIndexOf(".");
        if (lastIndexOf != -1 && lastIndexOf < file.getOriginalFilename().length()) {
            String fileExtension = file.getOriginalFilename().substring(lastIndexOf + 1);

            long numberOfPermittedExtensions = Stream.of(extensions)
                    .filter(s -> s.equals(fileExtension))
                    .count();
            // check file extension is valid
            if (numberOfPermittedExtensions > 0) {
                return;
            }
        }
        throw new IllegalArgumentException("invalid file");
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new IllegalArgumentException("invalid filename " + fileName);
        }
        return fileName.substring(dotIndex + 1);
    }

    private void saveFile(MultipartFile file, String absolutePath) {
        if (file == null || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("invalid file");
        }
        String transferFilePath = absolutePath.concat(File.separator).concat(file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), Path.of(transferFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createDirectoryIfNotExist(String path) {
        File currentDirectory = new File(path);
        if (!currentDirectory.isDirectory()) {
            boolean result = currentDirectory.mkdirs();
        }
        return currentDirectory.getAbsolutePath();
    }


    private Resource getFile(String fileName, String baseDirectory) {
        String downloadFilePath =
                new File(baseDirectory).getAbsolutePath().concat(File.separator).concat(fileName);

        File file = new File(downloadFilePath);
        if (!file.isFile()) {
            throw new FileNotExistException("fileName not found " + fileName);
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return new InputStreamResource(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
