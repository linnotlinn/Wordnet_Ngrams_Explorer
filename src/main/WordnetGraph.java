package main;

import edu.princeton.cs.algs4.In;
import java.util.*;

/**
 * A graph representation of WordNet data for finding hyponyms.
 * Maps words to their synset IDs and provides traversal capabilities.
 */

 
public class WordnetGraph {
    public final Map<String, List<Integer>> wordToIds;
    public final DiGraph graph;
    public final List<String> synsetStrings;

    /**
     * Constructs a WordnetGraph from synset and hyponym data files.
     */
    public WordnetGraph(String synsetsFile, String hyponymsFile) {
        if (synsetsFile == null || synsetsFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Synsets file path cannot be null or empty");
        }
        if (hyponymsFile == null || hyponymsFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Hyponyms file path cannot be null or empty");
        }

        this.wordToIds = new HashMap<>();
        this.synsetStrings = new ArrayList<>();
        
        loadSynsets(synsetsFile);
        this.graph = buildGraph(hyponymsFile);
    }

    /**
     * Loads synset data from the specified file.
     */
    private void loadSynsets(String synsetsFile) {
        In synsetReader = new In(synsetsFile);
        
        while (synsetReader.hasNextLine()) {
            String[] line = synsetReader.readLine().split(",");
            int synsetId = Integer.parseInt(line[0]);
            String synsetString = line[1];
            
            // Ensure the list is large enough
            while (synsetStrings.size() <= synsetId) {
                synsetStrings.add(null);
            }
            synsetStrings.set(synsetId, synsetString);

            // Create a map associating each word with its indices
            String[] words = synsetString.split(" ");
            for (String w : words) {
                if (wordToIds.containsKey(w)) {
                    List<Integer> indexList = wordToIds.get(w);
                    indexList.add(synsetId);
                    wordToIds.put(w, indexList);
                } else {
                    List<Integer> indexList = new ArrayList<>();
                    indexList.add(synsetId);
                    wordToIds.put(w, indexList);
                }
            }
        }
    }

    /**
     * Builds the directed graph from hyponym relationships.
     * @return the constructed DiGraph
     */
    private DiGraph buildGraph(String hyponymsFile) {
        int vertexCount = synsetStrings.size();
        DiGraph graph = new DiGraph(vertexCount);

        In hyponymReader = new In(hyponymsFile);
        while (hyponymReader.hasNextLine()) {
            String[] line = hyponymReader.readLine().split(",");
            int hypernymId = Integer.parseInt(line[0]);
            
            for (int i = 1; i < line.length; i++) {
                int hyponymId = Integer.parseInt(line[i]);
                graph.addEdge(hypernymId, hyponymId);
            }
        }
        
        return graph;
    }

    /**
     * Finds all hyponyms of the specified word.
     * 
     * @param word the word to find hyponyms for
     * @return a set of hyponym words
     */
    public Set<String> findHyponyms(String word) {
        List<Integer> synsetIds = wordToIds.get(word);
        if (synsetIds == null || synsetIds.isEmpty()) {
            return new TreeSet<>();
        }

        // Find all reachable synset IDs
        Set<Integer> reachableSynsetIds = new TreeSet<>();
        for (Integer synsetId : synsetIds) {
            reachableSynsetIds.addAll(graph.getReachableVertices(synsetId));
        }

        // Convert synset IDs to words
        Set<String> hyponymWords = new TreeSet<>();
        for (Integer synsetId : reachableSynsetIds) {
            if (synsetId < synsetStrings.size() && synsetStrings.get(synsetId) != null) {
                String[] words = synsetStrings.get(synsetId).split(" ");
                hyponymWords.addAll(Arrays.asList(words));
            }
        }
        
        return hyponymWords;
    }

}




