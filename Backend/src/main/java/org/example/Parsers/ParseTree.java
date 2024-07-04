package org.example.Parsers;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource; // Importing InputSource class
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class ParseTree {

    /* Method to parse XML and return a map of tags and their paths
     * @param xmlContent - XML content
     * @return Map of tags and their paths
     */
    public Map<String, String> parseXML(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlContent))); // Use InputSource

        Map<String, String> fields = new HashMap<>();
        traverseXML(document.getDocumentElement(), "", fields);
        return fields;
    }

    /* Method to traverse the XML tree and store the paths of the tags in a map
     * @param node - XML node
     * @param path - Path of the current node
     * @param fields - Map to store the paths of the tags
     */
    private void traverseXML(Node node, String path, Map<String, String> fields) {
        String currentPath = path.isEmpty() ? node.getNodeName() : path + "." + node.getNodeName();
        fields.put(currentPath, node.getNodeValue());
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                traverseXML(childNode, currentPath, fields);
            }
        }
    }

    /* Method to parse JSON and return a map of keys and their paths
     * @param jsonContent - JSON content
     * @return Map of keys and their paths
     */
    public Map<String, String> parseJSON(String jsonContent) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        Map<String, String> fields = new HashMap<>();
        traverseJSON(rootNode, "", fields);
        return fields;
    }

    /* Method to traverse the JSON tree and store the paths of the keys in a map
     * @param node - JSON node
     * @param path - Path of the current node
     * @param fields - Map to store the paths of the keys
     */
    private void traverseJSON(JsonNode node, String path, Map<String, String> fields) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                String currentPath = path.isEmpty() ? entry.getKey() : path + "." + entry.getKey();
                fields.put(currentPath, entry.getValue().toString());
                traverseJSON(entry.getValue(), currentPath, fields);
            });
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                traverseJSON(node.get(i), path + "[" + i + "]", fields);
            }
        } else {
            fields.put(path, node.asText());
        }
    }



    /**
     * Method to parse JSON content using metadata paths and convert it into a list of FeedData objects.
     *
     * @param content  - JSON content
     * @param metadata - Metadata containing field mappings
     * @return List of FeedData objects
     */

    public List<FeedData> parseJsonUsingMetadata(String content, Map<String, String> metadata) {
        JsonElement jsonElement = JsonParser.parseString(content);
        List<FeedData> feedDataList = new ArrayList<>();

        Map<String, String> cleanedMetadata = cleanMetadataPaths(metadata);
        System.out.println("CHECKPOINT #1 (parseJsonUsingMetadata) - Cleaned metadata: " + cleanedMetadata);

        String maxOverlapKey = maxOverlap(cleanedMetadata);
//        System.out.println("CHECKPOINT #2 (parseJsonUsingMetadata) - Max overlap key: " + maxOverlapKey);

        if (!maxOverlapKey.equals("null")) {
            traverseToMaxOverlap(maxOverlapKey, jsonElement, feedDataList, cleanedMetadata);
        }

        return feedDataList;
    }

    /**
     * Method to traverse the JSON tree to the maximum overlap key and extract the feed data.
     *
     * @param maxOverlapKey - Maximum overlap key
     * @param jsonElement   - JSON element
     * @param feedDataList  - List of FeedData objects
     * @param cleanedMetadata - Cleaned metadata containing field mappings
     * @return List of FeedData objects
     */
    private List<FeedData> traverseToMaxOverlap(String maxOverlapKey, JsonElement jsonElement, List<FeedData> feedDataList, Map<String, String> cleanedMetadata) {
        String[] keys = maxOverlapKey.split("\\.");
        JsonElement element = jsonElement;
//        System.out.println("CHECKPOINT #3 (traverseToMaxOverlap) - Keys: " + Arrays.toString(keys));

        for (String key : keys) {
            if (element != null && element.isJsonObject() && element.getAsJsonObject().has(key)) {
                element = element.getAsJsonObject().get(key);
            } else if (element != null && element.isJsonArray()) {
//                System.out.println("CHECKPOINT #4 (traverseToMaxOverlap) - Handling array element for key: " + key);
                element = handleArray(element.getAsJsonArray(), key);
            } else {
                element = null;
                break;
            }
        }

//        System.out.println("CHECKPOINT #5 (traverseToMaxOverlap) - Element after traversal: " + element);

        if (element != null && element.isJsonArray()) {
//            System.out.println("CHECKPOINT #6 (traverseToMaxOverlap) - Processing array: " + element.getAsJsonArray());
            for (JsonElement item : element.getAsJsonArray()) {
                if (item.isJsonObject()) {
                    parseItem(item.getAsJsonObject(), feedDataList, cleanedMetadata, maxOverlapKey);
                }
            }
        }

        return feedDataList;
    }

    /**
     * Method to parse the JSON object and extract the feed data based on the cleaned metadata.
     *
     * @param itemObject - JSON object representing an item in the feed
     * @param feedDataList - List of FeedData objects
     * @param cleanedMetadata - Cleaned metadata containing field mappings
     * @param maxOverlapKey - Maximum overlap key
     */
    private void parseItem(JsonObject itemObject, List<FeedData> feedDataList, Map<String, String> cleanedMetadata, String maxOverlapKey) {
        FeedData feedData = new FeedData();
        int prefixLength = maxOverlapKey.length();

        for (Map.Entry<String, String> entry : cleanedMetadata.entrySet()) {
            String relativePath = entry.getValue().substring(prefixLength);
            if (relativePath.startsWith(".")) {
                relativePath = relativePath.substring(1);
            }
            String[] path = relativePath.split("\\.");
            JsonElement currentElement = itemObject;
//            System.out.println("CHECKPOINT #6 (parseItem) - Current element: " + currentElement);
//            System.out.println("CHECKPOINT #7 (parseItem) - Processing path: " + Arrays.toString(path));

            for (String key : path) {
                if (currentElement != null) {
                    if (currentElement.isJsonObject() && currentElement.getAsJsonObject().has(key)) {
                        currentElement = currentElement.getAsJsonObject().get(key);
                    } else if (currentElement.isJsonArray()) {
                        JsonArray jsonArray = currentElement.getAsJsonArray();
                        for (JsonElement element : jsonArray) {
                            if (element.isJsonObject() && element.getAsJsonObject().has(key)) {
                                currentElement = element.getAsJsonObject().get(key);
                                break; // Stop iterating once key is found in an object inside the array
                            }
                        }
                    } else {
                        currentElement = null;
                        break;
                    }
                }
            }

            if (currentElement != null && !currentElement.isJsonNull()) {
                try {
                    switch (entry.getKey()) {
                        case "link":
                            feedData.setLink(new URL(currentElement.getAsString()));
                            break;
                        case "title":
                            feedData.setTitle(currentElement.getAsString());
                            break;
                        case "imageUrl":
                            feedData.setImageURL(currentElement.getAsString());
                            break;
                        case "publishDate":
                            feedData.setPublishDate(currentElement.getAsString());
                            break;
                        case "description":
                            feedData.setDescription(currentElement.getAsString());
                            break;
                        default:
//                            System.out.println("CHECKPOINT #13 (parseItem) - " + entry.getKey() + ": " + currentElement.getAsString());
                    }
                } catch (Exception e) {
                    System.err.println("CHECKPOINT #ERROR (parseItem) - Error setting field: " + entry.getKey() + " with value: " + currentElement.getAsString());
                    e.printStackTrace();
                }
            } else {
                // Set field to null if information is missing or unavailable
                switch (entry.getKey()) {
                    case "link":
                        feedData.setLink(null);
                        break;
                    case "title":
                        feedData.setTitle(null);
                        break;
                    case "imageUrl":
                        feedData.setImageURL(null);
                        break;
                    case "publishDate":
                        feedData.setPublishDate(null);
                        break;
                    case "description":
                        feedData.setDescription(null);
                        break;
                }
            }
        }

        feedDataList.add(feedData);
        System.out.println("CHECKPOINT #14 (parseItem) - FeedData added: " + feedData);
    }


    /**
     * Method to handle array elements in JSON.
     *
     * @param jsonArray - JSON array
     * @param key - Key to be searched in the array
     * @return JSON element
     */
    private JsonElement handleArray(JsonArray jsonArray, String key) {
        try {
            int index = Integer.parseInt(key);
            if (index < jsonArray.size()) {
                return jsonArray.get(index);
            }
        } catch (NumberFormatException e) {
            if (jsonArray.size() > 0 && jsonArray.get(0).isJsonObject()) {
                return jsonArray.get(0).getAsJsonObject().get(key);
            }
        }
        return null;
    }
    /*
    * Method to find the maximum overlap in the metadata paths.
    * @param cleanedMetadata - Cleaned metadata containing field mappings
    * @return Maximum overlap key
    */
    private String maxOverlap(Map<String, String> cleanedMetadata) {
        String minLenValue = "";
        int minLen = Integer.MAX_VALUE;
        for (Map.Entry<String, String> entry : cleanedMetadata.entrySet()) {
            if (entry.getValue().length() < minLen) {
                minLen = entry.getValue().length();
                minLenValue = entry.getValue();
            }
        }
        int minLenValueLen = minLenValue.length();

        for (int i = 0; i < minLenValueLen; i++) {
            for (Map.Entry<String, String> entry : cleanedMetadata.entrySet()) {
                if (entry.getValue().charAt(i) != minLenValue.charAt(i)) {
//                    System.out.println("CHECKPOINT #11 (maxOverlap) - Max overlap: " + minLenValue.substring(0, i));
                    return minLenValue.substring(0, i);
                }
            }
        }
        return "null";
    }

    /**
     * Method to clean the metadata paths by removing the array indices.
     *
     * @param metadata - Metadata containing field mappings
     * @return Cleaned metadata containing field mappings
     */
    private Map<String, String> cleanMetadataPaths(Map<String, String> metadata) {
        Pattern pattern = Pattern.compile("\\[.*?\\]");
        Map<String, String> cleanedMetadata = metadata.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> pattern.matcher(entry.getValue()).replaceAll("")
                ));
//        System.out.println("CHECKPOINT #12 (cleanMetadataPaths) - Cleaned metadata: " + cleanedMetadata);
        return cleanedMetadata;
    }

}


