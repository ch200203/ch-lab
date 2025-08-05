package com.study.virtualthreadspringv2.client;

import com.study.virtualthreadspringv2.annotation.UseVirtualThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExampleClient {

    private final RestClient restClient;

    @UseVirtualThread
    public Map<String, Object> getUser(String url) {
        log.debug("Is getUser Virtual Thread: {}", Thread.currentThread().isVirtual());
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @UseVirtualThread
    public Map<String, Object> getOrders(String url) {
        log.debug("Is getOrders Virtual Thread: {}", Thread.currentThread().isVirtual());
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
