package br.com.bibliotec;

import br.com.bibliotec.config.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Application {
    
    public Application(@Autowired GlobalProperties globalProperties) {
        File directory = GlobalProperties.getDirectory();
        if (!directory.exists()){
            directory.mkdirs();
        }
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
