package at.ac.tuwien.inso;

import org.mockito.*;
import org.springframework.context.annotation.*;
import org.springframework.mail.*;

@Configuration
public class TestConfig {

    @Bean
    public MailSender mailSender() {
        return Mockito.mock(MailSender.class);
    }
}
