package com.amore.test;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.amore.test.service.FactoryService;
import com.amore.test.service.FactoryServiceImpl;
import com.amore.test.util.CommonUtil;
import com.amore.test.util.FactoryUtil;
import com.amore.test.util.TimeUtil;

@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {
		//SpringApplication.run(TestApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(TestApplication.class);
		TimeUtil.currentDateTime = LocalDateTime.of(2022, 8, 1, 9, 0);
		TimeUtil.init();
		//FactoryUtil.init();
		Thread FactoryThread = new Thread(context.getBean(FactoryUtil.class));
		FactoryThread.start();
	}
	/*
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		TimeUtil.currentDateTime = LocalDateTime.of(2022, 8, 1, 9, 0);
		TimeUtil.init();
		//FactoryUtil.init();
		ConfigurableApplicationContext context = SpringApplication.run(TestApplication.class);
		Thread FactoryThread = new Thread(context.getBean(FactoryUtil.class));
		FactoryThread.start();
	}
	*/
}
