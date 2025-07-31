package main;

import browser.NgordnetServer;
import ngrams.NGramMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    // NGram data files
    public static final String VERY_SHORT_WORDS_FILE = "data/ngrams/very_short.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    private static final String WORDS_FILE = "data/ngrams/top_49887_words.csv";
   
    // WordNet data files
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    public static final String LARGE_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String LARGE_HYPONYM_FILE = "data/wordnet/hyponyms.txt";
   
    // Server configuration
    private static final int DEFAULT_PORT = 4567;
    private static final String SERVER_URL = "http://localhost:" + DEFAULT_PORT + "/ngordnet.html";

    /**
     * main entry point for the application.
     * initializes the WordNet graph, NGram map, and starts the web server.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        logger.info("Starting Ngordnet server...");
        
        try {
            // Initialize data structures
            WordnetGraph wordnetGraph = new WordnetGraph(LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
            NGramMap ngramMap = new NGramMap(WORDS_FILE, TOTAL_COUNTS_FILE);
            
            // Start server and register handlers
            NgordnetServer server = new NgordnetServer();
            server.startUp();
            
            registerHandlers(server, wordnetGraph, ngramMap);
            
            logger.info("Server startup complete! Visit {}", SERVER_URL);
            System.out.println("Finished server startup! Visit " + SERVER_URL);
            
        } catch (Exception e) {
            logger.error("Failed to start server", e);
            System.err.println("Error starting server: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * registers all query handlers with the server.
     * 
     * @param server the NgordnetServer instance
     * @param wordnetGraph the WordnetGraph
     * @param ngramMap the NGramMap
     */
    private static void registerHandlers(NgordnetServer server, WordnetGraph wordnetGraph, NGramMap ngramMap) {
        server.register("history", new HistoryHandler(ngramMap));
        server.register("historytext", new HistoryTextHandler(ngramMap));
        server.register("hyponyms", new HyponymsHandler(wordnetGraph, ngramMap));
    }
}