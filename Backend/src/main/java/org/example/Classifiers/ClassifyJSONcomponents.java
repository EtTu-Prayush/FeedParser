package org.example.Classifiers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.utils.FieldMappingLoader;
import org.example.utils.GloveEmbeddings;

import java.util.Map;
import java.util.Set;

public class ClassifyJSONcomponents {
    private static final Map<String, Set<String>> fieldMappings;
    private static final Set<String> itemFields;
    private static final Set<String> imageFields;
    private final GloveEmbeddings embeddings;

    /**
     * Constructor for the ClassifyJSONcomponents class.
     */
    public ClassifyJSONcomponents(){
        embeddings = new GloveEmbeddings();
    }

    /**
     * Static block to load the field mappings from the metadata.json file.
     */
    static {
        FieldMappingLoader loader = new FieldMappingLoader("src/main/java/org/example/utils/metadata.json");
        fieldMappings = loader.getFieldMappings();
        itemFields = loader.getItemFields();
        imageFields = loader.getImageFields();
    }

    /**
     * This method classifies a JSON object as an item, image, ambiguous or unknown based on the fields present in the object.
     *
     * @param jsonArray The JSON object to be classified (JsonObject).
     * @return The classification of the JSON object (String).
        */
    public String classifyJsonArray(JsonArray jsonArray) {
        if (jsonArray.size() == 0) return "unknown";
//       System.out.println("item fields : "+itemFields + " image fields : "+imageFields);
        JsonObject firstElement = jsonArray.get(0).getAsJsonObject();
        boolean isItem = matchItemFields(firstElement);
        boolean isImage = matchImageFields(firstElement);
//      System.out.println(embeddings.cosineSimilarity("images", "image") + "#######################");

        if (isItem && isImage) {
            return "ambiguous";
        } else if (isItem) {
            return "item";
        } else if (isImage) {
            return "image";
        }
        return "unknown";
    }

    /**
     * This method checks if a JSON object matches the expected fields for an item in the feed data.
     *
     * @param jsonObject The JSON object to be checked (JsonObject).
     * @return True if the object matches item fields, false otherwise (boolean).
     */
    private boolean matchItemFields(JsonObject jsonObject) {
        long matches = 0;
        Set<String> mapped = Set.of();
        for (String key : fieldMappings.keySet()) {
            if(key.toLowerCase() != "imgurl" && key.toLowerCase() != "imgtype" && key.toLowerCase() != "imglength" && key.toLowerCase() != "imgwidth") {
                Set<String> fields = fieldMappings.get(key);
                for (String field : fields) {
                    if (jsonObject.has(field)) {
                        matches++;
//                        mapped.add(key);
                        break;
                    }
                }
            }
        }

        /**
         * This block of code is used to check the similarity between the tags in the JSON object and the field mappings.
         */
//        for (String tags : jsonObject.keySet()) {
//            double max_similarity = 0.0;
//            for(String  key: fieldMappings.keySet()) {
//                if(!mapped.contains(key)) {
//                    double max_local = 0.0;
//                    for(String field: fieldMappings.get(key)) {
//                        double similarity = embeddings.cosineSimilarity(tags, field);
//                        if(similarity > max_local) {
//                            max_local = similarity;
//                        }
//                    }
//                    if(max_local > max_similarity) {
//                        System.out.println("Max Similarity: " + max_local + " for " + tags + " and " + key);
//                        max_similarity = max_local;
//                    }
//                }
//            }
//            if(max_similarity > 0.8) {
//                matches++;
//            }
//        }

        double totalParams = fieldMappings.size() - 4.0;
        double matchRatio = (double) matches / totalParams;
        System.out.println("Item Field Match Count: " + matches);

        return matchRatio >= 0.8;

    }


    /**
     * This method checks if a JSON object matches the expected fields for an image in the feed data.
     *
     * @param jsonObject The JSON object to be checked (JsonObject).
     * @return True if the object matches image fields, false otherwise (boolean).
     */
    private boolean matchImageFields(JsonObject jsonObject) {
        Set<String> imageFields = fieldMappings.getOrDefault("image", Set.of());
        long matches = imageFields.stream()
                .filter(jsonObject::has)
                .count();

        double totalParams = fieldMappings.size() - 8.0;
        double matchRatio = (double) matches / totalParams;
        boolean urlMatch = jsonObject.has("url") && jsonObject.get("url").getAsString().matches(".*\\.(jpeg|jpg|png|gif)$");
//        System.out.println("Image Field Match Count: " + matches + ", Ratio: " + matchRatio + ", URL Match: " + urlMatch);
        return matchRatio >= 0.6 && urlMatch;
    }
}


