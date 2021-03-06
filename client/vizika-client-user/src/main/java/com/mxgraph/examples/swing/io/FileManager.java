package com.mxgraph.examples.swing.io;

import lombok.extern.log4j.Log4j2;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//@Log4j2
public class FileManager {

    private final String BASE_DOWNLOAD_FILE = new File(".").getAbsolutePath().concat(File.separator).concat("updates");

    Logger logger = Logger.getLogger(FileManager.class);

    public void saveFile(byte[] file, String filename) {
        if (!new File(BASE_DOWNLOAD_FILE).isDirectory()) {
            new File(BASE_DOWNLOAD_FILE).mkdirs();
        }

        try {
            Files.write(Paths.get(BASE_DOWNLOAD_FILE.concat(File.separator).concat(filename)), file);
            logger.info("file save successfully");
        } catch (IOException e) {
            logger.warn("error in write file, error = " + e.getMessage());
        }
    }

}
