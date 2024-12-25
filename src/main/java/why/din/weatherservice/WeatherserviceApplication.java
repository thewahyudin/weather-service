package why.din.weatherservice;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import why.din.weatherservice.config.WeatherAPIConfig;

@SpringBootApplication
@EnableAutoConfiguration
public class WeatherserviceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		for (DotenvEntry entry : dotenv.entries()) {
			System.setProperty(entry.getKey(), entry.getValue());
		}

		SpringApplication.run(WeatherserviceApplication.class, args);
	}

}
