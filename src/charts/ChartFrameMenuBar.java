package charts;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 * Every chart frame contains an instance of this class as its menu bar.
 * The menu bar is used by the user to manipulate the displayed chart.
 */
public class ChartFrameMenuBar extends JMenuBar {

    private static final String CHART_TYPE_MENU_TITLE = "Chart Type";
    private static final String INTERVALL_MENU_TITLE = "Interval";
    private static final String INDICATORS_AND_ALARMS_MENU_TITLE = "Analysis";
    
    private static final String CANDLE_ITEM_TEXT = "Candle";
    private static final String LINE_ITEM_TEXT = "Line";
    private static final String ONE_MINUTE_INTERVALL_ITEM_TEXT = "1 Minute";
    private static final String FIVE_MINUTE_INTERVALL_ITEM_TEXT = "5 Minutes";
    private static final String FIFTEEN_MINUTE_INTERVALL_ITEM_TEXT = "15 Minutes";
    private static final String THIRTY_MINUTE_INTERVALL_ITEM_TEXT = "30 Minutes";
    private static final String SIXTY_MINUTE_INTERVALL_ITEM_TEXT = "60 Minutes";
    private static final String DAY_INTERVALL_ITEM_TEXT = "Day";
    private static final String WEEK_INTERVALL_ITEM_TEXT = "2 Weeks";
    private static final String MONTH_INTERVALL_ITEM_TEXT = "Month";
    private static final String INDICATORS_ITEM_TEXT = "Indicators";
    private static final String ALARMS_ITEM_TEXT = "Alarms";

    private JRadioButtonMenuItem candleItem;
    private JRadioButtonMenuItem lineItem;
    
    private JRadioButtonMenuItem oneMinuteIntervalItem;
    private JRadioButtonMenuItem fiveMinuteIntervalItem;
    private JRadioButtonMenuItem fifteenMinuteIntervalItem;
    private JRadioButtonMenuItem thirtyMinuteIntervalItem;
    private JRadioButtonMenuItem sixtyMinuteIntervalItem;
    private JRadioButtonMenuItem dayIntervalItem;
    private JRadioButtonMenuItem weekIntervalItem;
    private JRadioButtonMenuItem monthIntervalItem;
    
    private JMenuItem indicatorsItem;
    private JMenuItem alarmsItem;
    
    private JTextField numIntervalsField;
    private JMenu intervalMenu;
    
    

    /**
     * Constructs a chart frame menu bar. 
     */
    public ChartFrameMenuBar() {
        add(createChartTypeMenu());
        add(createIntervalMenu());
        add(createIndicatorsAndAlarmsMenu());
        
        numIntervalsField = new JTextField("10", 20);
        numIntervalsField.setPreferredSize(new Dimension(10, getPreferredSize().height));
        numIntervalsField.setMaximumSize(new Dimension(10, getPreferredSize().height));
        
        JLabel numIntervalsLabel = new JLabel("Number of Intervals: ");
        add(new JSeparator(SwingConstants.VERTICAL));
        add(Box.createHorizontalGlue());
        add(numIntervalsLabel);
        add(numIntervalsField);
    }

    private JMenu createIndicatorsAndAlarmsMenu() {
        // create menu
        JMenu indicatorsAndAlarmsMenu = new JMenu(INDICATORS_AND_ALARMS_MENU_TITLE);
        // createItems
        indicatorsItem = new JMenuItem(INDICATORS_ITEM_TEXT);
        alarmsItem = new JMenuItem(ALARMS_ITEM_TEXT); 
        indicatorsAndAlarmsMenu.add(indicatorsItem);
        indicatorsAndAlarmsMenu.add(alarmsItem);
        return indicatorsAndAlarmsMenu;
    }

    private JMenu createIntervalMenu() {
        intervalMenu = new JMenu(INTERVALL_MENU_TITLE);
        // create items
        oneMinuteIntervalItem = new JRadioButtonMenuItem(ONE_MINUTE_INTERVALL_ITEM_TEXT);
        fiveMinuteIntervalItem = new JRadioButtonMenuItem(FIVE_MINUTE_INTERVALL_ITEM_TEXT);
        fifteenMinuteIntervalItem = new JRadioButtonMenuItem(FIFTEEN_MINUTE_INTERVALL_ITEM_TEXT);
        thirtyMinuteIntervalItem = new JRadioButtonMenuItem(THIRTY_MINUTE_INTERVALL_ITEM_TEXT);
        sixtyMinuteIntervalItem = new JRadioButtonMenuItem(SIXTY_MINUTE_INTERVALL_ITEM_TEXT);
        dayIntervalItem = new JRadioButtonMenuItem(DAY_INTERVALL_ITEM_TEXT);
        weekIntervalItem = new JRadioButtonMenuItem(WEEK_INTERVALL_ITEM_TEXT);
        monthIntervalItem = new JRadioButtonMenuItem(MONTH_INTERVALL_ITEM_TEXT);
        ButtonGroup group = new ButtonGroup();
        group.add(oneMinuteIntervalItem);
        group.add(fiveMinuteIntervalItem);
        group.add(fifteenMinuteIntervalItem);
        group.add(thirtyMinuteIntervalItem);
        group.add(sixtyMinuteIntervalItem);
        group.add(dayIntervalItem);
        group.add(weekIntervalItem);
        group.add(monthIntervalItem);
        dayIntervalItem.setSelected(true);

        intervalMenu.add(oneMinuteIntervalItem);
        intervalMenu.add(fiveMinuteIntervalItem);
        intervalMenu.add(fifteenMinuteIntervalItem);
        intervalMenu.add(thirtyMinuteIntervalItem);
        intervalMenu.add(sixtyMinuteIntervalItem);
        intervalMenu.add(dayIntervalItem);
        intervalMenu.add(weekIntervalItem);
        intervalMenu.add(monthIntervalItem);
        return intervalMenu;
    }

    private JMenu createChartTypeMenu() {
        // create menu
        JMenu chartTypeMenu = new JMenu(CHART_TYPE_MENU_TITLE);
        chartTypeMenu.setMnemonic(KeyEvent.VK_C);
        
        // create items
        ButtonGroup group = new ButtonGroup();
        candleItem = new JRadioButtonMenuItem(CANDLE_ITEM_TEXT);
        lineItem = new JRadioButtonMenuItem(LINE_ITEM_TEXT);
        candleItem.setSelected(true);
        group.add(candleItem);
        group.add(lineItem);
        chartTypeMenu.add(candleItem);
        chartTypeMenu.add(lineItem);
        
        return chartTypeMenu;
    }

    /**
     * @return the "Candle" item of the chart type menu 
     */
    public JRadioButtonMenuItem getCandleItem() {
        return candleItem;
    }

    /**
     * @return the "Line" item of the chart type menu
     */
    public JRadioButtonMenuItem getLineItem() {
        return lineItem;
    }

    /**
     * @return the one minute interval item of the interval menu
     */
    public JRadioButtonMenuItem getOneMinuteIntervalItem() {
        return oneMinuteIntervalItem;
    }

    /**
     * @return the five minute interval item of the interval menu
     */
    public JRadioButtonMenuItem getFiveMinuteIntervalItem() {
        return fiveMinuteIntervalItem;
    }

    /**
     * @return the fifteen minute interval item of the interval menu
     */
    public JRadioButtonMenuItem getFifteenMinuteIntervalItem() {
        return fifteenMinuteIntervalItem;
    }

    /**
     * @return the thirty minute interval item of the interval menu
     */
    public JRadioButtonMenuItem getThirtyMinuteIntervalItem() {
        return thirtyMinuteIntervalItem;
    }

    /**
     * @return the sixty minute interval item of the interval menu
     */
    public JRadioButtonMenuItem getSixtyMinuteIntervalItem() {
        return sixtyMinuteIntervalItem;
    }

    /**
     * @return the day interval item of the interval menu
     */
    public JRadioButtonMenuItem getDayIntervalItem() {
        return dayIntervalItem;
    }

    /**
     * @return the week interval item of the interval menu
     */
    public JRadioButtonMenuItem getWeekIntervalItem() {
        return weekIntervalItem;
    }

    /**
     * @return the month interval item of the interval menu
     */ 
    public JRadioButtonMenuItem getMonthIntervalItem() {
        return monthIntervalItem;
    }

    /**
     * @return the indicators item of the indicators and alarms menu
     */
    public JMenuItem getIndicatorsItem() {
        return indicatorsItem;
    }

    /**
     * @return the alarms item of the indicators and alarms menu
     */
    public JMenuItem getAlarmsItem() {
        return alarmsItem;
    }

    /**
     * @return the text field where the user enters the number of intervals to display
     */
    public JTextField getNumIntervalsField() {
        return numIntervalsField;
    }

//    /**
//     * Gets the interval menu.
//     *
//     * @return the interval menu
//     */
//    public JMenu getIntervalMenu() {
//        return intervalMenu;
//    }


}
