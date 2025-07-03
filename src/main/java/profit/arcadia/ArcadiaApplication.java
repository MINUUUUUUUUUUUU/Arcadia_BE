package profit.arcadia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ArcadiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArcadiaApplication.class, args);
	}
}