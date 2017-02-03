package com.pilot.spring;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pilot.spring.service.MybatisService;

public class PilotSpring {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring/spring.xml");
		context.registerShutdownHook();
		context.getBean(MybatisService.class).pilot();
	}
}
