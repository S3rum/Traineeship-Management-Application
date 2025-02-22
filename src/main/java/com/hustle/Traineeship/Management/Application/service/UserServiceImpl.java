package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.User;
import com.hustle.Traineeship.Management.Application.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User registerUser(User user) {
        // 1. Check if username is already taken
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if (((Optional<?>) existing).isPresent()) {
            // 2. Handle it (throw exception or return an error)
            throw new RuntimeException("Username '" + user.getUsername() + "' is already taken.");
        }

        // 3. If username not taken, proceed to save
        return userRepository.save(user);
    }
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
