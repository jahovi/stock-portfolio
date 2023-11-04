
package charts;

/**
 * This enum type is used in the Alarm class to define whether an alarm threshold price is a lower or an upper bound,
 * in order to decide whether it is to be triggered when the stock price crosses from below or from above.
 */
public enum AlarmType {
    
    /** This constant marks an alarm price as an upper threshold. 
     * An alarm with this alarm type is triggered when the stock price crosses form below.*/
    UPPER_BOUND, 
    /** This constant marks an alarm price as a lower threshold. 
     * An alarm with this alarm type is triggered when the stock price crosses form above. */
    LOWER_BOUND;
}
