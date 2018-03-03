package ee.knk.neverland;

import ee.knk.neverland.config.AppInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NeverlandApplication {
    public static void main(String[] args) {
        SpringApplication.run(new Class<?>[] {NeverlandApplication.class, AppInitializer.class}, args);
    }

}
