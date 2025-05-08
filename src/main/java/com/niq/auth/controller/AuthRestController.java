package com.niq.auth.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	@GetMapping("/hello")
    public String hello() {
        return "Hello from auth: " + LocalDateTime.now();
    }
}
