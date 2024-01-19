package org.example.postal_items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PostalItemsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostalItemsApplication.class, args);
    }

}
