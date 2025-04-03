package com.abatalev.demo.stub;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StubApplicationTests {
	
	@BeforeAll 
	static void init(){
		System.setProperty("OTLP_HOST", "otlp.example.com");
		System.setProperty("OTLP_DISABLED", "true");
	}

	@Test
	void contextLoads() {
	}
}