package com.managetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;

@SpringBootApplication
		(exclude = RedisRepositoriesAutoConfiguration.class)
@EnableCaching
@EnableAsync
@ComponentScan(basePackages = { "com.managetask.*"})
//@ConfigurationProperties(prefix = "")
public class ManagerApplication implements CommandLineRunner {


	@Autowired
	Environment env;

	@PostConstruct
	public void run()
	{
		System.out.println("Post construct method");
	}

	public static void main(String[] args) {

		SpringApplication.run(ManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		System.out.println("command line runner"+env.getProperty());

	}
}
