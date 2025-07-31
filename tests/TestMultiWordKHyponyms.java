import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import edu.princeton.cs.algs4.StdRandom;
import main.AutograderBuddy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** Tests the case where the list of words is length greater than 1, and k is not zero. */
public class TestMultiWordKHyponyms {
    // ngrams files
    public static final String VERY_SHORT_WORDS_FILE = "data/ngrams/very_short.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    private static final String SMALL_WORDS_FILE = "data/ngrams/top_14377_words.csv";
    private static final String WORDS_FILE = "data/ngrams/top_49887_words.csv";


    // wordnet Files
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    public static final String LARGE_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String LARGE_HYPONYM_FILE = "data/wordnet/hyponyms.txt";
    private static final String HYPONYMS_FILE_SUBSET = "data/wordnet/hyponyms1000-subgraph.txt";
    private static final String SYNSETS_FILE_SUBSET = "data/wordnet/synsets1000-subgraph.txt";

    // EECS files
    private static final String FREQUENCY_EECS_FILE = "data/ngrams/frequency-EECS.csv";
    private static final String HYPONYMS_EECS_FILE = "data/wordnet/hyponyms-EECS.txt";
    private static final String SYNSETS_EECS_FILE = "data/wordnet/synsets-EECS.txt";

    @Test

    public void TestMultiWord() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                FREQUENCY_EECS_FILE, TOTAL_COUNTS_FILE, SYNSETS_EECS_FILE, HYPONYMS_EECS_FILE);
        List<String> words = new ArrayList<>();
        words.add("CS61A");

        NgordnetQuery nq = new NgordnetQuery(words, 2010, 2020, 4);
        String actual = studentHandler.handle(nq);
        String expected = "[CS170, CS61A, CS61B, CS61C]";
        assertThat(actual).isEqualTo(expected);
    }



    @Test
    public void TestMultiWordLarge() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(SMALL_WORDS_FILE, TOTAL_COUNTS_FILE
        , LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
        List<String> words = new ArrayList<>();
        words.add("food");
        words.add("cake");

        NgordnetQuery nq = new NgordnetQuery(words, 1950, 1990, 5);
        String actual = studentHandler.handle(nq);
        String expected = "[cake, cookie, kiss, snap, wafer]";
        assertThat(actual).isEqualTo(expected);

    }
}
