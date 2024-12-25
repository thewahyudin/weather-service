package why.din.weatherservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import why.din.weatherservice.config.ServiceConfig;
import why.din.weatherservice.exception.WeatherServiceException;
import why.din.weatherservice.util.ResponseCode;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final ServiceConfig svcCfg;

    public void insertWeatherData(String city, String weatherData) throws WeatherServiceException {
        try{
            String redisKey = city+"_weather-data";
            redisTemplate.opsForValue().set(redisKey, weatherData);
            redisTemplate.expire(redisKey, Duration.ofMinutes(svcCfg.getRedisExpire()));
        } catch (Exception e){
            throw new WeatherServiceException(ResponseCode.REDIS_ERROR.getCode(), ResponseCode.REDIS_ERROR.getMessage(), e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        }
    }

    public String getWeatherData(String city) throws WeatherServiceException {
        try{
            Object weatherData = redisTemplate.opsForValue().get(city+"_weather-data");
            if (weatherData == null){
                return "";
            } else {
                return weatherData.toString();
            }
        } catch (Exception e){
            throw new WeatherServiceException(ResponseCode.REDIS_ERROR.getCode(), ResponseCode.REDIS_ERROR.getMessage(), e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        }
    }

}
