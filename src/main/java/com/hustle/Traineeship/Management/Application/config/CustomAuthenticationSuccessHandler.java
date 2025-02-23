package com.hustle.Traineeship.Management.Application.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.hustle.Traineeship.Management.Application.model.User;
import com.hustle.Traineeship.Management.Application.model.Role;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // 1. Retrieve your User from the Principal
        User user = (User) authentication.getPrincipal();
        Role role = user.getRole();  // e.g. STUDENT, COMPANY, etc.

        // 2. Redirect based on role
        switch (role) {
            case STUDENT:
                response.sendRedirect("/student/profile");
                break;
            case COMPANY:
                response.sendRedirect("/company/profile");
                break;
            case PROFESSOR:
                response.sendRedirect("/professor/profile");
                break;
            case COMMITTEE:
                response.sendRedirect("/committee/profile");
                break;
            default:
                // Fallback URL
                response.sendRedirect("/");
                break;
        }
    }
}
