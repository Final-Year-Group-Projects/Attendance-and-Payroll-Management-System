package com.attendance.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    private final BuildProperties buildProperties;

    public TestController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/test")
    public Map<String, String> test() {
        Map<String, String> info = new HashMap<>();
        info.put("artifact", buildProperties.getArtifact());
        info.put("version", buildProperties.getVersion());
        info.put("buildTime", buildProperties.getTime().toString());
        return info;
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to the Attendance Service!";
    }
}