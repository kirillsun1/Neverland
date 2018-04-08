package ee.knk.neverland.config;


import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import javax.servlet.MultipartConfigElement;

@Configuration
@ComponentScan("ee.knk.neverland")
@EnableWebMvc
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("ee.knk.neverland.repository")
public class DataConfig {


    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5000KB");
        factory.setMaxRequestSize("5000KB");
        return factory.createMultipartConfig();
    }


}