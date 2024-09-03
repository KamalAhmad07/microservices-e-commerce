package com.service.inventory_service;

import com.service.inventory_service.model.Inventory;
import com.service.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;


@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		 return args->{
			 Inventory inventory = new Inventory();
			 inventory.setSkuCode("iphone_13");
			 inventory.setQuantity(180);

			 Inventory inventory1 = new Inventory();
			 inventory.setSkuCode("iphone_13_red");
			 inventory.setQuantity(1);

			 inventoryRepository.save(inventory);
			 inventoryRepository.save(inventory1);

		 };
	}
}