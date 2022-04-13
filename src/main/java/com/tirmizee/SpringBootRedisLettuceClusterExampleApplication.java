package com.tirmizee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;


@SpringBootApplication
public class SpringBootRedisLettuceClusterExampleApplication implements CommandLineRunner {

	@Autowired
	ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisLettuceClusterExampleApplication.class, args);
	}

	@Override
	public void run(String... args) {

		RedisConnectionFactory redisConnectionFactory = applicationContext.getBean(RedisConnectionFactory.class);
		StringRedisTemplate stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
		RedisProperties redisProperties = applicationContext.getBean(RedisProperties.class);

		boolean hasKey = stringRedisTemplate.hasKey("temp");
		if(!hasKey) {
			stringRedisTemplate.opsForValue().set("temp","hello");
		}

		System.out.println(stringRedisTemplate.opsForValue().get("temp"));

	}
}
