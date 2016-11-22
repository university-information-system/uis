package at.ac.tuwien.inso.integration_tests;

import org.mockito.*;
import org.springframework.context.annotation.*;
import org.springframework.mail.javamail.*;

@Configuration
public class TestConfig {

    @Bean
    public JavaMailSender mailSender() {
        return Mockito.mock(JavaMailSender.class);
    }
}
