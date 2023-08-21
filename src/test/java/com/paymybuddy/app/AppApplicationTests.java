package com.paymybuddy.app;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paymybuddy.app.controller.LoginController;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AppApplicationTests {
	
	@Autowired
	private LoginController controller;

	@Test
	void contextLoads()throws Exception {
	   assertThat(controller).isNotNull();
	   }

}
