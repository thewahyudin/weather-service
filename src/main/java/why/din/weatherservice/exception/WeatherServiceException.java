package why.din.weatherservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherServiceException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    private String detail;
    private String origin;
    private String carriedData;

    public WeatherServiceException(String errorCode, String errorMessage, String detail, String origin, String carriedData){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.detail = detail;
        this.origin = origin;
        this.carriedData = carriedData;
    }

    public WeatherServiceException(String message) {
        super(message);
    }
}
