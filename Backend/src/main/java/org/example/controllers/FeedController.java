package org.example.controllers;

import org.example.Parsers.FeedData;
import org.example.Parsers.FeedHandler;
import org.example.Main;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * This class is the controller for the API endpoints.
 * It has the following endpoints:
 * 1. GET /api/feeds - To get all the feeds
 * 2. POST /api/feeds - To add a new feed
 * 3. POST /api/feeds/clear - To clear all the feeds
 * 4. GET /api/feeds/hello - Dummy endpoint to return "HELLO"
 */

@RestController
@RequestMapping("/api/feeds")
public class FeedController {
    private FeedHandler feedHandler = new FeedHandler();

    /**
     * This method is called when the GET /api/feeds endpoint is hit.
     * It returns all the feeds in the system.
     * @return List<FeedData> - List of all the feeds
     */
    @GetMapping
    public List<FeedData> getFeeds() {
        System.out.println("GET /api/feeds called");
        List<FeedData> feeds = feedHandler.getFeedsfromFile();
        feeds.sort(FeedData::compareTo);
        return feeds;
    }

    /**
     * This method is called when the POST /api/feeds endpoint is hit.
     * It adds a new feed to the system.
     * @param request - Map<String, String> - The request body containing the URL of the feed
     */
    @PostMapping
    public Map<String, Object> addFeed(@RequestBody Map<String, String> request) {
        System.out.println("POST /api/feeds called with URL: " + request.get("url"));
        String feedUrl = request.get("url");
        Map<String, Object> result = Main.parseFeed(feedUrl);

        if (result.containsKey("unstructured")) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "unstructured");
            response.put("message", "The feed is unstructured. Please provide metadata.");
            response.put("content", result.get("content"));
            return response;
        } else {
            List<FeedData> feedDataList = (List<FeedData>) result.get("feedDataList");
            feedHandler.addFeeds(feedDataList);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "structured");
            response.put("feeds", feedDataList);
            return response;
        }
    }

    @PostMapping("/metadata")
    public void addMetadata(@RequestBody Map<String, String> metadata) {
        System.out.println("POST /api/feeds/metadata called with metadata: " + metadata);
        // Process metadata and store in FeedHandler
        // Assume metadata processing and feed addition here
    }

    /**
     * This method is called when the POST /api/feeds/clear endpoint is hit.
     * It clears all the feeds in the system.
     */
    @PostMapping("/clear")
    public void clearFeeds() {
        System.out.println("POST /api/feeds/clear called");
        feedHandler.clearFeeds();
    }

    /**
     * This method is called when the GET /api/feeds/hello endpoint is hit.
     * It returns "HELLO".
     * @return String - "HELLO"
     */
    @GetMapping("/hello")
    public String sayHello() {
        System.out.println("GET /api/feeds/hello called");
        return "HELLO";
    }
}
