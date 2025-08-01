package com.study.virtualthreadspringv2;

import com.study.virtualthreadspringv2.client.ExampleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/example")
public class ExampleController {

    private final ExampleClient exampleClient;

    @GetMapping("/users")
    public String getUser() {
        String url = "http://localhost:9090/load/users";
        return exampleClient.getUser(url).toString();
    }
}
