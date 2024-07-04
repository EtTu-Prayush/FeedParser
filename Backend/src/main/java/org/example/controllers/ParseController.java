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

/**
 * This class is the controller class for the REST API.
 * The REST API provides the following endpoints:
 * 1. /parse-json - To parse the JSON content
 * 2. /parse-xml - To parse the XML content
 * 3. /submit-metadata - To submit the metadata for the given URL
 * 4. /current-metadata - To get the current metadata
 */
@RestController
@RequestMapping("/api")
public class ParseController {

    @Autowired
    private ParseTree parseTree;
    private final ConcurrentMap<String, Map<String, String>> metadataMap = new ConcurrentHashMap<>();

    /**
     * This method is used to parse the JSON content and return the parsed JSON content.
     * The JSON content is parsed using the ParseTree class.
     * @param jsonContent - The JSON content to be parsed
     * @return - The parsed JSON content
     * @throws Exception - If there is an error while parsing the JSON content
     */
    @PostMapping("/parse-json")
    public Map<String, String> parseJson(@RequestBody String jsonContent) throws Exception {
        return parseTree.parseJSON(jsonContent);
    }

    /**
     * This method is used to parse the XML content and return the parsed XML content.
     * The XML content is parsed using the ParseTree class.
     * @param xmlContent - The XML content to be parsed
     * @return - The parsed XML content
     * @throws Exception - If there is an error while parsing the XML content
     */
    @PostMapping("/parse-xml")
    public Map<String, String> parseXML(@RequestBody String xmlContent) throws Exception {
        return parseTree.parseXML(xmlContent);
    }


    /**
     * This method is used to submit the metadata for the given URL.
     * The metadata is used to parse the content from the URL.
     * The content is fetched from the URL using the fetchContentFromURL method in the Main class.
     * The content is then parsed using the metadata and the ParseTree class.
     * The parsed JSON content is then stored in the database using the FeedHandler class.
     * @param requestData - The request data containing the URL and metadata
     */
    @PostMapping("/submit-metadata")
    public void submitMetadata(@RequestBody Map<String, Object> requestData) {
        String url = (String) requestData.get("url");
        Map<String, String> metadata = (Map<String, String>) requestData.get("metadata");

        String content = Main.fetchContentFromURL(url);
        /**     Debugging code
        *       System.out.println("Content: " + content);
        *       System.out.println("Metadata: " + metadata);
         */
        metadataMap.put("currentMetadata", metadata);
        List<FeedData> parsedJson = parseTree.parseJsonUsingMetadata(content, metadata);
        FeedHandler feedHandler = new FeedHandler();
        for(FeedData feedData: parsedJson) {
            feedHandler.addFeed(feedData);
        }
    }

    /**
     * This method is used to get the current metadata.
     * The current metadata is the metadata that was submitted using the submitMetadata method.
     * @return - The current metadata
     */
    @GetMapping("/current-metadata")
    public Map<String, String> getCurrentMetadata() {
        return metadataMap.get("currentMetadata");
    }
}
