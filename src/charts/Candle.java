package charts;

import java.awt.Color;


/**
 * Represents a stock chart candle. Candle type charts are painted from lists of Candles. 
 * 
 */
public class Candle {
    private double low;
    private double high;
    private double open; 
    private double close;
    private long timestamp;
    private Color color;

    /**
     * Creates a new candle and derives the appropriate color from the given prices.
     *
     * @param low the low price
     * @param high the high price
     * @param open the open price
     * @param close the close price
     * @param timestamp the timestamp of this candle
     */
    public Candle(double low, double high, double open, double close, long timestamp) {
        this.low = low;
        this.high = high;
        this.open = open;
        this.close = close;
        this.timestamp = timestamp;

        if (open >= close) {
            color = Color.RED;
        } else {
            color = Color.GREEN;
        }
    }


    /**
 * Gets the candle's low price
 *
 * @return the candle's low price
 */
public double getLow() {
        return low;
    }

    /**
     * Gets the candle's high price.
     *
     * @return the candle's high price
     */
    public double getHigh() {
        return high;
    }

    /**
     * Gets the candle's open price.
     *
     * @return the candle's open price
     */
    public double getOpen() {
        return open;
    }

    /**
     * Gets the candle's close price.
     *
     * @return the candle's close price
     */
    public double getClose() {
        return close;
    }

    /**
     * Gets the candle's timestamp.
     *
     * @return the candle's timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the candle color.
     *
     * @return the candle color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Checks if the stock price is falling in the interval represented by this candle.
     *
     * @return true, if is falling, false, if is rising
     */
    public boolean isFalling() {
        return (open >= close) ? true : false;
    }


    /**
     * Updates the candle with a new close price. Called by the chart panel that displays this candle
     * when it is informed of a new price by the main controller, which in turn receives new prices through the web socket connection.
     *
     * @param newClosePrice the new close price 
     */
    public void updateCandle(double newClosePrice) {
        this.close = newClosePrice;
        
        // update color
        if (open >= newClosePrice) {
            color = Color.RED;
        } else {
            color = Color.GREEN;
        }
        
        // update wick
        if (newClosePrice > high) {
            high = newClosePrice;
        }
        
        if (newClosePrice < low) {
            low = newClosePrice;
        }
        
    }
}

