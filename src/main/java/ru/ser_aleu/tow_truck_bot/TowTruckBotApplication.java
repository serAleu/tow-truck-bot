package ru.ser_aleu.tow_truck_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TowTruckBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TowTruckBotApplication.class, args);
	}

}
