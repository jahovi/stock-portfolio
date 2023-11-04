package charts;

/**
 * This enumeration defines constants for the indicator types, 
 *  which are used to decide how to calculate and render an indicator in a chart.
 */
public enum IndicatorTypes {
    
    /** This constant identifies an indicator as a moving average. */
    MOVING_AVERAGE("Moving Average"), 
    /** This constant identifies an indicator as a Bollinger Bands type indicator. */
    BOLLINGER_BANDS("Bollinger Bands");
    
    private final String name;


    private IndicatorTypes(String name) {
        this.name = name;
    }
    
    /**
     * Gets the indicator type as a string for printing in the list of available indicators in the indicators dialog.
     *
     * @return the indicator type as a string
     */
    public String getName() {
        return name;
    }
}
