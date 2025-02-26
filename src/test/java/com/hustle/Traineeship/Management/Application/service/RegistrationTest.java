package com.hustle.Traineeship.Management.Application.service;

import com.hustle.Traineeship.Management.Application.model.*;
import com.hustle.Traineeship.Management.Application.repos.CompanyRepository;
import com.hustle.Traineeship.Management.Application.repos.ProfessorRepository;
import com.hustle.Traineeship.Management.Application.repos.StudentRepository;
import com.hustle.Traineeship.Management.Application.repos.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)

class RegistrationTest {
    @Mock
    UserRepository userRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    CompanyRepository companyRepository;
    @Mock
    ProfessorRepository professorRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl service;

    @Test
    void registeredUser() {
        User userStudent = new User();
        User userCompany = new User();
        User userProfessor = new User();
        userStudent.setUsername("StudentRegistered");
        userStudent.setPassword("123456");
        userStudent.setRole(Role.STUDENT);
        userCompany.setUsername("CompanyRegistered");
        userCompany.setPassword("123456");
        userCompany.setRole(Role.COMPANY);
        userProfessor.setUsername("ProfessorRegistered");
        userProfessor.setPassword("123456");
        userProfessor.setRole(Role.PROFESSOR);
        Mockito.when(passwordEncoder.encode(userStudent.getPassword())).thenReturn("encodedPassword");
        Mockito.when(passwordEncoder.encode(userCompany.getPassword())).thenReturn("encodedPassword");
        Mockito.when(passwordEncoder.encode(userProfessor.getPassword())).thenReturn("encodedPassword");
        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(companyRepository.save(Mockito.any(Company.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(professorRepository.save(Mockito.any(Professor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        User studentToRegister = service.registerUser(userStudent);
        Assertions.assertEquals(userStudent.getUsername(),studentToRegister.getUsername());
        Assertions.assertEquals(userStudent.getPassword(),studentToRegister.getPassword());
        Assertions.assertEquals(userStudent.getRole(),studentToRegister.getRole());
        User companyToRegister = service.registerUser(userCompany);
        Assertions.assertEquals(userCompany.getUsername(),companyToRegister.getUsername());
        Assertions.assertEquals(userCompany.getPassword(),companyToRegister.getPassword());
        Assertions.assertEquals(userCompany.getRole(),companyToRegister.getRole());
        User professorToRegister = service.registerUser(userProfessor);
        Assertions.assertEquals(userProfessor.getUsername(),professorToRegister.getUsername());
        Assertions.assertEquals(userProfessor.getPassword(),professorToRegister.getPassword());
        Assertions.assertEquals(userProfessor.getRole(),professorToRegister.getRole());

    }
}

