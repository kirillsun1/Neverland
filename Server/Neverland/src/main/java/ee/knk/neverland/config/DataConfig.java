package ee.knk.neverland.config;


import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;
import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("ee.knk.neverland")
@EnableWebMvc
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("ee.knk.neverland.repository")
public class DataConfig {


    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("1000KB");
        factory.setMaxRequestSize("1000KB");
        return factory.createMultipartConfig();
    }


}