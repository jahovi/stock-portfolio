package charts;

/**
 * The abstract parent class of the indicators.
 * It contains their commonality: that they have a type (the available types are specified in the enum IndicatorTypes: 
 * currently either BOLLINGER_BANDS or MOVING_AVERAGE).
 */
public abstract class Indicator {
    
    /** The indicator type. */
    public final IndicatorTypes type;

    /**
     * This constructor is called by the subclass constructors to set the indicator type.
     *
     * @param type the indicator type
     */
    protected Indicator(IndicatorTypes type) {
        this.type = type;
    }

}
