package charts;

/**
 * The constants in this enumeration identify a chart interval. They are used for communication between the 
 * chart model and the controller as to what type of data to request from the date provider.
 */
public enum Intervals {
    ONE, FIVE, FIFTEEN, THIRTY, SIXTY, DAY, WEEK, MONTH;    
    
}