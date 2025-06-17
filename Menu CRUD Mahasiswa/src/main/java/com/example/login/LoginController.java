package com.example.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/")
    public String showLoginForm() {
        return "login"; // mengarah ke login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        if ("admin".equals(username) && "123".equals(password)) {
            return "redirect:/menu"; 
        } else {
            return "login"; 
        }
    }
}
