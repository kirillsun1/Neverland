package ee.knk.neverland;

import ee.knk.neverland.config.AppInitializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class NeverlandApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(NeverlandApplication.class, args);
    }

}
