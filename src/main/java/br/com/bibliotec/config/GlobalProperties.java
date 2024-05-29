package br.com.bibliotec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class GlobalProperties {

    private static String fileDirectory;

    @Value("${file.directory}")
    public void setFileDirectory(String fileDirectory) {
        GlobalProperties.fileDirectory = fileDirectory;
    }

    public static File getDirectory() {
        return new File(fileDirectory);
    }
    
    public static String getFileDirectory() {
        return GlobalProperties.fileDirectory;
    }
}


