package com.hustle.Traineeship.Management.Application.config;

import com.hustle.Traineeship.Management.Application.service.StudentsService;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/css/**", "/js/**").permitAll()
                        // Optionally, restrict pages by role if you like:
                        // .requestMatchers("/student/**").hasRole("STUDENT")
                        // .requestMatchers("/company/**").hasRole("COMPANY")
                        // .requestMatchers("/professor/**").hasRole("PROFESSOR")
                        // .requestMatchers("/committee/**").hasRole("COMMITTEE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")        // custom login page at /auth/login
                        .successHandler(customSuccessHandler())
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")  // Redirects to homepage after logout
                        .permitAll()
                );

        return http.build();
    }

    // Bean method to create your custom success handler

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        // Pass the StudentsService into the constructor
        return new CustomAuthenticationSuccessHandler(studentsService);
    }




}
