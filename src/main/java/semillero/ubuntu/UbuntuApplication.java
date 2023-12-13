package semillero.ubuntu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "semillero.ubuntu")
public class UbuntuApplication {

	public static void main(String[] args) {
		SpringApplication.run(UbuntuApplication.class, args);
	}

}
