package com.attendance.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    private BuildProperties buildProperties;

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/test")
    public Map<String, String> test() {
        Map<String, String> info = new HashMap<>();
        info.put("artifact", buildProperties != null ? buildProperties.getArtifact() : "unknown");
        info.put("version", buildProperties != null ? buildProperties.getVersion() : "unknown");
        info.put("buildTime", buildProperties != null && buildProperties.getTime() != null ? buildProperties.getTime().toString() : "unknown");
        return info;
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to the Attendance Service!";
    }
}