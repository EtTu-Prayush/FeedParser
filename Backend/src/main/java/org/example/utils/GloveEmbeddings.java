package org.example.utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * GloveEmbeddings class is used to load GloVe embeddings from a file and calculate cosine similarity between words.
 * The class is used to calculate the cosine similarity between two words based on their GloVe embeddings.
 */
public class GloveEmbeddings {

    private final Map<String, double[]> embeddings;

    /**
     * Constructor to load GloVe embeddings from a file.
     */
    public GloveEmbeddings() {
        embeddings = new HashMap<>();
        try {
            loadEmbeddings("src/main/java/org/example/utils/glove.6B.50d.txt");
        } catch (IOException e) {
            System.err.println("Error loading GloVe embeddings: " + e.getMessage());
        }
    }

    /**
     * Method to load GloVe embeddings from a file.
     * @param filePath Path to the file containing GloVe embeddings.
     * @throws IOException If an I/O error occurs.
     */
    private void loadEmbeddings(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String word = parts[0];
                double[] embedding = new double[parts.length - 1];
                for (int i = 1; i < parts.length; i++) {
                    embedding[i - 1] = Double.parseDouble(parts[i]);
                }
                embeddings.put(word.toLowerCase(), embedding);
            }
        }
    }

    /**
     * Method to calculate the cosine similarity between two words.
     * @param word1 First word.
     * @param word2 Second word.
     * @return Cosine similarity between the two words.
     */
    public double cosineSimilarity(String word1, String word2) {
        if (!embeddings.containsKey(word1.toLowerCase()) || !embeddings.containsKey(word2.toLowerCase())) {
            System.out.println("Word not found: " + (embeddings.containsKey(word1.toLowerCase()) ? word2 : word1)); // Debugging statement for missing words
            return 0.0;
        }

        double[] embedding1 = embeddings.get(word1.toLowerCase());
        double[] embedding2 = embeddings.get(word2.toLowerCase());

        double dotProduct = 0.0;
        double norm1 = 0.0, norm2 = 0.0;
        for (int i = 0; i < embedding1.length; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += Math.pow(embedding1[i], 2);
            norm2 += Math.pow(embedding2[i], 2);
        }

        double denominator = Math.sqrt(norm1) * Math.sqrt(norm2);
        return denominator > 0 ? dotProduct / denominator : 0.0; // Avoid division by zero
    }
}
