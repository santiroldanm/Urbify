package com.example.urbify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/public/login")
    public String showLoginForm() {
        return "public/login";
    }

}

