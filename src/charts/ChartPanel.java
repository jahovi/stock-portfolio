package charts;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.JPanel;

import main.Stock;

/**
 * Instances of this class are contained in chart frames and form the drawing surfaces for charts and stock data.
 */
public class ChartPanel extends JPanel {

    private final int padding = 25;
    private final int labelPadding = 40;
    private final int combinedPadding = padding + labelPadding;
    private final int numYLabels = 10;
    private final int candleWidth = 10;
    private float lineChartStrokeWidth = 1.5f;
    private BasicStroke lineChartStroke = new BasicStroke(lineChartStrokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    private String chartType = "Candle";

    private int crosshairX = -1;
    private int crosshairY = -1;
    private String crosshairPrice;
    private String crosshairDate;
    
    private List<String> dateLabelList;
    
    private Stock stock;
    private String stockSymbol;     
    
    
    private int drawingAreaX;
    private int drawingAreaY;
    private int drawingAreaWidth;
    private int drawingAreaHeight;
    
    // scaling factors
    private double xAxisFactor;
    private double yAxisFactor;
    
    private double maxPriceInCandleList;
    private double minPriceInCandleList;

    private ChartModel chartModel;
    
    /**
     * Constructs a new chart panel.
     *
     * @param stock the stock for a which a chart is to be drawn
     * @param chartModel the chart model contains the data that is visualized in this chart
     */
    public ChartPanel(Stock stock, ChartModel chartModel) {
        this.stock = stock;
        this.stockSymbol = stock.getSymbol();
        this.chartModel = chartModel;
        chartModel.setView(this);
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                moveCrosshair(e.getX(), e.getY());
            }
        });
    }
    
    

    private void moveCrosshair(int x, int y) {
        crosshairX = x;
        crosshairY = y;
        
        // calculate price at mouse position
        double priceIncrement = (maxPriceInCandleList - minPriceInCandleList) - ((double) (y - drawingAreaY) / (drawingAreaHeight)) * (maxPriceInCandleList - minPriceInCandleList);
        crosshairPrice = ((int) ((priceIncrement + minPriceInCandleList) * 100.0)) / 100.0 + "";
        // priceIncrement + minPriceInCandleList simplifies to: maxPriceInCandleList - (double) (y - drawingAreaY) / yAxisFactor 
        
        // calculate date at mouse position
        try {
            int xSection =  x / (int)  xAxisFactor; 
            crosshairDate = dateLabelList.get(xSection);
        } catch (IndexOutOfBoundsException | ArithmeticException e) {
            // e.printStackTrace();
        }
        repaint();
    }

    /**
     * All the custom painting code to draw the background, the coordinate axis and labels, the charts, 
     * the crosshair, the indicators and the alarm lines is called from this method.
     *
     * @param g the graphics object passed by the framework
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        drawingAreaX = padding;
        drawingAreaY = padding;
        drawingAreaWidth = getWidth() - padding - combinedPadding;
        drawingAreaHeight = getHeight() - padding - combinedPadding;
        
        maxPriceInCandleList = getMaxPriceInCandleList();
        minPriceInCandleList = getMinPriceInCandleList();

        xAxisFactor = ((double) drawingAreaWidth) / (chartModel.getCandles().size() - 1);  // this is where I initially went without the -1 to leave some space at the right end
        yAxisFactor = ((double) drawingAreaHeight) / (maxPriceInCandleList - minPriceInCandleList);

        drawBackground(g2);
        drawXAxisAndLabels(g2);
        drawYAxisAndLabels(g2);
        drawAlarms(g2);
        
        if (chartType.equals("Candle")) {
            drawCandles(g2);
        } else if (chartType.equals("Line")) {
            drawLineChart(g2);
        }

        drawCrosshair(g2);
        drawStatusBar(g2);
        drawIndicators(g2);
    }

    
    private void drawBackground(Graphics2D g2) {
        // draw background
        g2.setColor(Color.WHITE);
        g2.fillRect(drawingAreaX, drawingAreaY, drawingAreaWidth, drawingAreaHeight);
    }



    private void drawXAxisAndLabels(Graphics2D g2) {
        // draw x axis and labels
        dateLabelList = new ArrayList<String>();
        
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(drawingAreaX, drawingAreaY + drawingAreaHeight, drawingAreaX + drawingAreaWidth, drawingAreaY + drawingAreaHeight);
        for (int i = 0; i < chartModel.getCandles().size(); i++) {
                CustomDate date = new CustomDate(chartModel.getCandles().get(i).getTimestamp() * 1000L);
                dateLabelList.add(date.getCustomDateString());
                if (i % (int) ((chartModel.getCandles().size() - 1 ) / 8 + 1) == 0) {
                    // draw label for every candle-number/8th candle. + 1 to prevent division by zero when candlenumber < 8
                    String dateLabel = date.getMonthAndDay();
                    String timeLabel = date.getHourAndMinute();
                    String yearLabel = date.getYearString();
                    g2.drawString(dateLabel + "." + yearLabel, (int) (i * xAxisFactor + padding), padding + drawingAreaHeight + 12);
                    g2.drawString(timeLabel, (int) (i * xAxisFactor + padding), padding + drawingAreaHeight + 25);
                }
        }
    }



    private void drawYAxisAndLabels(Graphics2D g2) {
        // draw y axis and labels
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(drawingAreaX + drawingAreaWidth, drawingAreaY + drawingAreaHeight, drawingAreaX + drawingAreaWidth, drawingAreaY);
        for (int i = 0; i < numYLabels + 1; i++) {
            double increment = (((double) i) / numYLabels) * (maxPriceInCandleList - minPriceInCandleList);
            String label = ((int) ((increment + minPriceInCandleList) * 100.0)) / 100.0 + "";
            /*
             * ((int) x * 100) /100 : float point two places to the right, discard remaining
             * decimal places and float it back two places to the left -> trim number to two
             * decimal places
             */
            g2.drawString(label, drawingAreaX + drawingAreaWidth + 5, drawingAreaY + drawingAreaHeight - i * (drawingAreaHeight / numYLabels));
        }
    }



    private void drawLineChart(Graphics2D g2) {
        // draw lines
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.setStroke(lineChartStroke);
        for (int i = 0; i < chartModel.getCandles().size() - 1; i++) {
            Candle candle = chartModel.getCandles().get(i);
            Candle nextCandle = chartModel.getCandles().get(i + 1);
            int x1 = (int) (i * xAxisFactor + drawingAreaX);
            int y1 = (int) ((maxPriceInCandleList - candle.getClose()) * yAxisFactor) + drawingAreaY;
            int x2 = (int) ((i + 1) * xAxisFactor + drawingAreaX);
            int y2 = (int) ((maxPriceInCandleList - nextCandle.getClose()) * yAxisFactor) + drawingAreaY;
            g2.drawLine(x1, y1, x2, y2);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }



    private void drawCandles(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        // draw candles
        for (int i = 0; i < chartModel.getCandles().size(); i++) {
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            Candle candle = chartModel.getCandles().get(i);
    
            // draw wick
            int x1 = (int) (i * xAxisFactor + padding); // x of lower end of wick of candle i
            int y1 = (int) ((maxPriceInCandleList - candle.getLow()) * yAxisFactor) + padding; //  y of lower end of  wick
            int x2 = x1; // x of upper end of first wick
            int y2 = (int) ((maxPriceInCandleList - candle.getHigh()) * yAxisFactor) + padding; // y of upper end of first wick
            g2.drawLine(x1, y1, x2, y2);
    
            // draw body
            g2.setStroke(new BasicStroke(1f));
            int x = (int) (i * xAxisFactor + padding - candleWidth / 2); // x of upper left corner of first candle
            int y = 0; // y of upper left corner 
            int width = candleWidth;
            int height = 0;
            if (candle.isFalling()) {
                y = (int) ((maxPriceInCandleList - candle.getOpen()) * yAxisFactor) + padding;
                height = (int) ((candle.getOpen() - candle.getClose()) * yAxisFactor);
            } else {
                y = (int) ((maxPriceInCandleList - candle.getClose()) * yAxisFactor) + padding;
                height = (int) ((candle.getClose() - candle.getOpen()) * yAxisFactor);
            }
            g2.drawRect(x, y, width, height);
            g2.setColor(candle.getColor());
            g2.fillRect(x + 1, y + 1, width - 1, height - 1);
        }
    }



    private void drawStatusBar(Graphics2D g2) {
        // paint status line 10 pixels above the lower edge
        int textHeightAboveLowerEdge = getHeight() - 10;
        int dividerHeightAboveText = textHeightAboveLowerEdge - 15;
        
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke());
        g2.drawLine(0, dividerHeightAboveText, getWidth(), dividerHeightAboveText);
        
         
        // draw stock symbol
        String stockLabel = "Stock: " + stockSymbol;
        g2.drawString(stockLabel, drawingAreaX, textHeightAboveLowerEdge);
        
        // draw latest price
        String latestPrice = "Latest Price: " + stock.getPrice();
        g2.drawString(latestPrice, drawingAreaX + 200, textHeightAboveLowerEdge);
        
        // draw cursor label
        String cursorLabel = "Cursor: " + crosshairDate + " | " + crosshairPrice;
        g2.drawString(cursorLabel, drawingAreaX + 450, textHeightAboveLowerEdge);
    }



    private void drawIndicators(Graphics2D g2) {
        for (Indicator indicator : chartModel.getIndicators()) {
            switch (indicator.type) {
            case MOVING_AVERAGE:
                MovingAverage movingAverage = (MovingAverage) indicator;
                drawMovingAverage(g2, (movingAverage.length));
                break;
            case BOLLINGER_BANDS:
                BollingerBands bollingerBands = (BollingerBands) indicator;
                drawBollingerBands(g2, bollingerBands.length, bollingerBands.standardDeviation);
                break;
            default:
                break;
            }
        }
        
    }

//    xAxisFactor = ((double) drawingAreaWidth) / (chartModel.getCandles().size() - 1); 
//    yAxisFactor = ((double) drawingAreaHeight) / (maxPriceInCandleList - minPriceInCandleList);



    private void drawMovingAverage(Graphics2D g2, int length) {
        double[] movingAverageValues = chartModel.calculateMovingAverage(length);
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLUE);
        g2.setStroke(lineChartStroke);
        
        for (int i = movingAverageValues.length - 1; i > 0; i--) {
            double value1 = movingAverageValues[i];
            double value2 = movingAverageValues[i - 1];
            
            int x1 = (int) (drawingAreaWidth + padding - (movingAverageValues.length - i) * xAxisFactor); 
            int y2 = (int) ((maxPriceInCandleList - value1) * yAxisFactor) + drawingAreaY;
            int x2 = (int) (drawingAreaWidth + padding - ((movingAverageValues.length - i) - 1) * xAxisFactor);
            int y1 = (int) ((maxPriceInCandleList - value2) * yAxisFactor) + drawingAreaY;
            
            g2.drawLine(x1, y1, x2, y2);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }



    private void drawBollingerBands(Graphics2D g2, int length, int standardDeviation) {
        double[] upperBollingerBandValues = chartModel.calculateUpperBollingerBand(length, standardDeviation);
        double[] lowerBollingerBandValues = chartModel.calculateLowerBollingerBand(length, standardDeviation);
        
        drawMovingAverage(g2, length);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.ORANGE);
        g2.setStroke(lineChartStroke);
        
        for (int i = upperBollingerBandValues.length - 1; i > 0; i--) {
              double valueUpper = upperBollingerBandValues[i];
              double valueUpperPrevious = upperBollingerBandValues[i - 1];
              
              double valueLower = lowerBollingerBandValues[i];
              double valueLowerPrevious = lowerBollingerBandValues[i - 1];
              
              
              int x1Upper = (int) (drawingAreaWidth + padding - (upperBollingerBandValues.length - i) * xAxisFactor); 
              int y2Upper = (int) ((maxPriceInCandleList - valueUpper) * yAxisFactor) + drawingAreaY;
              int x2Upper = (int) (drawingAreaWidth + padding - ((upperBollingerBandValues.length - i) - 1) * xAxisFactor);
              int y1Upper = (int) ((maxPriceInCandleList - valueUpperPrevious) * yAxisFactor) + drawingAreaY;
              
              int x1Lower = x1Upper;
              int y2Lower = (int) ((maxPriceInCandleList - valueLower) * yAxisFactor) + drawingAreaY;
              int x2Lower = x2Upper;
              int y1Lower = (int) ((maxPriceInCandleList - valueLowerPrevious) * yAxisFactor) + drawingAreaY;
              
              g2.drawLine(x1Upper, y1Upper, x2Upper, y2Upper);
              g2.drawLine(x1Lower, y1Lower, x2Lower, y2Lower);
              
          }
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }



    private void drawAlarms(Graphics2D g2) {
        g2.setColor(Color.LIGHT_GRAY);
        for (Alarm alarm : stock.getAlarms()) {
            if (alarm.price <= maxPriceInCandleList) {
                int y = (int) ((maxPriceInCandleList - alarm.price) * yAxisFactor) + drawingAreaY;
                g2.drawLine(drawingAreaX, y, drawingAreaX + drawingAreaWidth, y);
            }
        }
    }



    private void drawCrosshair(Graphics2D g2) {
        if (crosshairX >= drawingAreaX && crosshairY >= drawingAreaY && crosshairX <= drawingAreaX + drawingAreaWidth && crosshairY <= drawingAreaY + drawingAreaHeight) {
            // draw crosshair
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            g2.drawLine(crosshairX, drawingAreaY + drawingAreaHeight, crosshairX, drawingAreaY);
            g2.drawLine(drawingAreaX, crosshairY, drawingAreaX + drawingAreaWidth, crosshairY);
        }
    }

    private double getMinPriceInCandleList() {
        double minPrice = Double.MAX_VALUE;
        for (Candle c : chartModel.getCandles()) {
            minPrice = Math.min(minPrice, c.getLow());
        }
        return minPrice;
    }

    private double getMaxPriceInCandleList() {
        double maxPrice = Double.MIN_VALUE;
        for (Candle c : chartModel.getCandles()) {
            maxPrice = Math.max(maxPrice, c.getHigh());
        }
        return maxPrice;
    }



    /**
     * Sets the chart type.
     *
     * @param chartType the new chart type. Either "Line" or "Candle"
     */
    public void setChartType(String chartType) {
        this.chartType = chartType;
        repaint();
    }
    
    /**
     * Updates the latest candle with a new close price. Called by the main controller when it receives a new price through the web socket connection.
     * 
     * @param price the new price
     */
    public void updateLatestCandle(double price) {
        Candle latestCandle = chartModel.getCandles().get(chartModel.getCandles().size() - 1);
        latestCandle.updateCandle(price);
        repaint();
    }
    

}
