package com.study.virtualthreadspringv2;

import com.study.virtualthreadspringv2.client.ExampleClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/example")
public class ExampleController {

    private final ExampleClient exampleClient;

    @GetMapping("/users")
    public String getUser() {
        log.info("getUser() starts in thread : {}", Thread.currentThread());
        String url1 = "http://localhost:9090/load/users";
        String url2 = "http://localhost:9090/load/orders";

        // CompletableFuture를 사용하여 두 API 호출을 비동기적으로 시작
        CompletableFuture<Map<String, Object>> userFuture = CompletableFuture.supplyAsync(
                () -> exampleClient.getUser(url1)
        );

        CompletableFuture<Map<String, Object>> ordersFuture = CompletableFuture.supplyAsync(
                () -> exampleClient.getOrders(url2)
        );

        // 두 작업이 모두 완료될 때까지 기다리고 결과를 합칩니다.
        var result = CompletableFuture.allOf(userFuture, ordersFuture).join();

        try {
            Map<String, Object> user = userFuture.get();
            Map<String, Object> order = ordersFuture.get();
            log.info("user : {}", result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return "complete";
    }
}
