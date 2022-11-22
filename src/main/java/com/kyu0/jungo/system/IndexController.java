package com.kyu0.jungo.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/test/user")
    public String testUser() {
        return "test_user";
    }

    @GetMapping("/test/admin")
    public String testAdmin() {
        return "test_admin";
    }
}
