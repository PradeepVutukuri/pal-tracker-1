package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
@RestController
public class EnvController {

    private String port;
    private String MEMORY_LIMIT;
    private String CF_INSTANCE_INDEX;
    private String CF_INSTANCE_ADDR;

    public EnvController(@Value("${PORT:NOT SET}")  String port, @Value("${MEMORY_LIMIT:NOT SET}") String MEMORY_LIMIT,  @Value("${CF_INSTANCE_INDEX:NOT SET}") String CF_INSTANCE_INDEX, @Value("${CF_INSTANCE_ADDR:NOT SET}")  String CF_INSTANCE_ADDR) {
        this.port = port;
        this.MEMORY_LIMIT = MEMORY_LIMIT;
        this.CF_INSTANCE_INDEX = CF_INSTANCE_INDEX;
        this.CF_INSTANCE_ADDR = CF_INSTANCE_ADDR;
    }
    @GetMapping("/env")
    public Map<String, String> getEnv() {
        Map<String, String> env = new HashMap<>();
        env.put("PORT", port);
        env.put("MEMORY_LIMIT", MEMORY_LIMIT);
        env.put("CF_INSTANCE_INDEX", CF_INSTANCE_INDEX);
        env.put("CF_INSTANCE_ADDR", CF_INSTANCE_ADDR);
        return env;
    }
}
