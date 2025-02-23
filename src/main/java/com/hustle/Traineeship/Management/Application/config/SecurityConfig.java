package com.hustle.Traineeship.Management.Application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                        .loginPage("/auth/login")         // custom login page at /auth/login
                        .successHandler(customSuccessHandler()) // <--- plug in the handler
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
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

}
