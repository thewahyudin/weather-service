package why.din.weatherservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "be")
@Getter
@Setter
public class WeatherAPIConfig {
    private String url;
    private String apiKey;
    private long timeout;
}
