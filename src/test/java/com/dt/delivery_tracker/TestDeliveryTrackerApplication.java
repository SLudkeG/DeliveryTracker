package com.dt.delivery_tracker;

import org.springframework.boot.SpringApplication;

import com.dt.delivery_tracker.DeliveryTrackerApplication;

public class TestDeliveryTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.from(DeliveryTrackerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
