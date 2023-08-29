package shop.jbshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class JbshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JbshopApplication.class, args);
	}

}
