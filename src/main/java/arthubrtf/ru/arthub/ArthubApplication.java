package arthubrtf.ru.arthub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"authorization", "Brush", "Program", "Reference", "Tutorial", "User", "arthubrtf.ru.arthub"})
@EnableJpaRepositories({"authorization", "Brush", "Program", "Reference", "Tutorial", "User", "arthubrtf.ru.arthub"})
@ComponentScan({"authorization", "Brush", "Program", "Reference", "Tutorial", "User", "arthubrtf.ru.arthub"})
public class ArthubApplication {
	public static void main(String[] args) {
		SpringApplication.run(ArthubApplication.class, args);
	}
}

