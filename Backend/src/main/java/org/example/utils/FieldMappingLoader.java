package org.example.utils;

import com.google.gson.JsonArray;
import com.google. gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class loads field mappings from a JSON file and provides methods to access the mappings.
 */
public class FieldMappingLoader {
    private Map<String, Set<String>> fieldMappings = new HashMap<>();

    /**
     * Constructor that loads field mappings from a JSON file.
     * @param filePath the path to the JSON file containing field mappings
     */
    public FieldMappingLoader(String filePath) {
        loadFieldMappings(filePath);
    }

    /**
     * Loads field mappings from a JSON file.
     * @param filePath the path to the JSON file containing field mappings
     */
    private void loadFieldMappings(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject metadata = JsonParser.parseReader(reader).getAsJsonObject();
            metadata.entrySet().forEach(entry -> {
                JsonArray jsonArray = entry.getValue().getAsJsonArray();
                Set<String> fields = jsonArrayToSet(jsonArray);
                fieldMappings.put(entry.getKey(), fields);
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load field mappings from " + filePath, e);
        }
    }

    /**
     * Converts a JSON array to a set of strings.
     * @param jsonArray the JSON array to convert
     * @return a set of strings
     */
    private Set<String> jsonArrayToSet(JsonArray jsonArray) {
        Set<String> result = new HashSet<>();
        jsonArray.forEach(element -> result.add(element.getAsString()));
        return result;
    }

    /**
     * Returns a copy of the field mappings.
     * @return a copy of the field mappings
     */
    public Map<String, Set<String>> getFieldMappings() {
        return new HashMap<>(fieldMappings);
    }

    /**
     * Returns the set of fields that a given field maps to.
     * @param fieldName the field name
     * @return the set of fields that the given field maps to
     */
    public Set<String> getMapping(String fieldName) {
        return fieldMappings.getOrDefault(fieldName, new HashSet<>());
    }

    /**
     * Returns the set of fields that are mapped to by item field.
     * @return the set of fields that are mapped to by item field
     */
    public Set<String> getItemFields() {
        Set<String> itemFields = new HashSet<>();
        fieldMappings.forEach((key, values) -> {
            if (!key.equals("imgURL") && !key.equals("imgType") && !key.equals("imgLength") && !key.equals("imgWidth")) {
                itemFields.addAll(values);
            }
        });
        return itemFields;
    }

    /**
     * Returns the set of fields that are mapped to by image fields.
     * @return the set of fields that are mapped to by image fields
     */
    public Set<String> getImageFields() {
        Set<String> imageFields = new HashSet<>();
        imageFields.addAll(fieldMappings.getOrDefault("imgURL", new HashSet<>()));
        imageFields.addAll(fieldMappings.getOrDefault("imgType", new HashSet<>()));
        imageFields.addAll(fieldMappings.getOrDefault("imgLength", new HashSet<>()));
        imageFields.addAll(fieldMappings.getOrDefault("imgWidth", new HashSet<>()));
        return imageFields;
    }
}

