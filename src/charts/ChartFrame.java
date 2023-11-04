package charts;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import main.Stock;

/**
 * Objects of this class are internal frames that house charts chart panels, which in turn draw charts.
 * ChartFrames also contain a menu bar to manipulate the chart.
 */
public class ChartFrame extends JInternalFrame {
    private final  ChartFrameMenuBar menuBar;
    private final  ChartPanel chartPanel;
    private final  IChartViewListener controller;
    private final Stock stock;
    
    /**
     * Constructs a chart frame. The frame in turn constructs the chart panel it contains.
     *
     * @param stock the stock to which this chart corresponds
     * @param controller the controller that processes user interactions with this chart
     * @param chartModel the chart model that contains the data to be displayed by this chart
     */
    public ChartFrame(Stock stock, IChartViewListener controller, ChartModel chartModel) {
        super(stock.getDescription(), true, true, true, true);
        this.controller = controller;
        this.stock = stock;
        menuBar = new ChartFrameMenuBar();
        chartPanel = new ChartPanel(stock, chartModel);
        
        setJMenuBar(menuBar);
        add(chartPanel);
        
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setMinimumSize(new Dimension(700, 300));
        setVisible(true);
        
        registerActionListeners();
    }

    private void registerActionListeners() {
        // pipe button events to controller
        menuBar.getLineItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.lineButtonClicked(chartPanel);
            }
        });
        
        menuBar.getCandleItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.candleButtonClicked(chartPanel);
            }
        });
        
        menuBar.getOneMinuteIntervalItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.oneMinuteButtonClicked();
                } catch (TooManyIntervalsException e1) {
                    menuBar.getNumIntervalsField().setText(String.valueOf(e1.getMaximumIntervalNumber()));
                }
            }
        });
        
        menuBar.getFiveMinuteIntervalItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.fiveMinuteButtonClicked();
                } catch (TooManyIntervalsException e1) {
                    menuBar.getNumIntervalsField().setText(String.valueOf(e1.getMaximumIntervalNumber()));
                }
            }
        });
        
        menuBar.getFifteenMinuteIntervalItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.fifteenMinuteButtonClicked();
                } catch (TooManyIntervalsException e1) {
                    menuBar.getNumIntervalsField().setText(String.valueOf(e1.getMaximumIntervalNumber()));
                }
            }
        });
        
        menuBar.getThirtyMinuteIntervalItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.thirtyMinuteButtonClicked();
                } catch (TooManyIntervalsException e1) {
                    menuBar.getNumIntervalsField().setText(String.valueOf(e1.getMaximumIntervalNumber()));
                }
            }
        });
        
        
        menuBar.getSixtyMinuteIntervalItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.sixtyMinuteButtonClicked();
                } catch (TooManyIntervalsException e1) {
                    menuBar.getNumIntervalsField().setText(String.valueOf(e1.getMaximumIntervalNumber()));
                }
            }
        });
        
        menuBar.getDayIntervalItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.dayButtonClicked();
                } catch (TooManyIntervalsException e1) {
                    menuBar.getNumIntervalsField().setText(String.valueOf(e1.getMaximumIntervalNumber()));
                }
            }
        });
        
        menuBar.getWeekIntervalItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.weekButtonClicked();
                } catch (TooManyIntervalsException e1) {
                    menuBar.getNumIntervalsField().setText(String.valueOf(e1.getMaximumIntervalNumber()));
                }
            }
        });
        
        menuBar.getMonthIntervalItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.monthButtonClicked();
                } catch (TooManyIntervalsException e1) {
                    menuBar.getNumIntervalsField().setText(String.valueOf(e1.getMaximumIntervalNumber()));
                }
            }
        });
        
        menuBar.getNumIntervalsField().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                    try {
                        controller.changeNumIntervals(Integer.parseInt(((JTextField) e.getSource()).getText()));
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(chartPanel, "Please enter an integer.");
//                        e1.printStackTrace();
                    } catch (TooManyIntervalsException e1) {
                        menuBar.getNumIntervalsField().setText(String.valueOf(e1.getMaximumIntervalNumber()));
                        System.out.println("Caught intervals field");
                    } catch (IllegalArgumentException e1) {
                        JOptionPane.showMessageDialog(chartPanel, "Please enter a positive integer.");
                    }
            }
        });
        
        menuBar.getIndicatorsItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showIndicatorsDialog(chartPanel);
            }
        });
        
        menuBar.getAlarmsItem().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAlarmsDialog(chartPanel, stock);
            }
        });
    }

    /**
     * @return the stock to which this chart corresponds
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * @return the chart panel contained in this frame
     */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }
    
    
    
}
