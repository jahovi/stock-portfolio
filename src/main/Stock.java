package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import charts.Alarm;
import charts.AlarmType;
import watchlist.ColorResetTimer;
import watchlist.PriceChangeDirection;

/**
 * Represents a stock. Holds all data that is received about the stock from the data provider
 * and derives further data from the data provided. Also contains information needed to 
 * draw colored cells in the watchlist: a change direction from which the color is derived, and a timer 
 * to reset that change direction. Also price alarms set by the user.
 */
public class Stock {
    // Idea: introduce isSubscribed parameter to reduce unnecessary subscription messages

    // parsed from symbol lookup
    private String description;
    private String displaySymbol;
    private String symbol;
    private String type;

    // parsed from quote and real time data
    private double price;
    private double previousClosePrice;
    private double openPrice;
    private long priceTimestamp;

    // calculated from previous close and current price
    private double priceChange;

    // Calculated from new price. Excluded from serialization
    transient private PriceChangeDirection changeDirection = PriceChangeDirection.CONSTANT;
    
    transient private ColorResetTimer colorResetTimer = new ColorResetTimer(this, null);

    private List<Alarm> alarms = new ArrayList<Alarm>();


    /**
     * Returns description, symbol, price and change as an object array
     * to be shown in the watchlist.
     *
     * @return an object array containing the specified data
     */
    public Object[] toObjectArray() {
        return new Object[] { description, symbol, price, priceChange };
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the display symbol.
     *
     * @return the display symbol
     */
    public String getDisplaySymbol() {
        return displaySymbol;
    }

    /**
     * Gets the symbol.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets a new price and derives the change to previous close in percent as well as the change direction for coloring the watchlist. Also checks if any alarms have been triggered by the price change.
     *
     * @param newPrice the new price
     */
    public void setPrice(final double newPrice) {
        if (newPrice > price) {
            changeDirection = PriceChangeDirection.RISING;
        } else if (newPrice < price) {
            changeDirection = PriceChangeDirection.FALLING;
        }
        price = newPrice;
        if (previousClosePrice != 0.0) {
            Stock.this.priceChange = calculateChangeToPreviousClose();
        }
        // check if new price triggers an alarm
        for (int i = 0; i < alarms.size(); i++) {
            if ((newPrice >= alarms.get(i).price && alarms.get(i).alarmType == AlarmType.UPPER_BOUND) || (newPrice <= alarms.get(i).price && alarms.get(i).alarmType == AlarmType.LOWER_BOUND)) {
                triggerAlarm(alarms.get(i));
            }
        }
    }

    private void triggerAlarm(Alarm alarm) {
        final String message;
        if (alarm.alarmType == AlarmType.UPPER_BOUND) {
            message = this.getDescription() + " rose above " + alarm.price;
        } else {
            message = this.getDescription() + " fell below " + alarm.price;
        }
        alarms.remove(alarm);
        JOptionPane.showMessageDialog(null, message, "Alarm triggered", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Gets the change to previous close in percent
     *
     * @return the change in percent
     */
    public double getChange() {
        return priceChange;
    }

    /**
     * Sets the change to previous close.
     *
     * @param change the new change in percent
     */
    public void setChange(double change) {
        this.priceChange = change;
    }

    /**
     * Gets the price timestamp.
     *
     * @return the price timestamp
     */
    public long getPriceTimestamp() {
        return priceTimestamp;
    }

    /**
     * Sets the price timestamp.
     *
     * @param priceTimestamp the new price timestamp
     */
    public void setPriceTimestamp(long priceTimestamp) {
        this.priceTimestamp = priceTimestamp;
    }

    /**
     * Gets the previous close price.
     *
     * @return the previous close price
     */
    public double getPreviousClosePrice() {
        return previousClosePrice;
    }

    /**
     * Sets the previous close price.
     *
     * @param previousClosePrice the new previous close price
     */
    public void setPreviousClosePrice(double previousClosePrice) {
        this.previousClosePrice = previousClosePrice;
    }

    //    private double calculateChangeToOpen() {
    //        //  not used
    //        double result = ((price - openPrice) / openPrice) * 100;
    //        return round(result, 2);
    //    }

    private double calculateChangeToPreviousClose() {
        double result = ((price - previousClosePrice) / previousClosePrice) * 100;
        return roundMath(result, 2);
    }

    private double roundMath(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    /**
     * Gets the open price.
     *
     * @return the open price
     */
    public double getOpenPrice() {
        return openPrice;
    }

    /**
     * Sets the open price.
     *
     * @param openPrice the new open price
     */
    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    /**
     * Gets the change direction.
     *
     * @return the change direction
     */
    public PriceChangeDirection getChangeDirection() {
        return changeDirection;
    }

    /**
     * Sets the change direction.
     *
     * @param changeDirection the new change direction
     */
    public void setChangeDirection(PriceChangeDirection changeDirection) {
        this.changeDirection = changeDirection;
    }

    /**
     * Resets the change direction when the color timer runs out
     */
    public synchronized void resetChangeDirection() {
        changeDirection = PriceChangeDirection.CONSTANT;
    }

    /**
     * Gets the color reset timer.
     *
     * @return the color reset timer
     */
    public ColorResetTimer getColorResetTimer() {
        return colorResetTimer;
    }

    /**
     * Adds the passed in alarm to the stock.
     *
     * @param alarm the alarm
     */
    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
    }

    /**
     * Gets the alarms for this stock.
     *
     * @return the alarms
     */
    public List<Alarm> getAlarms() {
        return alarms;
    }

    /**
     * Sets the alarm list to the passed in list
     *
     * @param alarms the alarm list to set to
     */
    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    /**
     * Sets the symbol.
     *
     * @param symbol the new symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    // ---------- below: methods used for the provided unit tests --------------

    /**
     * Removes the alarm with the specified alarm price.
     *
     * @param alarmPrice the alarm price
     */
    public void removeAlarmByPrice(double alarmPrice) {
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).price == alarmPrice) {
                alarms.remove(i);
            }
        }
    }

    /**
     * Clear alarms.
     */
    public void clearAlarms() {
        alarms.clear();
    }

    /**
     * Checks if an alarm is set for this stock.
     *
     * @return true, if there is at least one alarm set
     */
    public boolean hasAlarm() {
        if (alarms.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets the alarm prices set for this stock
     *
     * @return the alarm prices as a double array
     */
    public double[] getAlarmPrices() {
        double[] result = new double[alarms.size()];
        for (int i = 0; i < alarms.size(); i++) {
            result[i] = alarms.get(i).price;
        }
        return result;
    }

    /**
     * Adds an alarm for the specified price
     *
     * @param alarmPrice the alarm price
     */
    public void addAlarmByPrice(double alarmPrice) {
        if (!hasAlarmForPrice(alarmPrice)) {
            Alarm alarm = new Alarm(this, alarmPrice);
            addAlarm(alarm);
        }
    }

    private boolean hasAlarmForPrice(double alarmPrice) {
        boolean result = false;
        double[] alarmPrices = getAlarmPrices();
        for (double existingAlarmPrice : alarmPrices) {
            if (existingAlarmPrice == alarmPrice) {
                result = true;
            }
        }
        return result;
    }

}
