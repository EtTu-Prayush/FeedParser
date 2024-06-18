package org.example.Parsers;
/**
 * Dependencies:
 *     gson: com.google.code.gson:gson:2.8.8 - for JSON parsing
 *     log4j-api: org.apache.logging.log4j:log4j-api:2.14.1 - for logging
 *     log4j-core: org.apache.logging.log4j:log4j-core:2.14.1 - for logging
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.google.gson.JsonElement;
import org.example.Classifiers.ClassifyJSONcomponents;
import org.example.utils.FieldMappingLoader;


/**
 * Class to parse the unstructured JSON feed and convert it into a list of FeedData objects.
 */
public class ParseUnStructuredJSON {
    private FieldMappingLoader fieldMappingLoader;

    /**
     * Constructor to load the field mappings from the metadata file.
     */
    public ParseUnStructuredJSON() {
        fieldMappingLoader = new FieldMappingLoader("src/main/java/org/example/utils/metadata.json");
        System.out.println("Field mappings loaded successfully");
    }

    /**
     * Method to parse the unstructured JSON feed from the given content and convert it into a list of FeedData objects.
     *
     * @param content - JSON feed content
     * @return List of FeedData objects
     */
    public List<FeedData> parseJsonFeed(String content) {
        JsonElement jsonElement = JsonParser.parseString(content);
        List<FeedData> feedDataList = new ArrayList<>();

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            jsonObject.entrySet().forEach(entry -> exploreJsonElement(entry.getValue(), feedDataList));
        }
        return feedDataList;
    }

    /**
     * Method to explore the JSON element recursively and parse the feed data.
     *
     * @param element - JSON element to explore
     * @param feedDataList - List to store the parsed FeedData objects
     */
    private void exploreJsonElement(JsonElement element, List<FeedData> feedDataList) {
        if (element.isJsonArray()) {
            System.out.println("Element: " + element.toString());
            JsonArray array = element.getAsJsonArray();
            classifyAndParseJsonArray(array, feedDataList);
        } else if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            jsonObject.entrySet().forEach(entry -> exploreJsonElement(entry.getValue(), feedDataList));
        }
    }


    /**
     * Method to classify and parse the JSON array based on its type.
     *
     * @param array - JSON array to classify and parse
     * @param feedDataList - List to store the parsed FeedData objects
     */
    private void classifyAndParseJsonArray(JsonArray array, List<FeedData> feedDataList) {
        String type = new ClassifyJSONcomponents().classifyJsonArray(array);
        switch (type) {
            case "item":
                array.forEach(itemElement -> {
                    if (itemElement.isJsonObject()) {
                        JsonObject itemObject = itemElement.getAsJsonObject();
                        FeedData feedData = parseItem(itemObject);
                        if (itemObject.has("images") && itemObject.get("images").isJsonArray()) {
                            parseImageIntoItem(feedData, itemObject.getAsJsonArray("images"));
                        }
                        feedDataList.add(feedData);
                    }
                });
                break;
            case "image":
                array.forEach(imageElement -> {
                    if (imageElement.isJsonObject()) {
                        FeedData feedData = parseImage(imageElement.getAsJsonObject());
                        feedDataList.add(feedData);
                    }
                });
                break;
            default:
                // If it's not classified as item or image, recursively handling nested arrays
                array.forEach(element -> exploreJsonElement(element, feedDataList));
                break;
        }
    }




    /**
     * Method to parse the JSON object representing an item into a FeedData object.
     *
     * @param jsonObject - JSON object representing an item
     * @return FeedData object
     */
    private FeedData parseItem(JsonObject jsonObject) {
        String title = findField(jsonObject, "title");
        String description = findField(jsonObject, "description");
        String linkStr = findField(jsonObject, "link");
        String publishDate = findField(jsonObject, "publishDate");
        String guid = findField(jsonObject, "guid");
        String author = findField(jsonObject, "author");
        String comments = findField(jsonObject, "comments");
        String imgUrl = findField(jsonObject, "imgURL");

        try {
            URL link = new URL(linkStr);
            URL image = imgUrl != null ? new URL(imgUrl) : null;
            return new FeedData(title, description, link, publishDate, guid, image, author, comments, null, imgUrl, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to parse the JSON object representing an image into a FeedData object.
     *
     * @param jsonObject - JSON object representing an image
     * @return FeedData object
     */
    private FeedData parseImage(JsonObject jsonObject) {
        String imgUrl = findField(jsonObject, "imgURL");
        try {
            URL imageUrl = imgUrl != null ? new URL(imgUrl) : null;
            // Assuming other image-related fields are parsed here if necessary
            return new FeedData(null, null, imageUrl, null, null, imageUrl, null, null, null, imgUrl, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to parse the image details into the FeedData object.
     *
     * @param feedData - FeedData object to update with image details
     * @param imageArray - JSON array containing image details
     * @return FeedData object with image details
     */
    private FeedData parseImageIntoItem(FeedData feedData, JsonArray imageArray) {
        for (JsonElement element : imageArray) {
            JsonObject imageObject = element.getAsJsonObject();
            String imgUrl = findField(imageObject, "imgURL");
            try {
                URL imageUrl = imgUrl != null ? new URL(imgUrl) : null;
                feedData.setImage(imageUrl);
                // Additional image details can be parsed and set here if needed
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return feedData;
    }

    /**
     * Method to find the field value in the JSON object based on the field name.
     *
     * @param jsonObject - JSON object to search for the field
     * @param fieldName - Field name to search for
     * @return Field value if found, null otherwise
     */
    private String findField(JsonObject jsonObject, String fieldName) {
        Set<String> possibleKeys = fieldMappingLoader.getMapping(fieldName);
        for (String key : possibleKeys) {
            if (jsonObject.has(key)) {
                return jsonObject.get(key).getAsString();
            }
        }
        return null;
    }


}

