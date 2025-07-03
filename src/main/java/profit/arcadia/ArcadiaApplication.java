package profit.arcadia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
<<<<<<<< HEAD:src/main/java/profit/arcadia/ArcadiaApplication.java
public class ArcadiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArcadiaApplication.class, args);
========
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
>>>>>>>> a64fd986fca6456541b3baac03c26d743b94a974:src/main/java/profit/arcadia/auth/Main.java
	}
}