package com.firework.studyapiversioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping(path = "/sample/{id}", version = "1.0")
    public String getSample(@PathVariable Long id) {
        return "V1 : " + id.toString();
    }

    @GetMapping(path = "/sample/{id}", version = "1.1+")
    public String getSampleV2(@PathVariable Long id) {
        return "V2 : " + id.toString();
    }

    @GetMapping(path = "/sample/{id}", version = "3")
    public String getSampleV3(@PathVariable Long id) {
        return "V3 : " + id.toString();
    }
}
