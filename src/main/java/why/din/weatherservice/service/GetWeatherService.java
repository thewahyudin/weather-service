package why.din.weatherservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import why.din.weatherservice.config.ServiceConfig;
import why.din.weatherservice.dto.GetWeatherRequest;
import why.din.weatherservice.dto.GetWeatherResponse;
import why.din.weatherservice.exception.WeatherServiceException;
import why.din.weatherservice.util.ResponseCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class GetWeatherService {

    @Autowired
    private WeatherAPIService weatherAPIService;
    @Autowired
    private WeatherInterpretationService weatherInterpretationService;
    @Autowired
    private RedisService redisService;
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private final ServiceConfig svcCfg;

    public ResponseEntity<GetWeatherResponse> getWeather(GetWeatherRequest req) {
        String city = req.getCity();
        boolean isInterpreted = req.isInterpreted();

        String res_code = "";
        String res_msg = "";
        String status = ResponseCode.FAILED.getMessage();
        GetWeatherResponse.WeatherDetails weather;
        Map<String, String> interpretation = new HashMap<>();
        Map<String, Object> detail = new HashMap<>();
        LocalDateTime startProcess;
        String weatherData = "";
        boolean insertToRedis = false;

        // LogsId
        LocalDateTime startTime = LocalDateTime.now();
        String microseconds = String.format("%06d", startTime.getNano() / 1000);
        String logsId = String.format("%s%s", startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), microseconds);

        try {
            String reqStr = gson.toJson(req);
            log.info(String.format("%s - Get weather service started. Inc Req: %s", logsId, reqStr));
            startProcess = LocalDateTime.now();
            if ("YES".equals(svcCfg.getRedisUsage())) {
                weatherData = redisService.getWeatherData(city.toUpperCase());
                if (weatherData.isEmpty()) {
                    log.info(String.format("%s - No data stored in Redis. Trying to access it from WeatherAPI", logsId));
                    weatherData = weatherAPIService.sendRequest(city);
                    insertToRedis = true;
                } else {
                    log.info(String.format("%s - Weather data is exist in Redis", logsId));
                }
            } else {
                weatherData = weatherAPIService.sendRequest(city);
                insertToRedis = true;
            }

            if (weatherData != null) {
                log.info(String.format("%s - Weather data retrieved. Time elapsed: %d ms",
                        logsId, Duration.between(startProcess, LocalDateTime.now()).toMillis()));
                Map<String, Object> weatherDataMap = gson.fromJson(weatherData, Map.class);
                if (weatherDataMap.get("Error") != null) {
                    Map<String, String> weatherError = gson.fromJson(weatherDataMap.get("Error").toString(), Map.class);
                    res_code = ResponseCode.BACKEND_ERROR.getCode();
                    res_msg = String.format("%s %d: %s", ResponseCode.BACKEND_ERROR.getMessage(), weatherError.get("code"), weatherError.get("message"));
                } else {
                    detail = weatherDataMap;
                    if (isInterpreted){
                        interpretation = weatherInterpretationService.interpret(weatherDataMap);
                    }
                    res_code = ResponseCode.SUCCESS.getCode();
                    res_msg = ResponseCode.SUCCESS.getMessage();
                    status = ResponseCode.SUCCESS.getMessage();
                    if (svcCfg.getRedisUsage().equals("YES") && insertToRedis) {
                        startProcess = LocalDateTime.now();
                        redisService.insertWeatherData(city.toUpperCase(), weatherData);
                        log.info(String.format("%s - Weather data stored to Redis. Time elapsed: %d ms",
                                logsId, Duration.between(startProcess, LocalDateTime.now()).toMillis()));
                    }
                }
            }

        } catch (WeatherServiceException e) {
            res_code = e.getErrorCode();
            res_msg = e.getErrorMessage();
            log.error(String.format("%s - WeatherServiceException %s: %s", logsId, e.getOrigin(), e.getDetail()));
        } catch (Exception e) {
            res_code = ResponseCode.GENERAL_ERROR.getCode();
            res_msg = ResponseCode.GENERAL_ERROR.getMessage();
            log.error(String.format("%s - GeneralException: %s", logsId, e.getMessage()));
        }

        weather = new GetWeatherResponse().new WeatherDetails(interpretation, detail);
        GetWeatherResponse response = new GetWeatherResponse(res_code, res_msg, status, weather);

        String resStr = gson.toJson(response);
        log.info(String.format("%s - Get weather service finished. Out Res: %s. Time elapsed: %d ms",
                logsId, resStr.length() > 300 ? resStr.substring(0, 300)+ "..." : resStr, Duration.between(startTime, LocalDateTime.now()).toMillis()));

        return ResponseEntity.ok(response);
    }

}
