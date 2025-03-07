package com.siopa.siopa_products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SiopaProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiopaProductsApplication.class, args);
	}

}
