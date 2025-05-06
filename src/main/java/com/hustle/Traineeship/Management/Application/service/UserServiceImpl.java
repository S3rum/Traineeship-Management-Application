package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.*;
import com.hustle.Traineeship.Management.Application.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;




@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Repositories for the specific types:
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        // Check if username is already taken
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username '" + user.getUsername() + "' is already taken.");
        }

        // Encode the raw password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Create the appropriate subclass instance with a basic profile
        User savedUser;
        switch (user.getRole()) {
            case STUDENT:
                Student student = new Student();
                student.setUsername(user.getUsername());
                student.setPassword(user.getPassword());
                student.setRole(user.getRole());
                // Set empty values for required fields to create a basic profile
                student.setFullName("");
                student.setUniversityId(null);
                savedUser = studentRepository.save(student);
                break;
            case COMPANY:
                Company company = new Company();
                company.setUsername(user.getUsername());
                company.setPassword(user.getPassword());
                company.setRole(user.getRole());
                savedUser = companyRepository.save(company);
                break;
            case PROFESSOR:
                Professor professor = new Professor();
                professor.setUsername(user.getUsername());
                professor.setPassword(user.getPassword());
                professor.setRole(user.getRole());
                savedUser = professorRepository.save(professor);
                break;
            case COMMITTEE:
                Committee committee = new Committee();
                committee.setUsername(user.getUsername());
                committee.setPassword(user.getPassword());
                committee.setRole(user.getRole());
                savedUser = committeeRepository.save(committee);
                break;
            default:
                savedUser = userRepository.save(user);
                break;
        }
        return savedUser;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    }
}
