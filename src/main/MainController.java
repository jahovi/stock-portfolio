package main;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import charts.*;
import preferences.*;
import stockFinder.StockFinderDialog;
import watchlist.*;
import webSocket.*;
import webSocket.TradeMessage.Data;

/**
 * An instance of this class controls the interactions between the major program components: preferences, watchlist and charts.
 */
public class MainController extends WindowAdapter implements ActionListener, IWebSocketListener, InternalFrameListener {
    private JDialog preferencesDialog;
    private StockFinderDialog stockFinderDialog;
    private WatchlistFrame watchlist;
    private IWatchlistModel watchlistModel;
    private ApplicationPreferencesModel preferences;
    private IPreferencesPersistence persistence;
    private WebSocketClientWrapper webSocketWrapper;
    private MainFrame mainFrame;
    private MainMenuBar mainMenuBar;
    private Gson gson = new GsonBuilder().create();
    private ChartFrameMap openedChartsPerStock = new ChartFrameMap();
    
    
    /**
     *  Constructs the main controller, establishes a connection to the active data provider on program start and 
     *  restores both watchlist and charts from persistent storage.
     *
     * @param preferencesDialog the preferences dialog
     * @param stockFinderDialog the stock finder dialog
     * @param watchlist the watchlist
     * @param watchlistModel the watchlist model
     * @param preferences the preferences model
     * @param persistence an object that provides methods to persistenly store application preferences
     * @param mainFrame the main frame
     * @param mainMenuBar the main menu bar
     */
    public MainController(JDialog preferencesDialog, StockFinderDialog stockFinderDialog, WatchlistFrame watchlist,
            IWatchlistModel watchlistModel, ApplicationPreferencesModel preferences, IPreferencesPersistence persistence, MainFrame mainFrame, MainMenuBar mainMenuBar) {
        this.preferencesDialog = preferencesDialog;
        this.stockFinderDialog = stockFinderDialog;
        this.watchlist = watchlist;
        this.watchlistModel = watchlistModel;
        this.preferences = preferences;
        this.persistence = persistence;
        this.mainFrame = mainFrame;
        this.mainMenuBar = mainMenuBar;
        try {
            this.webSocketWrapper = new WebSocketClientWrapper(this, preferences.getActiveDataProvider().getFullPushUrl());
            webSocketWrapper.connectBlocking();
            if (webSocketWrapper.isConnected()) {
                System.out.println("Connection established");
                JOptionPane.showMessageDialog(mainFrame, "Connection established to: " + preferences.getActiveDataProvider().getName());
            }
        } catch (Exception e) {
            System.out.println("Web Socket connection failed. Enter valid Url and API key in preferences.");
            e.printStackTrace();
        }
        restoreWatchlistFromPersistence();
        restoreChartsFromPersistence();
    }

    /**
     * Handles events from the user interacting with the "Preferences", "Stock Finder", "Watchlist"
     * and "Exit" buttons on the main menu bar, as well as the buttons within the watchlist frame and
     * the stock finder.
     *
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Preferences")) {
            showPreferencesDialog();
        } else if (e.getActionCommand().equals("Stock Finder")) {
            showStockfinder();
        } else if (e.getActionCommand().equals("Watchlist")) {
            showWatchlist();
        } else if (e.getActionCommand().equals(watchlist.ADD_BUTTON_COMMAND)) {
            showStockfinder();
        } else if (e.getActionCommand().equals(watchlist.REMOVE_BUTTON_COMMAND)) {
            removeStockFromWatchlist();
        } else if (e.getActionCommand().equals(watchlist.SHOW_BUTTON_COMMAND)) {
            showChartFromWatchlist();
        } else if (e.getActionCommand().equals(stockFinderDialog.ADD_TO_WATCHLIST_BUTTON_COMMAND)) {
            addStockFromStockFinderToWatchlist();
        } else if (e.getActionCommand().equals("Preferences.saveAndClose")) {
            changeActiveDataProvider();     
        } else if (e.getActionCommand().equals(stockFinderDialog.OPEN_CHART_BUTTON_COMMAND)) {
            showChartFromStockFinder();
        } else if (e.getActionCommand().equals("Exit")) {
            exit();
        } else if (e.getActionCommand().equals("Reconnect")) {
            reconnect();
        }

    }


    private void reconnect() {
        webSocketWrapper.changeUri(preferences.getActiveDataProvider().getFullPushUrl());
        webSocketWrapper.connectBlocking();
        if (webSocketWrapper.isConnected()) {
            JOptionPane.showMessageDialog(mainFrame, "Connection established to: " + preferences.getActiveDataProvider().getName());
            for (Stock stock : watchlistModel.getStockList()) {
                webSocketWrapper.subscribe(stock.getSymbol());
            } 
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Could not connect to: " + preferences.getActiveDataProvider().getName());
            
        }
    }

    private void exit() {
        storeDataToBePersistedInPreferences();
        System.exit(0);
    }

    private void addStockFromStockFinderToWatchlist() {
        Stock stock = stockFinderDialog.getSelectedStock();
        addStockToWatchlist(stock);
    }
    

    private void addStockToWatchlist(Stock stock) {
        if (!watchlistModel.contains(stock)) {
//            quoteRequester.setCurrentAndPreviousClosePrice(stock);
            StaticStockQuoteRequester.setCurrentAndPreviousClosePrice(stock, preferences.getActiveDataProvider());
            watchlistModel.addStock(stock);
            webSocketWrapper.subscribe(stock.getSymbol());
        }
    }
    
    private void showChartFromStockFinder() {
        Stock stock = stockFinderDialog.getSelectedStock();
//        quoteRequester.setCurrentAndPreviousClosePrice(stock);
        StaticStockQuoteRequester.setCurrentAndPreviousClosePrice(stock, preferences.getActiveDataProvider());
        webSocketWrapper.subscribe(stockFinderDialog.getSelectedStock().getSymbol());
        showChart(stock);
    }

    private void showChartFromWatchlist() {
        Stock stock = watchlist.getSelectedStock();
        showChart(stock);

    }
    
    private void restoreChartsFromPersistence() {
        for (ChartDataStorageForPersistence chartData : preferences.getOpenedChartsData()) {
            ChartFrame newChart = showChart(chartData.getStock());
            newChart.setSize(chartData.getSize());
            newChart.setLocation(chartData.getPosition());
            webSocketWrapper.subscribe(chartData.getStock().getSymbol());
            
        }
    }

    private ChartFrame showChart(Stock stock) {
        ChartModel chartModel = new ChartModel(preferences, stock);
        IChartViewListener chartController = new ChartController(chartModel);
        ChartFrame chartFrame = new ChartFrame(stock, chartController, chartModel);
        mainFrame.addInternalFrame(chartFrame);
        
        // put new chartFrame in the middle and into the foreground
        Dimension desktopPaneSize = chartFrame.getDesktopPane().getSize();
        Dimension chartFrameSize = new Dimension(chartFrame.getSize().width, chartFrame.getSize().height);
        chartFrame.setLocation((desktopPaneSize.width - chartFrameSize.width) / 2, (desktopPaneSize.height - chartFrameSize.height) / 2); // put it in the middle of the mainframe
        chartFrame.toFront();
        
        // add this as window listener to new chartFrame in order to detect its closing to remove it from the opened charts list
        chartFrame.addInternalFrameListener(this);
        
        openedChartsPerStock.addChartFrame(stock.getSymbol(), chartFrame);
        
        addChartItemToWindowsMenu(chartFrame);
        
        return chartFrame;
        
    }
    
    private void addChartItemToWindowsMenu(ChartFrame chartFrame) {
        mainMenuBar.getWindowsMenu().add(new WindowsMenuChartItem(chartFrame));
    }

    private void removeStockFromWatchlist() {
        Stock stock = watchlist.getSelectedStock();
        watchlistModel.removeStock(stock);
        webSocketWrapper.unsubscribe(stock.getSymbol());
    }

    private void showWatchlist() {
        watchlist.show();
        watchlist.toFront();
    }

    private void showStockfinder() {
        stockFinderDialog.setVisible(true);
    }

    private void showPreferencesDialog() {
        preferencesDialog.setVisible(true);
    }

    /**
     * Process trade messages received from the data provider through the web socket connection. 
     * The message gets parsed and prices in the watchlist and the charts updated accordingly.
     *
     * @param message the trade message in JSON format
     */
    @Override
    public void processWebSocketTradeMessage(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TradeMessage tradeMessageObject = gson.fromJson(message, TradeMessage.class);
                //        tradeMessageObject.roundPrices();
                if (!tradeMessageObject.getType().equals("ping")) { // Don't process ping messages
                    for (Iterator<Data> iterator = tradeMessageObject.getData().iterator(); iterator.hasNext();) {
                        Data data = iterator.next();
                        
                        // Update watchlist
                        watchlistModel.updateStockPrice(data.getSymbol(), data.getPrice());
                        
                        // Update charts
                        List<ChartFrame>  chartsToUpdate = openedChartsPerStock.getChartFrames(data.getSymbol());
                        if (chartsToUpdate != null) {
                            for (ChartFrame chartFrame : chartsToUpdate) {
                                chartFrame.getStock().setPrice(data.getPrice());
                                chartFrame.getChartPanel().updateLatestCandle(data.getPrice());
                                chartFrame.repaint();
                            } 
                        }
                        
                    }
                }
            }
        });
    }
    
    /**
     * Called when the user closes the main frame. Stores data to be persisted in
     * the preferences model and exits the application. 
     *
     */
    @Override
    public void windowClosing(WindowEvent e) {
        storeDataToBePersistedInPreferences();
        System.exit(0);
    }

    private void storeDataToBePersistedInPreferences() {
        // closing the main frame triggers persisting the watchlist
        preferences.setWatchlistStocks(watchlistModel.getStockList());
        // persist open charts
        List<ChartFrame> openedCharts = openedChartsPerStock.getChartFrames();
        List<ChartDataStorageForPersistence> chartDataForPersistenceList = new ArrayList<>();
        for (ChartFrame chartFrame : openedCharts) {
            Stock stock = chartFrame.getStock();
            Dimension size = chartFrame.getSize();
            Point location = chartFrame.getLocation();
            chartDataForPersistenceList.add(new ChartDataStorageForPersistence(stock, size, location));
        }
        preferences.setOpenedChartsData(chartDataForPersistenceList);
        
        persistence.storeApplicationPreferences(preferences);
    }
    
    private void restoreWatchlistFromPersistence() {
        try {
            for (Stock s : preferences.getWatchlistStocks()) {
                addStockToWatchlist(s);
            }
        } catch (Exception e) {
            System.out.println("Watchlist empty");
        }
    }
    
    private void changeActiveDataProvider() {
        // close all chart windows and clear watchlist
        try {
            watchlistModel.clearWatchlist();
            for (ChartFrame chartFrame: openedChartsPerStock.getChartFrames()) {
                chartFrame.dispose();
            }
            
            webSocketWrapper.changeUri(preferences.getActiveDataProvider().getFullPushUrl());
            webSocketWrapper.connectBlocking();
            if (webSocketWrapper.isConnected()) {
                JOptionPane.showMessageDialog(mainFrame, "Connection established to: " + preferences.getActiveDataProvider().getName());
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Could not connect to: " + preferences.getActiveDataProvider().getName());
            }


            
        } catch (Exception e) {
            System.out.println("Web Socket connection failed. Enter valid Url and API key in preferences.");
//            JOptionPane.showMessageDialog(mainFrame, "Web Socket connection failed. Enter valid Url and API key in preferences.");
            e.printStackTrace();
        }
    }


    /**
     * Called when the user closes a chart frame. Removes that chart frame from
     * the chart frame map.
     *
     */
    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        // chart frame closed
        openedChartsPerStock.removeChartFrame((ChartFrame) e.getInternalFrame());
    }
    
    
    // ---------- unused InternalFrameListener methods ---------
    /**
     * Not used.

     */
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {}
    
    /**
     * Not used.

     */
    @Override
    public void internalFrameClosing(InternalFrameEvent e) {}
    
    /**
     * Not used.

     */
    @Override
    public void internalFrameIconified(InternalFrameEvent e) {}

    /**
     * Not used.

     */
    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {}

    /**
     * Not used.

     */
    @Override
    public void internalFrameActivated(InternalFrameEvent e) {}

    /**
     * Not used.

     */
    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {}
    

}
