package charts;

/**
 * Exceptions of this type are thrown by the chart model when the user requests more data points than the data provider provides.
 * It contains the maximum number of intervals allowed. This information can be used by the catching chart frame to set 
 * the number of intervals text box, where the user enters the desired number of intervals, to this maximum value.
 */
public class TooManyIntervalsException extends Exception {
    private int maximumIntervalNumber;
    
    /**
     * Constructs an exception object and stores in it the maximum allowed number of intervals.
     *
     * @param maximumIntervalNumber the maximum allowed number of intervals 
     */
    public TooManyIntervalsException(int maximumIntervalNumber) {
        this.maximumIntervalNumber = maximumIntervalNumber;
    }

    /**
     * Gets the maximum allowed interval number.
     *
     * @return the maximum allowed interval number
     */
    public int getMaximumIntervalNumber() {
        return maximumIntervalNumber;
    }
}
