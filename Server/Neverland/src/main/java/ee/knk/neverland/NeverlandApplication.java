package ee.knk.neverland;

import ee.knk.neverland.config.AppInitializer;
import ee.knk.neverland.config.DataConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;


@SpringBootApplication
public class NeverlandApplication {
    public static void main(String[] args) {
        SpringApplication.run(new Class<?>[] {NeverlandApplication.class, AppInitializer.class}, args);
    }

}
