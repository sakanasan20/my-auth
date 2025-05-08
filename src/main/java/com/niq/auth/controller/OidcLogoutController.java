package com.niq.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OidcLogoutController {

	@GetMapping("/oidc/logout")
    public String logout(HttpServletRequest request,
                         @RequestParam(name = "post_logout_redirect_uri", required = false) String redirectUri)
            throws ServletException {

        request.logout();
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 清除 HttpSession
        }
        return "redirect:" + (redirectUri != null ? redirectUri : "/");
    }
}
