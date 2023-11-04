package charts;

/**
 * Represents a Bollinger Bands stock chart indicator. This data structure holds values for length and 
 * standard deviation multiplier the user enters when creating new indicators in the indicators dialog of a chart frame.
 */
public class BollingerBands extends Indicator{
    
    /** The number of closing prices to use for the calculation. */
    public final int length;
    
    /** The standard deviation multiplier to be used for the calculation. */
    public final int standardDeviation;

    /**
     * Creates a new instance of bollinger bands.
     *
     * @param n The number of closing prices to use for the calculation.
     * @param f The standard deviation multiplier to use for the calculation.
     */
    public BollingerBands(int n, int f) {
        super(IndicatorTypes.BOLLINGER_BANDS);
        this.length = n;
        this.standardDeviation = f;
    }

    /**
     * Returns a string representation that is used to display a user created Bollinger Bands instance in
     * the list of existing indicators in the indicators dialog.
     *
     * @return the string representation in the form BB(standard deviation multiplier, length)
     */
    @Override
    public String toString() {
        return "BB" + "(" + standardDeviation + ", " + length + ")";
    }
}
