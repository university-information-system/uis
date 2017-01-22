package at.ac.tuwien.inso.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("demo")
public class DemoDataInitConfig {

    @Autowired
    private DataInitializer dataInitializer;

    @Bean
    CommandLineRunner initialize() {
        return String -> dataInitializer.initialize();
    }
}
