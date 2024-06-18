package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class to parse the feed from the given URL and store the feed data in the database.
 * The feed can be in the form of RSS or JSON.
 * The feed data is stored in the database using the FeedHandler class.
 * The feed data is parsed using the following classes:
 * 1. ParseRSS - To parse the RSS feed
 * 2. ParseStructuredJSON - To parse the structured JSON feed
 * 3. ParseUnStructuredJSON - To parse the unstructured JSON feed
 * The feed data is classified as structured or unstructured JSON using the ClassifyJSON class.
 */

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}