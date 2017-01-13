package at.ac.tuwien.inso;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cache.annotation.*;
import org.springframework.scheduling.annotation.*;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class UisApplication {

	public static void main(String[] args) {
		SpringApplication.run(UisApplication.class, args);
	}
}
