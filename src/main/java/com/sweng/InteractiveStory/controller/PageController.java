package com.sweng.InteractiveStory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "Login Page Placeholder";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "Register Page Placeholder";
    }
}
