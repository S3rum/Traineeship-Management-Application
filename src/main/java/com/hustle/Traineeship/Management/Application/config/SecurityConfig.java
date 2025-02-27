package com.hustle.Traineeship.Management.Application.config;

import com.hustle.Traineeship.Management.Application.service.ProfessorService;
import com.hustle.Traineeship.Management.Application.service.StudentsService;
import com.hustle.Traineeship.Management.Application.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private ProfessorService professorService;  // Inject the professor service

    @Autowired
    private CompanyService companyService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // custom login page
                        // Now call customSuccessHandler with both dependencies
                        .successHandler(customSuccessHandler(studentsService, professorService,companyService))
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // Redirects to homepage after logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler(StudentsService studentsService, ProfessorService professorService,CompanyService companyService) {
        return new CustomAuthenticationSuccessHandler(studentsService, professorService, companyService);
    }
}
