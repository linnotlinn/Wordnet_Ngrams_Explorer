package main;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.*;

/** 
 * Handler for hyponym queries that generates a list of hyponyms for a given word.
 * Processes word frequency data and returns encoded list of hyponyms.
 */

public class HyponymsHandler extends NgordnetQueryHandler {

    public WordnetGraph wordnetGraph;
    public String wordsFile;
    public String countsFile;
    public NGramMap ngm;

/*
 * Constructor for HyponymsHandler
 */
    public HyponymsHandler(WordnetGraph graph, NGramMap nGramMap) {
        wordnetGraph = graph;
        ngm = nGramMap;
    }
/*
 * handle method for hyponyms query that returns a list of hyponyms for a given word
 */
    @Override
    public String handle(NgordnetQuery q) {
        // get the words, start year, end year, and k from the query
        List<String> label = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        
        String word = label.get(0);
        if (wordnetGraph.wordToIds.get(word) == null){
            return Collections.emptyList().toString();
        }
        else {
            Set<String> wordPath = wordnetGraph.findHyponyms(word);

            // find the shared hyponyms between the words in the query
            for (int i = 1; i < label.size(); i++) {
                String w = label.get(i);
                if (wordnetGraph.wordToIds.get(w) != null) {
                    wordPath.retainAll(wordnetGraph.findHyponyms(w));
                } else {
                    // If any word doesn't exist, return empty list
                    return Collections.emptyList().toString();
                }
            }

            if (k != 0) {
                // pick out k hyponyms based on frequency from startYear to endYear
                TreeMap<Double, List<String>> map = KHyponymsSorting(wordPath, startYear, endYear);
                List<String> ListofK = new ArrayList<>();

                // Get all hyponyms with their frequencies
                for (Map.Entry<Double, List<String>> entry : map.entrySet()) {
                    double count = entry.getKey();
                    if (count == 0) {
                        break;
                    }
                    List<String> wordsWithThisCount = entry.getValue();
                    for (String wordWithCount : wordsWithThisCount) {
                        if (ListofK.size() < k) {
                            ListofK.add(wordWithCount);
                        } else {
                            break;
                        }
                    }
                    if (ListofK.size() >= k) {
                        break;
                    }
                }

                if (ListofK.isEmpty()) {
                    return Collections.emptyList().toString();
                } else {
                    Collections.sort(ListofK);
                    return ListofK.toString();
                }
            }
            else {
                ArrayList<String> List = new ArrayList<>(wordPath);
                Collections.sort(List);
                return List.toString();
            }
        }
    }

/*
 * Helper method to sort the hyponyms by frequency
 */
    private TreeMap<Double, List<String>> KHyponymsSorting(Set<String> wordPath, int startYear, int endYear) {

        // make sure that the frequencyMap is ordered from large keys to small keys
        TreeMap<Double, List<String>> frequencyMap = new TreeMap<>(Collections.reverseOrder());
        
        // add a frequency and a word to the map if the frequency doesn't already exist in the map;
        // otherwise update the word list associated with the frequency 
        for (String w: wordPath) {
            TimeSeries wordFrequency = ngm.countHistory(w, startYear, endYear);

            // add together all the frequencies for the word
            List<Double> frequency = wordFrequency.data();
            double f = 0;
            for (double o: frequency){
                f = f + o;
            }

            if (frequencyMap.get(f) != null) {
                frequencyMap.get(f).add(w);
                Collections.sort(frequencyMap.get(f));
            } else {
                List<String> wordList = new ArrayList<>();
                wordList.add(w);
                frequencyMap.put(f, wordList);
            }
        }
        return frequencyMap;
    }

}
