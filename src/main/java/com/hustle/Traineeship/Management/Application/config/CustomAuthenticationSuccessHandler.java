package com.hustle.Traineeship.Management.Application.config;

import com.hustle.Traineeship.Management.Application.model.User;
import com.hustle.Traineeship.Management.Application.model.Role;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final StudentsService studentsService;

    // Constructor injection
    public CustomAuthenticationSuccessHandler(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        Role role = user.getRole();

        switch (role) {
            case STUDENT:
                if (!studentsService.studentProfileExists(user.getId())) {
                    response.sendRedirect("/student/create_profile");
                } else {
                    response.sendRedirect("/student/profile");
                }
                break;
            case COMPANY:
                response.sendRedirect("/company/create_profile");
                break;
            case PROFESSOR:
                response.sendRedirect("/professor/create_profile");
                break;
            case COMMITTEE:
                response.sendRedirect("/committee/create_profile");
                break;
            default:
                response.sendRedirect("/");
                break;
        }
    }
}
