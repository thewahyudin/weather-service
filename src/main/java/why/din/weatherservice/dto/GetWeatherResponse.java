package why.din.weatherservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GetWeatherResponse {
    private String res_code;
    private String res_msg;
    private String status;
    private WeatherDetails weather;

    public GetWeatherResponse(String res_code, String res_msg, String status, WeatherDetails weather) {
        this.res_code = res_code;
        this.res_msg = res_msg;
        this.status = status;
        this.weather = weather;
    }

    public GetWeatherResponse() {

    }

    @Getter
    @Setter
    public class WeatherDetails{
        private Map<String, String> interpretation;
        private Map<String, Object> detail;

        public WeatherDetails(Map<String, String> interpretation, Map<String, Object> detail) {
            this.interpretation = interpretation;
            this.detail = detail;
        }
    }


}
