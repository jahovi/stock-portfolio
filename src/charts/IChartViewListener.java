package charts;

import main.Stock;

/**

 * This interface contains methods that are called by chart frames in response
 * to user actions. 
 * 
 */
public interface IChartViewListener {
    
    /**
     * Called when the "Line" button in the chart type menu of the chart frame is clicked. 
     *
     * @param chartPanel the chart panel that is to draw the line chart
     */
    void lineButtonClicked(ChartPanel chartPanel);
    
    /**
     * Called when the "Candle" button in the chart type menu of the chart frame is clicked. 
     *
     * @param  chartPanel the chart panel that is to draw the candle chart
     */
    void candleButtonClicked(ChartPanel chartPanel);
    
    /**
     * Called when the "1 Minute" button in the interval menu of the chart frame is clicked. 
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    void oneMinuteButtonClicked() throws TooManyIntervalsException;
    
    /**
     * Called when the "5 Minutes" button in the interval menu of the chart frame is clicked. 
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    void fiveMinuteButtonClicked() throws TooManyIntervalsException;
    
    /**
     * Called when the "15 Minutes" button in the interval menu of the chart frame is clicked. 
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    void fifteenMinuteButtonClicked() throws TooManyIntervalsException;
    
    /**
     * Called when the "2 Weeks" button in the interval menu of the chart frame is clicked.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    void weekButtonClicked() throws TooManyIntervalsException;
    
    /**
     * Called when the "30 Minutes" button in the interval menu of the chart frame is clicked.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    void thirtyMinuteButtonClicked() throws TooManyIntervalsException;
    
    /**
     * Called when the "60 Minutes" button in the interval menu of the chart frame is clicked.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    void sixtyMinuteButtonClicked() throws TooManyIntervalsException;
    
    /**
     * Called when the "1 Day" button in the interval menu of the chart frame is clicked.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    void dayButtonClicked() throws TooManyIntervalsException;
    
    /**
     * Called when the "1 Month" button in the interval menu of the chart frame is clicked.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    void monthButtonClicked() throws TooManyIntervalsException;
    
    /**
     * Called when the user requests a change to the number of intervals that are displayed in the chart.
     *
     * @param num the number of intervals to display
     * @throws TooManyIntervalsException thrown when more candles of the current interval are requested than the data provider provides
     */
    void changeNumIntervals(int num) throws TooManyIntervalsException;
    
    
    /**
     * Called when the user requests to display the indicators dialog.
     *
     * @param chartPanel the chart panel that will draw the indicators
     */
    void showIndicatorsDialog(ChartPanel chartPanel);
    
    /**
     * Called when the user requests to display the alarms dialog.
     *
     * @param chartPanel the chart panel that will draw the alarm lines
     * @param stock the stock for which alarm prices will be set in the dialog
     */
    void showAlarmsDialog(ChartPanel chartPanel, Stock stock);

}
