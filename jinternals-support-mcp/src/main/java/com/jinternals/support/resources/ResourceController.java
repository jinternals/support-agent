package com.jinternals.support.resources;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ResourceController {

    @GetMapping("/mcp/resources/meeting-types")
    public List<String> getMeetingTypes() {
        return List.of("1:1", "Group", "Review", "Project Sync");
    }
}

