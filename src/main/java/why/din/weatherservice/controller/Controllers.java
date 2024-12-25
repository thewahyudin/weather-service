package why.din.weatherservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import why.din.weatherservice.dto.GetWeatherRequest;
import why.din.weatherservice.dto.GetWeatherResponse;
import why.din.weatherservice.service.GetWeatherService;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/")
public class Controllers {

    private final GetWeatherService getWeatherService;

    @PostMapping("get-weather")
    public ResponseEntity<GetWeatherResponse> getWeather (@Valid @RequestBody GetWeatherRequest getWeatherRequest){
        return getWeatherService.getWeather(getWeatherRequest);
    }

}
