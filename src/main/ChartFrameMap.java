package main;

import java.util.*;

import charts.ChartFrame;

/**
 * An instance of this class stores the presently opened chart frames in a HashMap. The keys are stock symbols
 * and the values are Lists of chart frames that exist for that Stock. Lookup occurs when new prices for stocks come in
 * and the associated chart frames need to be updated.
 */

public class ChartFrameMap {
    private HashMap<String, List<ChartFrame>> chartFramesPerStock;
    
    /**
     * Constructs an empty chart frame map.
     */
    public ChartFrameMap() {
        chartFramesPerStock = new HashMap<>();
    }
    
    /**
     * Constructs a new chart frame map and stores the specified HashMap in it.
     *
     * @param chartFramesPerStock the chart frames per stock
     */
    public ChartFrameMap(HashMap<String, List<ChartFrame>> chartFramesPerStock) {
        this.chartFramesPerStock = chartFramesPerStock;
    }
    
    /**
     * Adds a chart frame with the specified stock symbol as key.
     *
     * @param stockSymbol the symbol of the stock the chart corresponds to
     * @param chartFrame the chart frame to be stored
     */
    public void addChartFrame(String stockSymbol, ChartFrame chartFrame) {
        if (chartFramesPerStock.containsKey(stockSymbol)) {
            chartFramesPerStock.get(stockSymbol).add(chartFrame);
        } else {
            // if the stock isnt't in the key set
            List<ChartFrame> newList = new ArrayList<>();
            newList.add(chartFrame);
            chartFramesPerStock.put(stockSymbol, newList);
        }
//        System.out.println("Map contains: ");
//        for (List<ChartFrame> frameList : chartFramesPerStock.values()) {
//            for (ChartFrame frame : frameList) {
//                System.out.println(frame.toString());
//            }
//        }
    }
    
    /**
     * Removes the specified chart frame from this container.
     *
     * @param chartFrame the chart frame to remove
     */
    public void removeChartFrame(ChartFrame chartFrame) {
            String symbol = chartFrame.getStock().getSymbol();
            List<ChartFrame> targetList = chartFramesPerStock.get(symbol);
            if (targetList != null) {
                targetList.remove(chartFrame);
            }
            // if a list is empty remove the key
            if (chartFramesPerStock.get(symbol).size() == 0) {
                chartFramesPerStock.remove(symbol);
            }
    }

    /**
     * Gets the chart frames stored in this container that correspond to the specified stock symbol
     *
     * @param stock the stock symbol the desired chart frames correspond to
     * @return the chart frames associated with the specified stock
     */
    public List<ChartFrame> getChartFrames(String stock){
        return chartFramesPerStock.get(stock);
    }
    
    /**
     *  Checks if this container stores charts corresponding to the specified stock symbol
     *
     * @param key the stock symbol to check
     * @return true, if this container contains charts for the specified stock symbol
     */
    public boolean containsKey(String key) {
        return chartFramesPerStock.containsKey(key);
    }
    
    private Collection<List<ChartFrame>> values() {
        return chartFramesPerStock.values();
    }
    
    /**
     * Returns a list of the chart frames stored in this container.
     *
     * @return a list of chart frames
     */
    public List<ChartFrame> getChartFrames() {
        List<ChartFrame> result = new ArrayList<>();
        for (List<ChartFrame> chartFrameList : this.values()) {
            result.addAll(chartFrameList);
        }
        return result;
    }
    
    /**
     * Returns the number of chart frames stored in this container.
     *
     * @return the number of chart frames as an int
     */
    public int size() {
        return chartFramesPerStock.size();
    }

    /**
     * Returns a String representation of the stock symbols and associated chart frames in this container.
     * For debugging.
     *
     * @return the string representation of this container
     */
    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<String, List<ChartFrame>> entry : chartFramesPerStock.entrySet()) {
            result += (entry.getKey() + " : ");
            for (ChartFrame chart : entry.getValue()) {
                result += chart.toString();
            }
            result += "\n";
        }
        return result;
    }


    
    
    
}
