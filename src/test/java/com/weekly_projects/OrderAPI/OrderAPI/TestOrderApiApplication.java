package com.weekly_projects.OrderAPI.OrderAPI;

import org.springframework.boot.SpringApplication;

public class TestOrderApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(OrderApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
