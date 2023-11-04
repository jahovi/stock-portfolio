package webSocket;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Trade messages in json format received through a websocket connection get deserialized into instances of this class.
 */
public class TradeMessage {
    private ArrayList<Data> data;
    private String type;
    
    /**
     * Returns an array of the actual trade data sets contained in the message. 
     * 
     * @return the array of data sets about the individual trades
     */
    public ArrayList<Data> getData() {
        return data;
    }
    
    /**
     * Gets the type of message. Used to differentiate between actual trade and ping messages.
     *
     * @return the type of message
     */
    public String getType() {
        return type;
    }
    
    /**
     * Each individual trade is represented by an instance of this class.
     */
    public static class Data {
        @SerializedName("p") private double price;
        @SerializedName("s") private String symbol;
        @SerializedName("t") private long millisecondTimestamp;
        @SerializedName("v") private double volume;
        
        /**
         * Gets the new price that results from the trade.
         *
         * @return the new price
         */
        public double getPrice() {
            return price;
        }
        
        /**
         * Gets the symbol of the stock for which the price has changed.
         *
         * @return the stock symbol
         */
        public String getSymbol() {
            return symbol;
        }
        
        /**
         * Gets the unix timestamp in milliseconds when trade occurred
         *
         * @return the trade's unix timestamp in millisecond 
         */
        public double getMillisecondTimestamp() {
            return millisecondTimestamp;
        }
        
        /**
         * Gets the volume that was traded.
         *
         * @return the trade volume
         */
        public double getVolume() {
            return volume;
        }
    }
    
//    public void roundPrices() {
//        for (Data d : data) {
//            // round value to places decimal places
//            BigDecimal bd = new BigDecimal(Double.toString(d.getPrice()));
//            bd = bd.setScale(2, RoundingMode.HALF_EVEN);
//            d.setPrice(bd.doubleValue());
//            
//        }
//    }
    
}

