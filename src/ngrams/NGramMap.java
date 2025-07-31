package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 */
public class NGramMap {


    public TreeMap<String, TimeSeries> wordmap = new TreeMap<>();
    public TimeSeries countmap = new TimeSeries();



    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {

        In words = new In(wordsFilename);

        while (words.hasNextLine()){
            String[] nextLine = words.readLine().split("\t");
            String word = nextLine[0];
            int year = Integer.parseInt(nextLine[1]);
            double number = Double.parseDouble(nextLine[2]);
            TimeSeries t = new TimeSeries();
            t.put(year, number);
            if (wordmap.get(word) == null){
                wordmap.put(word, t);
            } else {
                t = wordmap.get(word).plus(t);
                wordmap.put(word, t);
            }
        }

        In counts = new In(countsFilename);

        while(counts.hasNextLine()){
            String[] nextLine = counts.readLine().split(",");
            int year = Integer.parseInt(nextLine[0]);
            double number = Double.parseDouble(nextLine[1]);
            if (countmap.get(year) == null){
                countmap.put(year, number);
            } else{
                TimeSeries t = new TimeSeries();
                t.put(year, number);
                countmap.plus(t);
            }
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {

        if (wordmap.get(word) == null) {
            return new TimeSeries();
        } else {
            return copy(word, startYear, endYear);
        }
    }

    private TimeSeries copy(String word, int startYear, int endYear){
        TimeSeries copy = new TimeSeries();
        int year = startYear;
        while (year <= endYear){
            if(wordmap.get(word).containsKey(year)){
                copy.put(year, wordmap.get(word).get(year));
            }
            year += 1;
        }
        return copy;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {

        if (wordmap.get(word) == null) {
            return new TimeSeries();
        } else {
            List<Integer> years = wordmap.get(word).years();
            int startYear = years.get(0);
            int endYear = years.get(years.size()-1);
            return copy(word, startYear, endYear);
        }
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries t = new TimeSeries();
        List<Integer> years = countmap.years();
        for (int i = 0; i < years.size(); i += 1){
            int year = years.get(i);
            double number = countmap.get(year);
            t.put(year, number);
        }
        return t;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {

        if (wordmap.get(word) == null){
            return new TimeSeries();
        }
        TimeSeries t = countHistory(word, startYear, endYear);
        TimeSeries weightedt = t.dividedBy(countmap);
        return weightedt;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (wordmap.get(word) == null){
            return new TimeSeries();
        }
        TimeSeries t = countHistory(word);
        TimeSeries weightedt = t.dividedBy(countmap);
        return weightedt;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        Iterator<String> iter= words.iterator();
        TimeSeries collection = new TimeSeries();

        while (iter.hasNext()){
            String word = iter.next();
            TimeSeries t = weightHistory(word, startYear, endYear);
            collection = collection.plus(t);
        }
        return collection;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        Iterator<String> iter= words.iterator();
        TimeSeries collection = new TimeSeries();

        while (iter.hasNext()){
            String word = iter.next();
            TimeSeries t = weightHistory(word);
            collection = collection.plus(t);
        }
        return collection;
    }

}
