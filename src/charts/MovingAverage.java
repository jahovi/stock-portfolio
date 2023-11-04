package charts;

/**
 * Instances of this class store data for calculating Moving Average type indicators.
 */
public class MovingAverage extends Indicator {
    
    /** The number of data points to use for the calculation of this moving average. */
    public final int length;

    /**
     * Instantiates a new moving average.
     *
     * @param length The number of data points to use for the calculation of this moving average
     */
    public MovingAverage(int length) {
        super(IndicatorTypes.MOVING_AVERAGE);
        this.length = length;
    }

    /**
     * Returns a string representation for display in the indicators dialog, 
     * in the format MAlength.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "MA" + length;
    }
}
