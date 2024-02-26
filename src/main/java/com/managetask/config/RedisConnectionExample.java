package com.managetask.config;

import redis.clients.jedis.Jedis;

public class RedisConnectionExample {

    public static void main(String[] args) {
        try (Jedis jedis = createRedisConnection()) {
            // Test the connection
            System.out.println("Connected to Redis");
            String key = "exampleKey";
            String value = "exampleValue";

            // Set a key-value pair
            jedis.set(key, value);

            // Retrieve the value
            String retrievedValue = jedis.get(key);
            System.out.println("Retrieved value for key '" + key + "': " + retrievedValue);
        } catch (Exception e) {
            System.err.println("Error connecting to Redis: " + e.getMessage());
        }
    }

    private static Jedis createRedisConnection() {
        // Replace these values with your actual Redis server configuration
        String redisHost = "127.0.0.1";
        int redisPort = 6379;

        Jedis jedis = new Jedis(redisHost, redisPort);

        // If your Redis server has authentication, set the password
        // jedis.auth("yourRedisPassword");

        return jedis;
    }
}

