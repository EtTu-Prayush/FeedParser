//package org.example;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.HashMap;
//
//@RestController
//@RequestMapping("/api")
//public class ParseTreeController {
//
//
//    private ParseTree parseTree;
//
//    private ParseController parseController;
//
//    private FeedHandler feedHandler;
//
//    private final ConcurrentMap<String, Map<String, String>> metadataMap = new ConcurrentHashMap<>();
//
//    @PostMapping("/parse-json")
//    public Map<String, String> parseJson(@RequestBody String jsonContent) throws Exception {
//        Map<String, String> metadata = parseController.getCurrentMetadata();
//        if (metadata == null) {
//            throw new IllegalStateException("Metadata not found. Please submit metadata before parsing JSON.");
//        }
//        List<FeedData> parsedJson = parseTree.parseJsonUsingMetadata(jsonContent, metadata);
//        System.out.println("Parsed JSON tree in backend: " + parsedJson);  // Print JSON tree in backend console
//
//        // Convert List<FeedData> to Map<String, String> if needed
//        Map<String, String> parsedJsonMap = new HashMap<>();
//        // Conversion logic if necessary
//
//        return parsedJsonMap;
//    }
//
//    @PostMapping("/submit-metadata")
//    public void submitMetadata(@RequestBody Map<String, String> metadata) {
//        System.out.println("Received metadata: " + metadata);
//        metadataMap.put("currentMetadata", metadata);
//
//        String jsonContent = ""; // Retrieve JSON content from somewhere
//        try {
//            List<FeedData> parsedJson = parseTree.parseJsonUsingMetadata(jsonContent, metadata);
//            System.out.println("Parsed JSON tree after submitting metadata: " + parsedJson);
//
//            // Add parsed data to feed handler
//            feedHandler.addFeeds(parsedJson);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
