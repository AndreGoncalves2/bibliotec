package br.com.bibliotec;

import br.com.bibliotec.config.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.io.File;

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "file:C:/bibliotec.properties", ignoreResourceNotFound = true), // Windows
        @PropertySource(value = "file:/etc/bibliotec.properties", ignoreResourceNotFound = true) // Linux
})
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
