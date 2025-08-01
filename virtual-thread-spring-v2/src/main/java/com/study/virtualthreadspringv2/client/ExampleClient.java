package com.study.virtualthreadspringv2.client;

import com.study.virtualthreadspringv2.annotation.UseVirtualThread;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExampleClient {

    private final RestClient restClient;

    @UseVirtualThread
    public Map<String, Object> getUser(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
