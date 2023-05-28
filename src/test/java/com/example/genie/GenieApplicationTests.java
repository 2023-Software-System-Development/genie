package com.example.genie;

import com.example.genie.domain.auth.service.AuthUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class GenieApplicationTests {

	@Autowired
	AuthUserService authUserService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Test
	void contextLoads() {

	}


}
