package com.shantanu.journalApp.service;

import com.shantanu.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;  // External API calls ke liye Spring ka HTTP client

    @Autowired
    private RedisService redisService;

    // application.yml se API credentials
    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${app.redis.ttl:300}")
    private Long cacheTtl;


    /**
     * City ka weather fetch - pehle cache check, phir API call
     * @param city - City name
     * @return WeatherResponse
     */
    public WeatherResponse getWeather(String city) {
        String key = "weather_of_" + city;  // Redis key banao
        // Pehle cache mein check karo
        WeatherResponse weatherResponse = redisService.get(key, WeatherResponse.class);
        if(weatherResponse != null) {
            // Cache hit - Redis se mil gaya
            return weatherResponse;
        }
        else {
            // Cache miss - External API call karo
            // URL mein CITY aur ACCESS_KEY replace karo bcz apiurl yml has placeholders for abstraction
            String finalAPI = apiUrl.replace("CITY", city).replace("ACCESS_KEY", apiKey);

            // API call karke response fetch
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();

            // Response ko cache mein save for next same calls (300 sec = 5 min TTL)
            if(body != null) {
                redisService.set(key, body, cacheTtl);
            }

            return body;
        }
    }
}
