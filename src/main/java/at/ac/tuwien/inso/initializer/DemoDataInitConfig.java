package at.ac.tuwien.inso.initializer;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;

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
