package org.example.Classifiers;
import org.json.JSONObject;


// This class is named ClassifyJSON and its purpose is to check if a JSON string adheres to a standard format for feed data.
public class ClassifyJSON {

    /**
     * This method checks if the provided JSON content adheres to a standard format for feed data.
     *
     * @param jsonContent The JSON content to be checked.
     * @return True if the JSON content adheres to the standard format, false otherwise.
     */
    public boolean isStandardJSON(String jsonContent) {
        JSONObject json = new JSONObject(jsonContent);

        boolean hasRequiredFields = json.has("version") && json.has("title") && json.has("items");
        boolean hasRecommendedFields = json.has("home_page_url") && json.has("feed_url");
        boolean hasOptionalFields = json.has("description") || json.has("icon") || json.has("favicon");
        boolean validItemsStructure = json.has("items") && json.getJSONArray("items").length() > 0;

        if (validItemsStructure) {
            JSONObject firstItem = json.getJSONArray("items").getJSONObject(0);
            validItemsStructure = firstItem.has("id") && (firstItem.has("content_html") || firstItem.has("content_text"));
        }

        return hasRequiredFields && (hasRecommendedFields || hasOptionalFields || validItemsStructure);
    }
}
