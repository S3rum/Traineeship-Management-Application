package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.*;
import com.hustle.Traineeship.Management.Application.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username '" + user.getUsername() + "' is already taken.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser;
        switch (user.getRole()) {
            case STUDENT:
                Student student = new Student();
                student.setUsername(user.getUsername());
                student.setPassword(user.getPassword());
                student.setRole(user.getRole());
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
