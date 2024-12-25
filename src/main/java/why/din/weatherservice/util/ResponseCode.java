package why.din.weatherservice.util;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS("00", "SUCCESS"),
    FAILED("", "FAILED"),
    BACKEND_ERROR("11", "(BACKEND) "),
    REDIS_ERROR("41", "REDIS ERROR"),
    CONNECTION_ERROR("91", "CONNECTION ERROR TO BACKEND"),
    TIMEOUT_ERROR("92", "TIMEOUT WHILE RETRIEVING DATA FROM BACKEND"),
    GENERAL_ERROR("99", "GENERAL ERROR");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
