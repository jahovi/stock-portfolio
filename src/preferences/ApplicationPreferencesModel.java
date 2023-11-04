package preferences;

import java.util.ArrayList;
import java.util.List;

import charts.ChartDataStorageForPersistence;
import main.Stock;

/**
 * An instance of this class stores the application preferences, the stocks in the watchlist and the opened chart frames.
 * It is stored persistently when the application closes and reconstructed when it restarts.
 */
public class ApplicationPreferencesModel {
    private ArrayList<DataProvider> dataProviderList = new ArrayList<>();
    private ArrayList<Stock> watchlistStocks;
    private List<ChartDataStorageForPersistence> openedChartsData = new ArrayList<>();

    /**
     * Adds a data provider.
     *
     * @param dataProvider the data provider to add
     */
    public void addDataProvider(DataProvider dataProvider) {
        dataProviderList.add(dataProvider);
    }
    
    /**
     * Removes the data provider at the specified index from the list 
     *
     * @param index the index
     */
    public void removeDataProviderListItem(int index) {
        if (index < getDataProviderListLength()) {
            dataProviderList.remove(index);
        }
    }
    
    /**
     * Gets the data provider with the specified index.
     *
     * @param index the index
     * @return the data provider at the specified index in the list
     */
    public DataProvider getDataProviderListItem(int index) {
        return dataProviderList.get(index);
    }
    
    /**
     * Gets the data provider list length.
     *
     * @return the data provider list length
     */
    public int getDataProviderListLength() {
        return dataProviderList.size();
    }
    
    /**
     * Moves the data provider with the specified index to the top of the list. The provider
     * at the top is active.
     *
     * @param index the index of the data provider to activate
     */
    public void setDataProviderActive(int index) {
        // Moves selected DataProvieder to top of list. Whichever is at the top is active.
        DataProvider dataProviderToActivate = getDataProviderListItem(index);
        removeDataProviderListItem(index);
        dataProviderList.add(0, dataProviderToActivate);
    }

    
    // For debugging 
//    public void printDataProviderList() {
//        for (DataProvider item : dataProviderList) {
//            System.out.println(item.getName() + " " + item.getApiKey() + " " + item.getPullUrl()  + " " + item.getPushUrl());
//        }
//    }

    /**
     * Gets the data provider list.
     *
     * @return the data provider list
     */
    public ArrayList<DataProvider> getDataProviderList() {
        return dataProviderList;
    }

    /**
     * Sets the data provider list. Used by the preferences controller to restore the data provider list 
     * on program start.
     *
     * @param dataProviderList the new data provider list
     */
    public void setDataProviderList(ArrayList<DataProvider> dataProviderList) {
        this.dataProviderList = dataProviderList;
    }
    
    /**
     * Gets the active data provider.
     *
     * @return the active data provider
     */
    public DataProvider getActiveDataProvider() {
        // returns the first data provider on the list 
        return getDataProviderListItem(0);
    }

    /**
     * Gets the watchlist stocks.
     *
     * @return the watchlist stocks
     */
    public ArrayList<Stock> getWatchlistStocks() {
        return watchlistStocks;
    }

    /**
     * Sets the watchlist stocks.
     *
     * @param watchlistStocks the new watchlist stocks
     */
    public void setWatchlistStocks(ArrayList<Stock> watchlistStocks) {
        this.watchlistStocks = watchlistStocks;
    }

    /**
     * Gets the opened charts data. Used for restoring charts on restart.
     *
     * @return the opened charts data
     */
    public List<ChartDataStorageForPersistence> getOpenedChartsData() {
        return openedChartsData;
    }

    /**
     * Sets the opened charts data. Used to store information about charts prior to shutdown. 
     *
     * @param openedCharts the new opened charts data
     */
    public void setOpenedChartsData(List<ChartDataStorageForPersistence> openedCharts) {
        this.openedChartsData = openedCharts;
    }

}
