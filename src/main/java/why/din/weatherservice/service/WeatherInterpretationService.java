package why.din.weatherservice.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherInterpretationService {
    public Map<String, String> interpret(Map<String, Object> weatherData) {
        Map<String, String> interpretation = new HashMap<>();
        Map<String, Object> location = (Map<String, Object>) weatherData.get("location");
        Map<String, Object> current = (Map<String, Object>) weatherData.get("current");

        // Location
        String locationName = (String) location.get("name");
        String region = (String) location.get("region");
        String country = (String) location.get("country");

        // Weather
        Double tempC = (Double) current.get("temp_c");
        Double feelsLikeC = (Double) current.get("feelslike_c");
        Double tempF = (Double) current.get("temp_f");
        Double feelsLikeF = (Double) current.get("feelslike_f");
        String conditionText = (String) ((Map<String, Object>) current.get("condition")).get("text");
        Double windSpeed = (Double) current.get("wind_kph");
        String windDir = (String) current.get("wind_dir");
        Double humidity = (Double) current.get("humidity");
        Double pressure = (Double) current.get("pressure_mb");
        Double precipMM = (Double) current.get("precip_mm");
        Double uvIndex = (Double) current.get("uv");
        Map<String, Object> airQuality = (Map<String, Object>) current.get("air_quality");
        Double pm25 = (Double) airQuality.get("pm2_5");

        interpretation.put("Location", String.format("%s, %s, %s", locationName, region, country));
        interpretation.put("Temperature", String.format("%.1f°C (%.1f°F), but it feels like %.1f°C (%.1f°F)", tempC, tempF, feelsLikeC, feelsLikeF));
        interpretation.put("Condition", conditionText);
        interpretation.put("Wind", String.format("%s at %.1f kph speed from %s", getWindCondition(windSpeed), windSpeed, windDir));
        interpretation.put("Humidity", String.format("%.0f%% which is %s", humidity, getHumidityCondition(humidity)));
        interpretation.put("Pressure", String.format("%.1f mbar which means %s", pressure, getPressureCondition(pressure)));
        interpretation.put("Precipitation", getPrecipitationCondition(precipMM));
        interpretation.put("UV Index", String.format("%.1f, which is %s", uvIndex, getUVIndexLevel(uvIndex)));
        interpretation.put("Air Quality", String.format("PM2.5 %.2f µg/m³, which indicates %s", pm25, getAirQualityLevel(pm25)));
        return interpretation;
    }

    private String getWindCondition(double windSpeed) {
        String windCondition;
        if (windSpeed <= 5) {
            windCondition = "Calm";
        } else if (windSpeed <= 15) {
            windCondition = "Light breeze";
        } else if (windSpeed <= 30) {
            windCondition = "Moderate breeze";
        } else if (windSpeed <= 50) {
            windCondition = "Strong winds";
        } else {
            windCondition = "Gale-force winds";
        }
        return windCondition;
    }

    private String getHumidityCondition(double humidity) {
        String humidityCondition = "";
        if (humidity <= 30) {
            humidityCondition = "low humidity, air feels dry.";
        } else if (humidity <= 60) {
            humidityCondition = "moderate humidity, comfortable.";
        } else if (humidity <= 80) {
            humidityCondition = "high humidity, may feel warmer than actual temperature.";
        } else {
            humidityCondition = "very high humidity, sticky and uncomfortable.";
        }
        return humidityCondition;
    }

    private String getPressureCondition(double pressure) {
        String pressureCondition = "";
        if (pressure >= 1013) {
            pressureCondition = "normal pressure, stable weather.";
        } else if (pressure >= 1000) {
            pressureCondition = "low pressure, mild weather changes possible.";
        } else {
            pressureCondition = "very low pressure, expect stormy or rainy weather.";
        }
        return pressureCondition;
    }

    private String getPrecipitationCondition(double precipMM){
        String precipCondition = "";
        if (precipMM > 0) {
            precipCondition = String.format("There is precipitation of %.2f mm", precipMM);
        } else {
            precipCondition = "No precipitation is expected.";
        }
        return precipCondition;
    }

    private String getUVIndexLevel(Double uvIndex) {
        if (uvIndex <= 2) {
            return "low";
        } else if (uvIndex <= 5) {
            return "moderate";
        } else if (uvIndex <= 7) {
            return "high";
        } else {
            return "very high";
        }
    }

    private String getAirQualityLevel(Double pm25) {
        if (pm25 <= 12) {
            return "good";
        } else if (pm25 <= 35.4) {
            return "moderate";
        } else if (pm25 <= 55.4) {
            return "unhealthy for sensitive groups";
        } else if (pm25 <= 150.4) {
            return "unhealthy";
        } else {
            return "very unhealthy";
        }
    }

}
