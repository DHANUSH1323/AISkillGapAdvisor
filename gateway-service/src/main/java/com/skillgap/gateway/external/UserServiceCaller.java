package com.skillgap.gateway.external;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class UserServiceCaller {

    private final RestClient restClient;

    public UserServiceCaller() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8082")  // user-service URL
                .build();
    }

    public Map<String, Object> getMe(String token) {
        return restClient.get()
                .uri("/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(Map.class);
    }
}
