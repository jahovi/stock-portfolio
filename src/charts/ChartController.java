package charts;

import java.util.List;

import main.Stock;

/**
 * Represents the controller of a chart window according to the Model-View-Controller paradigm. 
 * ChartController implements IChartViewListener. Therefore, a chart controller instance 
 * listens for events the chart frame and its associated indicators- and alarms-dialogs 
 * produce in response to user actions and changes the chart model accordingly. 
 */
public class ChartController implements IChartViewListener {
    private ChartModel chartModel;
    private IndicatorsDialog indicatorsDialog;
    
    /**
     * Instantiates a new chart controller and associates it with a chart model.
     *
     * @param chartModel the chart model this controller is to be associated with
     */
    public ChartController(ChartModel chartModel) {
        this.chartModel = chartModel;
    }

    /**
     * Called when the "Line" button in the chart type menu of the chart frame is clicked. 
     * Sets the displayed chart type accordingly.
     *
     * @param chartPanel the chart panel that is to draw the line chart
     */
    @Override
    public void lineButtonClicked(ChartPanel chartPanel) {
         chartPanel.setChartType("Line");
    }

    /**
     * Called when the "Candle" button in the chart type menu of the chart frame is clicked. 
     * Sets the displayed chart type accordingly.
     *
     * @param chartPanel the chart panel that is to draw the candle chart
     */
    @Override
    public void candleButtonClicked(ChartPanel chartPanel) {
        chartPanel.setChartType("Candle");
    }

    /**
     * Called when the "1 Minute" button in the interval menu of the chart frame is clicked. 
     * Sets the interval type in the chart model accordingly.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    @Override
    public void oneMinuteButtonClicked() throws TooManyIntervalsException {
        chartModel.setInterval(Intervals.ONE);
    }

    /**
     * Called when the "5 Minutes" button in the interval menu of the chart frame is clicked. 
     * Sets the interval type in the chart model accordingly.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    @Override
    public void fiveMinuteButtonClicked() throws TooManyIntervalsException {
        chartModel.setInterval(Intervals.FIVE);
    }

    /**
     * Called when the "15 Minutes" button in the interval menu of the chart frame is clicked. 
     * Sets the interval type in the chart model accordingly.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    @Override
    public void fifteenMinuteButtonClicked() throws TooManyIntervalsException {
        chartModel.setInterval(Intervals.FIFTEEN);
    }


    /**
     * Called when the "30 Minutes" button in the interval menu of the chart frame is clicked. 
     * Sets the interval type in the chart model accordingly.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    @Override
    public void thirtyMinuteButtonClicked() throws TooManyIntervalsException {
        chartModel.setInterval(Intervals.THIRTY);

    }

    /**
     * Called when the "60 Minutes" button in the interval menu of the chart frame is clicked. 
     * Sets the interval type in the chart model accordingly.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    @Override
    public void sixtyMinuteButtonClicked() throws TooManyIntervalsException {
        chartModel.setInterval(Intervals.SIXTY);

    }

    /**
     * Called when the "1 Day" button in the interval menu of the chart frame is clicked. 
     * Sets the interval type in the chart model accordingly.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    @Override
    public void dayButtonClicked() throws TooManyIntervalsException {
        chartModel.setInterval(Intervals.DAY);

    }
    
    /**
     * Called when the "2 Weeks" button in the interval menu of the chart frame is clicked. 
     * Sets the interval type in the chart model accordingly.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    @Override
    public void weekButtonClicked() throws TooManyIntervalsException {
        chartModel.setInterval(Intervals.WEEK);

    }

    /**
     * Called when the "1 Month" button in the interval menu of the chart frame is clicked. 
     * Sets the interval type in the chart model accordingly.
     *
     * @throws TooManyIntervalsException thrown when more candles of the interval are requested than the data provider provides
     */
    @Override
    public void monthButtonClicked() throws TooManyIntervalsException {
        chartModel.setInterval(Intervals.MONTH);

    }

    /**
     * Called when the user requests a change to the number of intervals that are displayed in the chart.
     *
     * @param num the number of intervals to display
     * @throws TooManyIntervalsException thrown when more candles of the current interval are requested than the data provider provides
     */
    @Override
    public void changeNumIntervals(int num) throws TooManyIntervalsException {
        chartModel.setNumberOfIntervals(num);
    }
    
    /**
     * Called when the user requests to display the indicators dialog.
     *
     * @param chartPanel the chart panel that will draw the indicators
     */
    @Override
    public void showIndicatorsDialog(ChartPanel chartPanel) {
        if (indicatorsDialog == null) {
            indicatorsDialog = new IndicatorsDialog(chartPanel, this);
        } else {
            indicatorsDialog.setVisible(true);
        }
    }

    /**
     * Called when the user clicks the "OK" button in the indicators dialog. 
     * Adds the specified indicators to the chart model.
     *
     * @param indicators the list of indicators specified in the indicators dialog where the "OK" button is clicked
     */
    public void indicatorsDialogOKButtonClicked(List<Indicator> indicators) {
        chartModel.setIndicators(indicators);
    }

    /**
     * Called when the user requests to display the alarms dialog.
     *
     * @param chartPanel the chart panel that will draw the alarm lines
     * @param stock the stock for which alarm prices will be set in the dialog
     */
    @Override
    public void showAlarmsDialog(ChartPanel chartPanel, Stock stock) {
//        alarmsDialog = new AlarmsDialog(chartPanel, stock);
        new AlarmsDialog(chartPanel, stock);
    }


}
