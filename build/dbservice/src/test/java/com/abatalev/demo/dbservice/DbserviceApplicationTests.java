package com.abatalev.demo.dbservice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DbserviceApplicationTests {
	
	@BeforeAll 
	static void init(){
		System.setProperty("OTLP_HOST", "example.com");
	}

	@Test
	void contextLoads() {
	}
}
