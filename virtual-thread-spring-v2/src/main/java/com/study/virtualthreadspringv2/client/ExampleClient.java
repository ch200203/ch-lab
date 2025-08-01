package com.study.virtualthreadspringv2.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExampleClient {

    private final RestClient restClient;

    public Map<String, Object> getUser(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Map<String, Object> getOrders(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
