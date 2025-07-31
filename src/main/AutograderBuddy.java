package main;

import browser.NgordnetQueryHandler;
import ngrams.NGramMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for creating query handlers for autograder testing.
 * Provides factory methods to instantiate handlers with proper configuration.
 * 
 * @author Your Name
 */
public class AutograderBuddy {
    private static final Logger logger = LoggerFactory.getLogger(AutograderBuddy.class);

    /**
     * Creates and returns a HyponymsHandler configured with the specified data files.
     * 
     * @param wordFile path to the word frequency data file
     * @param countFile path to the total counts data file
     * @param synsetFile path to the synsets data file
     * @param hyponymFile path to the hyponyms data file
     * @return a configured HyponymsHandler instance
     * @throws IllegalArgumentException if any file path is null or empty
     * @throws RuntimeException if there's an error initializing the WordNet graph or handler
     */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        
        validateFilePaths(wordFile, countFile, synsetFile, hyponymFile);
        
        try {
            logger.debug("Creating HyponymsHandler with files: wordFile={}, countFile={}, synsetFile={}, hyponymFile={}", 
                        wordFile, countFile, synsetFile, hyponymFile);
            
            NGramMap ngm = new NGramMap(wordFile, countFile);
            WordnetGraph wordnetGraph = new WordnetGraph(synsetFile, hyponymFile);
            return new HyponymsHandler(wordnetGraph, ngm);
            
        } catch (Exception e) {
            logger.error("Failed to create HyponymsHandler", e);
            throw new RuntimeException("Failed to initialize HyponymsHandler", e);
        }
    }
    
    /**
     * Validates that all provided file paths are non-null and non-empty.
     * 
     * @param wordFile word frequency file path
     * @param countFile total counts file path
     * @param synsetFile synsets file path
     * @param hyponymFile hyponyms file path
     * @throws IllegalArgumentException if any file path is invalid
     */
    private static void validateFilePaths(String wordFile, String countFile, 
                                        String synsetFile, String hyponymFile) {
        if (wordFile == null || wordFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Word file path cannot be null or empty");
        }
        if (countFile == null || countFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Count file path cannot be null or empty");
        }
        if (synsetFile == null || synsetFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Synset file path cannot be null or empty");
        }
        if (hyponymFile == null || hyponymFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Hyponym file path cannot be null or empty");
        }
    }
}
