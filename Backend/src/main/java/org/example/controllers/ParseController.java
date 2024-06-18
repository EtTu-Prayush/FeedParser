package org.example.controllers;

import org.example.Parsers.FeedData;
import org.example.Parsers.FeedHandler;
import org.example.Main;
import org.example.Parsers.ParseTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ParseController {

    @Autowired
    private ParseTree parseTree;
    private final ConcurrentMap<String, Map<String, String>> metadataMap = new ConcurrentHashMap<>();

    @PostMapping("/parse-json")
    public Map<String, String> parseJson(@RequestBody String jsonContent) throws Exception {
//        System.out.println("#parse-json Received JSON: " + jsonContent);
//        System.out.println("#parse-json parseTree: " + (parseTree.parseJSON(jsonContent)));
        return parseTree.parseJSON(jsonContent);
    }

    @PostMapping("/parse-xml")
    public Map<String, String> parseXML(@RequestBody String xmlContent) throws Exception {
        return parseTree.parseXML(xmlContent);
    }

//    @PostMapping("/submit-metadata")
//    public void submitMetadata(@RequestBody Map<String, String> metadata) {
//
//        String text = Main.fetchContentFromURL(content);
//        metadataMap.put("currentMetadata", metadata);
//        System.out.println("Received metadata: " + metadata);
//        List<FeedData>  parsedJson = parseTree.parseJsonUsingMetadata(text, metadata);
//        FeedHandler feedHandler = new FeedHandler();
//        feedHandler.addFeeds(parsedJson);
//    }

    @PostMapping("/submit-metadata")
    public void submitMetadata(@RequestBody Map<String, Object> requestData) {
        String url = (String) requestData.get("url");
        Map<String, String> metadata = (Map<String, String>) requestData.get("metadata");

        // Fetch content from the URL
        String content = Main.fetchContentFromURL(url);
//        System.out.println("## Content: " + content);
        System.out.println("## Metadata: " + metadata);
        metadataMap.put("currentMetadata", metadata);
        List<FeedData> parsedJson = parseTree.parseJsonUsingMetadata(content, metadata);
        FeedHandler feedHandler = new FeedHandler();
        for(FeedData feedData: parsedJson) {
            feedHandler.addmetadataFeed(feedData);
        }
    }

    @GetMapping("/current-metadata")
    public Map<String, String> getCurrentMetadata() {
        return metadataMap.get("currentMetadata");
    }
}
