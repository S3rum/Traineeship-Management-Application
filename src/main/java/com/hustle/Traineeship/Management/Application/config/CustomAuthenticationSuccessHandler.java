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

        switch (role) {
            case STUDENT:
                if (!studentsService.studentProfileExists(user.getId())) {
                    response.sendRedirect("/student/create_profile");
                } else {
                    response.sendRedirect("/student/profile");
                }
                break;
            case COMPANY:
                if (!companyService.companyProfileExists(user.getId())) {
                    response.sendRedirect("/company/create_profile");
                } else {
                    response.sendRedirect("/company/profile");
                }
                break;
            case PROFESSOR:
                if (!professorService.professorProfileExists(user.getId())) {
                    response.sendRedirect("/professor/create_profile");
                } else {
                    response.sendRedirect("/professor/profile");
                }
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
