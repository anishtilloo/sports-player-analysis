package com.sports_analysis_app.sports_analysis_app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/api/hello")
    public String firstApi() {
        return "Hello this is my first spring boot api";
    }
}

