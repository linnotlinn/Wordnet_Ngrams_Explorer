package plotting;

import ngrams.TimeSeries;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Utility class for creating and manipulating charts for the Ngordnet application.
 * Provides functionality to generate time series charts and encode them for web display.
 * 
 * @author CS61B Staff
 * @version 2024
 */
public class Plotter {
    private static final Logger logger = LoggerFactory.getLogger(Plotter.class);
    
    // Chart configuration constants
    private static final int DEFAULT_CHART_WIDTH = 800;
    private static final int DEFAULT_CHART_HEIGHT = 600;
    private static final String IMAGE_FORMAT = "png";
    
    /**
     * Generates a time series chart from a list of words and their corresponding TimeSeries data.
     * Each word is plotted as a separate series on the same chart.
     * 
     * @param words the list of words to plot
     * @param lts the list of TimeSeries objects corresponding to each word
     * @return an XYChart object ready for display or encoding
     * @throws IllegalArgumentException if the lists have different sizes or contain null values
     */
    public static XYChart generateTimeSeriesChart(List<String> words, List<TimeSeries> lts) {
        logger.debug("Generating time series chart for {} words", words.size());
        
        // Validate input parameters
        validateChartInputs(words, lts, "TimeSeries");
        
        XYChart chart = new XYChart(DEFAULT_CHART_WIDTH, DEFAULT_CHART_HEIGHT);
        
        try {
            for (int i = 0; i < words.size(); i += 1) {
                TimeSeries ts = lts.get(i);
                String word = words.get(i);
                
                // Validate individual series data
                if (ts == null) {
                    logger.warn("TimeSeries at index {} is null, skipping", i);
                    continue;
                }
                
                if (ts.years().isEmpty() || ts.data().isEmpty()) {
                    logger.warn("TimeSeries for word '{}' has no data, skipping", word);
                    continue;
                }
                
                chart.addSeries(word, ts.years(), ts.data());
                logger.debug("Added series for word: {}", word);
            }
            
            logger.info("Successfully generated time series chart with {} series", chart.getSeriesMap().size());
            return chart;
            
        } catch (Exception e) {
            logger.error("Error generating time series chart", e);
            throw new RuntimeException("Failed to generate time series chart: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a chart from a list of words and their corresponding TreeMap data.
     * Each word is plotted as a separate series using the TreeMap's key-value pairs.
     * 
     * @param words the list of words to plot
     * @param lts the list of TreeMap objects containing year-value pairs
     * @return an XYChart object ready for display or encoding
     * @throws IllegalArgumentException if the lists have different sizes or contain null values
     */
    public static XYChart generateTreeMapChart(List<String> words, List<TreeMap<Integer, Double>> lts) {
        logger.debug("Generating TreeMap chart for {} words", words.size());
        
        // Validate input parameters
        validateChartInputs(words, lts, "TreeMap");
        
        XYChart chart = new XYChart(DEFAULT_CHART_WIDTH, DEFAULT_CHART_HEIGHT);
        
        try {
            for (int i = 0; i < words.size(); i += 1) {
                TreeMap<Integer, Double> tm = lts.get(i);
                String word = words.get(i);
                
                // Validate individual series data
                if (tm == null) {
                    logger.warn("TreeMap at index {} is null, skipping", i);
                    continue;
                }
                
                if (tm.isEmpty()) {
                    logger.warn("TreeMap for word '{}' has no data, skipping", word);
                    continue;
                }
                
                chart.addSeries(word, new ArrayList<>(tm.keySet()), new ArrayList<>(tm.values()));
                logger.debug("Added series for word: {} with {} data points", word, tm.size());
            }
            
            logger.info("Successfully generated TreeMap chart with {} series", chart.getSeriesMap().size());
            return chart;
            
        } catch (Exception e) {
            logger.error("Error generating TreeMap chart", e);
            throw new RuntimeException("Failed to generate TreeMap chart: " + e.getMessage(), e);
        }
    }

    /**
     * Displays a chart in a Swing window.
     * This method is primarily used for debugging and development purposes.
     * 
     * @param chart the XYChart to display
     * @throws IllegalArgumentException if the chart is null
     */
    public static void displayChart(XYChart chart) {
        if (chart == null) {
            logger.error("Cannot display null chart");
            throw new IllegalArgumentException("Chart cannot be null");
        }
        
        logger.info("Displaying chart with {} series", chart.getSeriesMap().size());
        try {
            new SwingWrapper(chart).displayChart();
        } catch (Exception e) {
            logger.error("Error displaying chart", e);
            throw new RuntimeException("Failed to display chart: " + e.getMessage(), e);
        }
    }

    /**
     * Encodes a chart as a Base64 string for web transmission.
     * Converts the chart to a PNG image and encodes it as a Base64 string.
     * 
     * @param chart the XYChart to encode
     * @return a Base64 encoded string representation of the chart image
     * @throws IllegalArgumentException if the chart is null
     * @throws RuntimeException if encoding fails
     */
    public static String encodeChartAsString(XYChart chart) {
        if (chart == null) {
            logger.error("Cannot encode null chart");
            throw new IllegalArgumentException("Chart cannot be null");
        }
        
        logger.debug("Encoding chart as Base64 string");
        
        try {
            BufferedImage img = BitmapEncoder.getBufferedImage(chart);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            
            boolean writeSuccess = ImageIO.write(img, IMAGE_FORMAT, os);
            if (!writeSuccess) {
                logger.error("Failed to write image in {} format", IMAGE_FORMAT);
                throw new IOException("ImageIO.write returned false");
            }
            
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            logger.debug("Successfully encoded chart as Base64 string ({} bytes)", encodedImage.length());
            return encodedImage;
            
        } catch (IOException e) {
            logger.error("Error encoding chart as Base64 string", e);
            throw new RuntimeException("Failed to encode chart: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error encoding chart", e);
            throw new RuntimeException("Unexpected error encoding chart: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validates that the input lists for chart generation are valid.
     * Checks for null lists, matching sizes, and null elements.
     * 
     * @param words the list of words
     * @param data the list of data objects (TimeSeries or TreeMap)
     * @param dataType the type of data for error messages
     * @throws IllegalArgumentException if validation fails
     */
    private static void validateChartInputs(List<String> words, List<?> data, String dataType) {
        if (words == null) {
            logger.error("Words list is null");
            throw new IllegalArgumentException("Words list cannot be null");
        }
        
        if (data == null) {
            logger.error("{} data list is null", dataType);
            throw new IllegalArgumentException(dataType + " data list cannot be null");
        }
        
        if (words.size() != data.size()) {
            logger.error("Mismatched list sizes: words={}, {}={}", words.size(), dataType, data.size());
            throw new IllegalArgumentException(
                String.format("List of words (%d) and List of %s objects (%d) must be the same length", 
                            words.size(), dataType, data.size())
            );
        }
        
        // Check for null elements
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i) == null) {
                logger.error("Word at index {} is null", i);
                throw new IllegalArgumentException("Words list cannot contain null elements");
            }
        }
        
        logger.debug("Input validation passed for {} words", words.size());
    }
}
