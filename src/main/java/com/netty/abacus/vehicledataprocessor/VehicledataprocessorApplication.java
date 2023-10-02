package com.netty.abacus.vehicledataprocessor;

import java.text.ParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VehicledataprocessorApplication {

	public static void main(String[] args) throws InterruptedException, ParseException {
		SpringApplication.run(VehicledataprocessorApplication.class, args);
		
		// Start the Netty server
        NettyServerInitializer nettyServerInitializer = new NettyServerInitializer();
        nettyServerInitializer.startServer();
	}
	
	

}
