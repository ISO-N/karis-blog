package top.kariscode.karis_blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    // “健康检查”接口
    @GetMapping("/api/ping")
    public String ping(){
        return "pong";
    }
}
