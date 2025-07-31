package ngrams;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;



    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        TimeSeries copy = new TimeSeries();
        int year = startYear;
        while (year <= endYear){
            if(ts.containsKey(year)){
                copy.put(year, ts.get(year));
            }
            year += 1;
        }
    }

    /**
     *  Returns all years for this time series in ascending order.
     */
    public List<Integer> years() {

        Set<Integer> x = keySet();
        int size = size();
        Iterator iter = x.iterator();
        List<Integer> arr = new ArrayList<>(size);
        for (int i = 0; i < size; i += 1){
            arr.add((Integer) iter.next());
        }
        return arr;
    }

    /**
     *  Returns all data for this time series. Must correspond to the
     *  order of years().
     */
    public List<Double> data() {

        Collection<Double> y = values();
        int size = size();
        Iterator iter = y.iterator();
        List<Double> arr = new ArrayList<>(size);
        for (int i = 0; i < size; i += 1){
            arr.add((double)iter.next());
        }
        return arr;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {

        if (isEmpty() && ts.isEmpty()){
            return new TimeSeries();
        }
        List<Integer> allyears = allyears(years(), ts.years());
        TimeSeries t = new TimeSeries();
        for (int i = 0; i < allyears.size(); i += 1){
            int year = allyears.get(i);
            double data;
            if (ts.get(year) == null){
                data = get(year);
            } else if (get(year) == null){
                data = ts.get(year);}
            else{
            data = get(year) + ts.get(year);}
            t.put(year, data);
        }
        return t;
    }

    private List<Integer> allyears(List<Integer> a, List<Integer> b){
        List<Integer> c = new ArrayList<>();
        c.addAll(a);
        c.addAll(b);
        return c;
    }



    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {

        TimeSeries t = new TimeSeries();
        List<Integer> years = years();
        for (int i = 0; i < years.size(); i += 1){
            int year = years.get(i);
            double data;
            if (ts.get(year)== null){
                throw new IllegalArgumentException();
            } else{
                data = get(year) / ts.get(year);
            }
            t.put(year, data);
        }
        return t;
    }

}
