package com.sweng.InteractiveStory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/index"})
    public String indexPage() {
        return "index"; // Restituisce index.html da src/main/resources/templates
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Restituisce login.html da src/main/resources/templates
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Restituisce register.html da src/main/resources/templates
    }
}
