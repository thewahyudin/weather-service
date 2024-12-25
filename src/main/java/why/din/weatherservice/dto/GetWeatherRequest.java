package why.din.weatherservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.Getter;
import lombok.Setter;
import why.din.weatherservice.validation.BooleanValue;

@Getter
@Setter
public class GetWeatherRequest {
    @NotBlank(message = "Fill the city name")
    private String city;

    @BooleanValue
    private Boolean isInterpreted;

    public GetWeatherRequest(String city, Boolean isInterpreted) {
        this.city = city;
        this.isInterpreted = isInterpreted;
    }
}
