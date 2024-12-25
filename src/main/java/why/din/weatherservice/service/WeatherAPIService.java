package why.din.weatherservice.service;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import why.din.weatherservice.config.WeatherAPIConfig;
import why.din.weatherservice.exception.WeatherServiceException;
import why.din.weatherservice.util.ResponseCode;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

@RequiredArgsConstructor
@Service
public class WeatherAPIService {

    private final OkHttpClient client;
    private final WeatherAPIConfig cfg;

    public String sendRequest(String city) throws WeatherServiceException {
        try {
            String url = String.format("%scurrent.json?key=%s&q=%s&aqi=yes", cfg.getUrl(), cfg.getApiKey(), city);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (ConnectException e) {
            throw new WeatherServiceException(ResponseCode.CONNECTION_ERROR.getCode(), ResponseCode.CONNECTION_ERROR.getMessage(), e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        } catch (SocketTimeoutException e) {
            throw new WeatherServiceException(ResponseCode.TIMEOUT_ERROR.getCode(), ResponseCode.TIMEOUT_ERROR.getMessage(), e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        } catch (Exception e) {
            throw new WeatherServiceException(ResponseCode.GENERAL_ERROR.getCode(), ResponseCode.GENERAL_ERROR.getMessage(), e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        }
    }
}
