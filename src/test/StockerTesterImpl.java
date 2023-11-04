package test;

import java.util.*;

import charts.*;
import main.Stock;
import stocker.IStockerTester;
import watchlist.WatchlistModel;

public class StockerTesterImpl implements IStockerTester {
    WatchlistModel watchlistModel = new WatchlistModel();
    WatchlistModel stocksWithAlarms = new WatchlistModel();

    
    @Override
    public String getMatrNr() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getEmail() {
        return "jan.hofmann1@studium.fernuni-hagen.de";
    }

    @Override
    public void clearWatchlist() {
        watchlistModel.clearWatchlist();
    }

    @Override
    public void addWatchlistEntry(String stockId) {
        Stock testStock = new Stock();
        testStock.setSymbol(stockId);
        watchlistModel.addStock(testStock);
    }

    @Override
    public void removeWatchlistEntry(String stockId) {
        Stock stockToRemove = watchlistModel.getStockBySymbol(stockId);
        watchlistModel.removeStock(stockToRemove);
    }

    @Override
    public String[] getWatchlistStockIds() {
        return watchlistModel.getWatchlistStockIds();
    }

    @Override
    public void clearAlarms(String stockId) {
        Stock stock = stocksWithAlarms.getStockBySymbol(stockId);
        stock.clearAlarms();
        stocksWithAlarms.removeStock(stock);
    }

    @Override
    public void clearAllAlarms() {
        for (Stock stock : stocksWithAlarms.getStockList()) {
            stock.clearAlarms();
        }
        stocksWithAlarms.clearWatchlist();
    }

    @Override
    public void addAlarm(String stockId, double threshold) {
        Stock stock;
        if ((stock = stocksWithAlarms.getStockBySymbol(stockId)) != null) {
            stock.addAlarmByPrice(threshold);
        } else {
            stock = new Stock();
            stock.setSymbol(stockId);
            stock.addAlarmByPrice(threshold);
            stocksWithAlarms.addStock(stock);
        }
    }

    @Override
    public void removeAlarm(String stockId, double threshold) {
        Stock stock = stocksWithAlarms.getStockBySymbol(stockId);
        stock.removeAlarmByPrice(threshold);
    }

    @Override
    public double[] getAlarms(String stockId) {
        Stock stock = stocksWithAlarms.getStockBySymbol(stockId);
        double[] result = stock.getAlarmPrices();
        return result;
    }

    @Override
    public Set<String> getAlarmStockIds() {
        Set<String> result = new HashSet<>();
        for (Stock stock : stocksWithAlarms.getStockList()) {
            if (stock.hasAlarm()) {
                result.add(stock.getSymbol());
            }
        }
        return result;
    }

    @Override
    public double[] getMovingAverage(int n, double[] stockData) {
        ChartModel chartModel = new ChartModel(null);
        List<Candle> candleList = new ArrayList<>();
        for (double price : stockData) {
            candleList.add(new Candle(0, 0, 0, price, 0));
        }
        chartModel.setCandles(candleList);
        double[] result = chartModel.calculateMovingAverage(n);
        return result;
    }

    @Override
    public double[] getUpperBollingerBand(double f, int n, double[] stockData) {
        ChartModel chartModel = new ChartModel(null);
        List<Candle> candleList = new ArrayList<>();
        for (double price : stockData) {
            candleList.add(new Candle(0, 0, 0, price, 0));
        }
        chartModel.setCandles(candleList);
        double[] result = chartModel.calculateUpperBollingerBand(n, (int) f);
        return result;
    }

    @Override
    public double[] getLowerBollingerBand(double f, int n, double[] stockData) {
        ChartModel chartModel = new ChartModel(null);
        List<Candle> candleList = new ArrayList<>();
        for (double price : stockData) {
            candleList.add(new Candle(0, 0, 0, price, 0));
        }
        chartModel.setCandles(candleList);
        double[] result = chartModel.calculateLowerBollingerBand(n, (int) f);
        return result;
    }

}
