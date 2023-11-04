package charts;

import main.Stock;

/**
 * Represents an alarm price. Alarms can be set by the user to be notified when a stock crosses a threshold price.
 * This data structure holds a threshold price and deducts whether that threshold is an upper or a lower bound. 
 * Upper bound alarms are triggered when the stock price crosses from below. Lower bound alarms are triggered when the stock price crosses from above. 
 * Alarm prices are defined by the user from the alarms dialog and associated with a stock.
 */
public class Alarm {
    
    /** The user defined threshold price. */
    public final double price;
    
    /** The alarm type. Records whether the threshold is an upper or a lower bound. Deducted automatically from the threshold and the current stock price.
     * Upper bound alarms are triggered when the stock price crosses from below. Lower bound alarms are triggered when the stock price crosses from above. */
    public final AlarmType alarmType;
    
    
    /**
     * Creates a new alarm price and deducts whether it is an upper or a lower bound.
     *
     * @param stock the stock this alarm is associated with.
     * @param price the threshold price that triggers the alarm when crossed
     */
    public Alarm(Stock stock, double price) {
        this.price = price;
        
        if (price >= stock.getPrice()) {
            alarmType = AlarmType.UPPER_BOUND;
        } else {
            alarmType = AlarmType.LOWER_BOUND;
        }
    }
}
