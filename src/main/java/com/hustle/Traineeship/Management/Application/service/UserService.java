package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User registerUser(User user);
    // You can add other user-related operations as needed.
}