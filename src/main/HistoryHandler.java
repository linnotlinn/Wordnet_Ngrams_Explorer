package main;

import ngrams.NGramMap;
import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.TimeSeries;
import plotting.Plotter;
import org.knowm.xchart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for history queries that generates time series charts.
 * Processes word frequency data and returns encoded chart images.
 * 
 * @author Your Name
 */
public class HistoryHandler extends NgordnetQueryHandler {
    private static final Logger logger = LoggerFactory.getLogger(HistoryHandler.class);
    
    private final NGramMap ngramMap;

    /**
     * Constructs a new HistoryHandler with the specified NGram map.
     * 
     * @param ngramMap the NGram map containing word frequency data
     */
    public HistoryHandler(NGramMap ngramMap) {
        this.ngramMap = ngramMap;
    }

    /**
     * Handles history queries by generating time series charts for the specified words.
     * 
     * @param query the NgordnetQuery containing words and year range
     * @return encoded string representation of the generated chart
     */
    @Override
    public String handle(NgordnetQuery query) {
        try {
            List<String> words = query.words();
            int startYear = query.startYear();
            int endYear = query.endYear();
            
            logger.debug("Processing history query for words: {}, years: {} to {}", 
                        words, startYear, endYear);

            if (words == null || words.isEmpty()) {
                logger.warn("Empty word list provided to history handler");
                return "";
            }

            List<TimeSeries> timeSeriesList = buildTimeSeriesList(words, startYear, endYear);
            
            if (timeSeriesList.isEmpty()) {
                logger.warn("No valid time series data found for the provided words");
                return "";
            }

            XYChart chart = Plotter.generateTimeSeriesChart(words, timeSeriesList);
            return Plotter.encodeChartAsString(chart);
            
        } catch (Exception e) {
            logger.error("Error processing history query", e);
            return "";
        }
    }
    
    /**
     * Builds a list of TimeSeries objects for the given words and year range.
     * 
     * @param words the list of words to process
     * @param startYear the start year for the time series
     * @param endYear the end year for the time series
     * @return list of TimeSeries objects
     */
    private List<TimeSeries> buildTimeSeriesList(List<String> words, int startYear, int endYear) {
        List<TimeSeries> timeSeriesList = new ArrayList<>();
        
        for (String word : words) {
            if (word != null && !word.trim().isEmpty()) {
                TimeSeries timeSeries = ngramMap.weightHistory(word, startYear, endYear);
                if (!timeSeries.isEmpty()) {
                    timeSeriesList.add(timeSeries);
                } else {
                    logger.debug("No data found for word: {}", word);
                }
            }
        }
        
        return timeSeriesList;
    }
}
