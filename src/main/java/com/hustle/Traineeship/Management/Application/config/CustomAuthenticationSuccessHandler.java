package com.hustle.Traineeship.Management.Application.config;

import com.hustle.Traineeship.Management.Application.model.User;
import com.hustle.Traineeship.Management.Application.model.Role;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import com.hustle.Traineeship.Management.Application.service.ProfessorService;
import com.hustle.Traineeship.Management.Application.service.CompanyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final StudentsService studentsService;
    private final ProfessorService professorService;
    private final CompanyService companyService;

    // Constructor injection for both services
    public CustomAuthenticationSuccessHandler(StudentsService studentsService, ProfessorService professorsService, CompanyService companyService) {
        this.studentsService = studentsService;
        this.professorService = professorsService;
        this.companyService = companyService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        Role role = user.getRole();
        String username = user.getUsername();
        switch (role) {
            case STUDENT:
                try {
                    // Try to find the student by username - if it has a profile, this will succeed
                    studentsService.findByUsername(username);
                    response.sendRedirect("/student/profile");
                } catch (RuntimeException e) {
                    // If no profile exists, redirect to create profile
                    response.sendRedirect("/student/create_profile");
                }
                break;
            case COMPANY:
                try {
                    companyService.findByUsername(username);
                    response.sendRedirect("/company/profile");
                } catch (RuntimeException e) {
                    response.sendRedirect("/company/create_profile");
                }
                break;
            case PROFESSOR:
                try {
                    professorService.findByUsername(username);
                    response.sendRedirect("/professor/profile");
                } catch (RuntimeException e) {
                    response.sendRedirect("/professor/create_profile");
                }
                break;
            case COMMITTEE:
                response.sendRedirect("/committee/dashboard");
                break;
            default:
                response.sendRedirect("/");
                break;
        }
    }
}
