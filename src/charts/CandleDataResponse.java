package charts;


import com.google.gson.annotations.SerializedName;

/**
 * Represents a data provider's response to a request for candle data. Each response consists of six JSON arrays 
 * that get deserialized into the fields of objects of this class by the StaticCandleDataRequester.
 * One entry from each of these six arrays together constitute a candle.
 */
public class CandleDataResponse {
    @SerializedName("c") private double[] closePrices;
    @SerializedName("h") private double[] highPrices;
    @SerializedName("l") private double[] lowPrices;
    @SerializedName("o") private double[] openPrices;
    @SerializedName("t") private long[] timestamps;
    @SerializedName("v") private double[] volume;
    
    /**
     * Gets the close prices array of this response.
     *
     * @return the close prices array
     */
    public double[] getClosePrices() {
        return closePrices;
    }
    
    /**
     * Gets the high prices array of this response.
     *
     * @return the high prices array
     */
    public double[] getHighPrices() {
        return highPrices;
    }
    
    /**
     * Gets the low prices array of this response.
     *
     * @return the low prices array
     */
    public double[] getLowPrices() {
        return lowPrices;
    }
    
    /**
     * Gets the open prices array of this response.
     *
     * @return the open prices array
     */
    public double[] getOpenPrices() {
        return openPrices;
    }
    
    /**
     * Gets the timestamps array of this response.
     *
     * @return the timestamps array
     */
    public long[] getTimestamps() {
        return timestamps;
    }
    
    /**
     * Gets the volume array of this response.
     *
     * @return the volume array
     */
    public double[] getVolume() {
        return volume;
    }
    
    /**
     * Gets the number of candles contained in this response.
     *
     * @return the number of candles
     */
    public int getArraysLength() {
        return openPrices.length;
    }
    
}
