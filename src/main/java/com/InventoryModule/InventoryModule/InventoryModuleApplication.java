package com.InventoryModule.InventoryModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InventoryModuleApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryModuleApplication.class, args);
	}
}

