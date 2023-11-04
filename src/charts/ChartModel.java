package charts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import main.Stock;
import preferences.ApplicationPreferencesModel;

/**
 * Instances of this class act as the models for the chart views. They request from the data provider 
 * and contain the candle data from which the charts are drawn and from that data calculate the values needed to draw
 * the indicators. 
 */
public class ChartModel implements ActionListener {
    private ApplicationPreferencesModel preferences;
    
    private final Stock stock;
    private List<Candle> candles;
    private ChartPanel view;

    private final int defaultIntervals = 10;
    private final int secondsPerMinute = 60;
    private final int secondsPerDay = 86400;
    private final int secondsPerWeek = 604800;
    private final int secondsPerMonth = 2629743;

    private int numIntervals = defaultIntervals;
    private long startDate;
    private List<Candle> oneMinuteCandles;
    private List<Candle> fiveMinuteCandles;
    private List<Candle> thirtyMinuteCandles;
    private List<Candle> sixtyMinuteCandles;
    private List<Candle> dayCandles;
    private List<Candle> weekCandles;
    private List<Candle> monthCandles;
    private List<Candle> fifteenMinuteCandles;
    private Intervals interval;

    private Timer intervalTimer;
    
    private List<Indicator> indicators = new ArrayList<Indicator>();
    
    /**
     * Constructs a chart model holding data for the specified stock. This constructor
     * is only used for the unit tests.
     *
     * @param stock the stock to which this chart model corresponds
     */
    public ChartModel(Stock stock) {
        this.stock = new Stock();
    }

    /**
     * Constructs a chart model holding data for the specified stock and getting 
     * application preferences from the specified ApplicationPreferencesModel.
     *
     * @param preferences the preferences model that holds the application preferences
     * @param stock the stock for which this chart model holds chart data
     */
    public ChartModel(ApplicationPreferencesModel preferences, Stock stock) {
        this.preferences = preferences;
        this.stock = stock;
        // Get candles for the past 20 days
        startDate = System.currentTimeMillis() / 1000L - secondsPerDay  * 20;  
        dayCandles = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "D", startDate, System.currentTimeMillis() / 1000L);
        try {
            setInterval(Intervals.DAY);
        } catch (TooManyIntervalsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the list of candles to draw. Used by the chart panel that draws the candles.
     *
     * @return the candles to draw
     */
    public List<Candle> getCandles() {
        return candles;
    }


    /**
     * Sets a list of candles that need to be drawn
     *
     * @param candles the new candle list
     */
    public void setCandles(List<Candle> candles) {
        this.candles = candles;
    }

    /**
     * Sets the candle interval the user requests. Gets the appropriate candle data from the provider.
     *
     * @param interval the new interval for which the user requests candle data
     * @throws TooManyIntervalsException thrown when the user requests more candles for the interval than the data provider provides
     */
    public void setInterval(Intervals interval) throws TooManyIntervalsException {
        this.interval = interval;
        
        
        if (intervalTimer != null) {
            intervalTimer.stop();
        }
        
        switch (interval) {
        case ONE:
            oneMinuteCandles = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "1", System.currentTimeMillis() / 1000L - secondsPerMinute * numIntervals * 2, System.currentTimeMillis() / 1000L);
            
            Timer oneMinuteTimer = new Timer(60000, this);
            oneMinuteTimer.start();
            intervalTimer = oneMinuteTimer;
            
            if (numIntervals > oneMinuteCandles.size()) {
                // show message,
                JOptionPane.showMessageDialog(view, "Too many intervals for interval type " + Intervals.ONE + ". The maximum is " + oneMinuteCandles.size());
                // set numIntervals to oneMinuteCandles.size
                numIntervals = oneMinuteCandles.size();
                candles = oneMinuteCandles.subList(oneMinuteCandles.size() - numIntervals, oneMinuteCandles.size());
                fireCandleDataChanged();
                // throw exception to reset text field in chart frame menu bar to length of candle list
                throw new TooManyIntervalsException(oneMinuteCandles.size());

            }
            candles = oneMinuteCandles.subList(oneMinuteCandles.size() - numIntervals, oneMinuteCandles.size());
            fireCandleDataChanged();

            break;

        case FIVE:
            fiveMinuteCandles = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "5", System.currentTimeMillis() / 1000L - secondsPerMinute * 5 * numIntervals * 2, System.currentTimeMillis() / 1000L);
            
            Timer fiveMinuteTimer = new Timer(300000, this);
            fiveMinuteTimer.start();
            intervalTimer = fiveMinuteTimer;
            
            if (numIntervals > fiveMinuteCandles.size()) {
                // show message,
                JOptionPane.showMessageDialog(view, "Too many intervals for interval type " + Intervals.FIVE
                        + ". The maximum is " + fiveMinuteCandles.size());
                // set numIntervals to fiveMinuteCandles.size
                numIntervals = fiveMinuteCandles.size();
                candles = fiveMinuteCandles.subList(fiveMinuteCandles.size() - numIntervals, fiveMinuteCandles.size());
                fireCandleDataChanged();
                // throw exception to reset text field in chart frame menu bar to length of candle list
                throw new TooManyIntervalsException(fiveMinuteCandles.size());

            }
            candles = fiveMinuteCandles.subList(fiveMinuteCandles.size() - numIntervals, fiveMinuteCandles.size());
            fireCandleDataChanged();

            break;

        case FIFTEEN:
            fifteenMinuteCandles = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "15", System.currentTimeMillis() / 1000L - secondsPerMinute * 15 * numIntervals * 2, System.currentTimeMillis() / 1000L);
            
            Timer fifteenMinuteTimer = new Timer(900000, this);
            fifteenMinuteTimer.start();
            intervalTimer = fifteenMinuteTimer;
            
            if (numIntervals > fifteenMinuteCandles.size()) {
                // show message,
                JOptionPane.showMessageDialog(view, "Too many intervals for interval type " + Intervals.FIFTEEN
                        + ". The maximum is " + fifteenMinuteCandles.size());
                // set numIntervals to fifteenMinutesCandles.size
                numIntervals = fifteenMinuteCandles.size();
                candles = fifteenMinuteCandles.subList(fifteenMinuteCandles.size() - numIntervals, fifteenMinuteCandles.size());
                fireCandleDataChanged();
                // throw exception to reset text field in chart frame menu bar to length of candle list
                throw new TooManyIntervalsException(fifteenMinuteCandles.size());

            }
            candles = fifteenMinuteCandles.subList(fifteenMinuteCandles.size() - numIntervals,
                    fifteenMinuteCandles.size());
            fireCandleDataChanged();

            break;

        case THIRTY:
            thirtyMinuteCandles = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "30", System.currentTimeMillis() / 1000L - secondsPerMinute * 30 * numIntervals * 2, System.currentTimeMillis() / 1000L);
            
            Timer thirtyMinuteTimer = new Timer(1800000, this);
            thirtyMinuteTimer.start();
            intervalTimer = thirtyMinuteTimer;
            
            if (numIntervals > thirtyMinuteCandles.size()) {
                // show message,
                JOptionPane.showMessageDialog(view, "Too many intervals for interval type " + Intervals.THIRTY
                        + ". The maximum is " + thirtyMinuteCandles.size());
                // set numIntervals to thirtyMinuteCandles.size
                numIntervals = thirtyMinuteCandles.size();
                candles = thirtyMinuteCandles.subList(thirtyMinuteCandles.size() - numIntervals, thirtyMinuteCandles.size());
                fireCandleDataChanged();
                // throw exception to reset text field in chart frame menu bar to length of candle list
                throw new TooManyIntervalsException(thirtyMinuteCandles.size());

            }
            candles = thirtyMinuteCandles.subList(thirtyMinuteCandles.size() - numIntervals,
                    thirtyMinuteCandles.size());
            fireCandleDataChanged();

            break;

        case SIXTY:
            sixtyMinuteCandles = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "60", System.currentTimeMillis() / 1000L - secondsPerMinute * 60 * numIntervals * 2, System.currentTimeMillis() / 1000L);
            
            Timer sixtyMinuteTimer = new Timer(3600000, this);
            sixtyMinuteTimer.start();
            intervalTimer = sixtyMinuteTimer;
            
            if (numIntervals > sixtyMinuteCandles.size()) {
                // show message,
                JOptionPane.showMessageDialog(view, "Too many intervals for interval type " + Intervals.SIXTY
                        + ". The maximum is " + sixtyMinuteCandles.size());
                // set numIntervals to sixtyMinuteCandles.size
                numIntervals = sixtyMinuteCandles.size();
                candles = sixtyMinuteCandles.subList(sixtyMinuteCandles.size() - numIntervals, sixtyMinuteCandles.size());
                fireCandleDataChanged();
                // throw exception to reset text field in chart frame menu bar to length of candle list
                throw new TooManyIntervalsException(sixtyMinuteCandles.size());

            }
            candles = sixtyMinuteCandles.subList(sixtyMinuteCandles.size() - numIntervals, sixtyMinuteCandles.size());
            fireCandleDataChanged();

            break;
        case DAY:
            dayCandles = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "D", System.currentTimeMillis() / 1000L - secondsPerDay * numIntervals * 2, System.currentTimeMillis() / 1000L);
            
            if (numIntervals > dayCandles.size()) {
                // show message,
                JOptionPane.showMessageDialog(view, "Too many intervals for interval type " + Intervals.DAY
                        + ". The maximum is " + dayCandles.size());
                // set numIntervals to dayCandles.size
                numIntervals = dayCandles.size();
                candles = dayCandles.subList(dayCandles.size() - numIntervals, dayCandles.size());
                fireCandleDataChanged();
                // throw exception to reset text field in chart frame menu bar to length of candle list
                throw new TooManyIntervalsException(dayCandles.size());

            }
            
            candles = dayCandles.subList(dayCandles.size() - numIntervals, dayCandles.size());
            fireCandleDataChanged();

            break;

        case WEEK:
            weekCandles = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "W", System.currentTimeMillis() / 1000L - secondsPerWeek * 2 * numIntervals * 2, System.currentTimeMillis() / 1000L);
            if (numIntervals > weekCandles.size()) {
                // show message,
                JOptionPane.showMessageDialog(view, "Too many intervals for interval type " + Intervals.WEEK
                        + ". The maximum is " + weekCandles.size());
                // set numIntervals to weekCandles.size
                numIntervals = weekCandles.size();
                candles = weekCandles.subList(weekCandles.size() - numIntervals, weekCandles.size());
                fireCandleDataChanged();
                // throw exception to reset text field in chart frame menu bar to length of candle list
                throw new TooManyIntervalsException(weekCandles.size());

            }
            candles = weekCandles.subList(weekCandles.size() - numIntervals, weekCandles.size());
            fireCandleDataChanged();

            break;

        case MONTH:
            monthCandles = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "M", System.currentTimeMillis() / 1000L - secondsPerMonth * numIntervals * 2, System.currentTimeMillis() / 1000L);
            if (numIntervals > monthCandles.size()) {
                // show message,
                JOptionPane.showMessageDialog(view, "Too many intervals for interval type " + Intervals.MONTH
                        + ". The maximum is " + monthCandles.size());
                // set numIntervals to monthCandles.size
                numIntervals = monthCandles.size();
                candles = monthCandles.subList(monthCandles.size() - numIntervals, monthCandles.size());
                fireCandleDataChanged();
                // throw exception to reset text field in chart frame menu bar to length of candle list
                throw new TooManyIntervalsException(monthCandles.size());

            }
            candles = monthCandles.subList(monthCandles.size() - numIntervals, monthCandles.size());
                fireCandleDataChanged();

            break;
        }
        fireCandleDataChanged();
    }

//    private void addTodayCandle() {
//        //             trying to get updates
//        long latestTimestamp = dayCandles.get(dayCandles.size() - 1).getTimestamp();
//        List<Candle> candlesSince = StaticCandleDataRequester.getCandleList(preferences.getActiveDataProvider(), stock, "M", latestTimestamp, System.currentTimeMillis() / 1000L);
//        double low = getLowOfCandleList(candlesSince);
//        double high = getHighOfCandleList(candlesSince);
//        double open = candlesSince.get(0).getOpen();
//        double close = stock.getPrice();
//        Candle newCandle = new Candle(low, high, open, close, System.currentTimeMillis() / 1000L);
//        dayCandles.add(newCandle);
//        
//        Candle latestCandle = dayCandles.get(dayCandles.size() - 1);
//    }

    
    /**
     * Sets the number of intervals the user requests.
     *
     * @param numIntervals the new number of intervals
     * @throws TooManyIntervalsException thrown when the user requests more candles for the interval than the data provider provides
     */
    public void setNumberOfIntervals(int numIntervals) throws TooManyIntervalsException {
        this.numIntervals = numIntervals;
        setInterval(interval);
        fireCandleDataChanged();
    }

    /**
     * Gets the number of intervals currently shown in the chart
     *
     * @return the number of intervals
     */
    public int getNumberOfIntervals() {
        return numIntervals;
    }

    /**
     * Gets the view that visualizes this data.
     *
     * @return the view
     */
    public ChartPanel getView() {
        return view;
    }

    /**
     * Sets the view that visualizes this data.
     *
     * @param view the new view
     */
    public void setView(ChartPanel view) {
        this.view = view;
    }

    /**
     * Gets the indicators to draw.
     *
     * @return the indicators that currently need to be drawn
     */
    public List<Indicator> getIndicators() {
        return indicators;
    }

    /**
     * Sets a new list of indicators to be drawn.
     *
     * @param indicators the new indicator list
     */
    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
        fireCandleDataChanged();
    }

    /**
     * Calculate moving average.
     *
     * @param n the number of candles to use for the calculation
     * @return a double array of calculated values
     */
    public double[] calculateMovingAverage(int n) {
        double[] result = new double[0];
        if (n <= candles.size() + 1) {
            result = new double[candles.size() + 1 - n];
            for (int i = 0; i < candles.size() + 1 - n; i++) {
                double sum = 0;
                for (int j = i; j < i + n; j++) {
                    sum += candles.get(j).getClose();
                }
                result[i] = (1.0 / (double) n) * sum;
            }
        }
        return result;
    }

    /**
     * Calculate upper Bollinger Band.
     *
     * @param n the number of candles to use for the calculation
     * @param f the standard deviation multiplier to use for the calculation
     * @return a double array of calculated values
     */
    public double[] calculateUpperBollingerBand(int n, int f) {
        double[] movingAverage = calculateMovingAverage(n);
        double[] standardDeviation = calculateStandardDeviation(n);
        double[] result = new double[movingAverage.length];
        for (int i = 0; i < movingAverage.length; i++) {
            result[i] = movingAverage[i] + f * standardDeviation[i];
        }
        return result;
    }

    /**
     * Calculate lower Bollinger Band.
     *
     * @param n the number of candles to use for the calculation
     * @param f the standard deviation multiplier to use for the calculation
     * @return a double array of calculated values
     */
    public double[] calculateLowerBollingerBand(int n, int f) {
        double[] movingAverage = calculateMovingAverage(n);
        double[] standardDeviation = calculateStandardDeviation(n);
        double[] result = new double[movingAverage.length];
        for (int i = 0; i < movingAverage.length; i++) {
            result[i] = movingAverage[i] - f * standardDeviation[i];
        }
        return result;
    }

    private double[] calculateStandardDeviation(int n) {
        double[] movingAverage = calculateMovingAverage(n);

        double[] result = new double[0];
        if (n <= candles.size() + 1) {
            result = new double[candles.size() + 1 - n];
            for (int i = 0; i < candles.size() + 1 - n; i++) {
                double sum = 0;
                for (int j = i; j < i + n; j++) {
                    sum += Math.pow(candles.get(j).getClose() - movingAverage[i], 2);
                }
                result[i] = Math.sqrt((1.0 / (double) n) * sum);
            }
        }
        return result;
    }

    /**
     * Fire candle data changed. Informs the view it needs to repaint itself.
     */
    public void fireCandleDataChanged() {
        if (view != null) {
            view.repaint();
        }
    }

    /**
     * Handles events generated by the timer that fires when one instance of the currently selected interval is over.
     * Then the candle list needs to be updated.
     *
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            setNumberOfIntervals(numIntervals);
        } catch (TooManyIntervalsException e1) {
            e1.printStackTrace();
        }
    }

}
