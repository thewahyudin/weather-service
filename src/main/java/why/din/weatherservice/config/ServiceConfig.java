package why.din.weatherservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "svc")
@Getter
@Setter
public class ServiceConfig {
    private String redisUsage;
    private String redisExpire;

    public long getRedisExpire() {
        return Long.parseLong(redisExpire);
    }
}
