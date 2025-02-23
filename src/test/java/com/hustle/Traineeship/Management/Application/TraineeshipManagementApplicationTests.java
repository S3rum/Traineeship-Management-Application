package com.hustle.Traineeship.Management.Application;

import com.hustle.Traineeship.Management.Application.repos.UserRepository;
import com.hustle.Traineeship.Management.Application.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class TraineeshipManagementApplicationTests {

	@Autowired
	private UserServiceImpl service;

	@MockitoBean
	private UserRepository repository;




	@Test
	void contextLoads() {
	}

}
