package watchlist;

import java.util.ArrayList;

import main.Stock;

/**
 * This interface specifies capabilities a watchlist model needs to have.
 */
public interface IWatchlistModel {
    
    /**
     * Add a stock to the watchlist
     *
     * @param stock the stock to be added
     */
    void addStock(Stock stock);
    
    /**
     * Removes a stock from the watchlist
     *
     * @param stock the stock to be removed
     */
    void removeStock(Stock stock);
    
    /**
     * Check if the watchlist contains a stock.
     *
     * @param stock the stock to be checked for
     * @return true, if the watchlist contains the specified stock
     */
    boolean contains(Stock stock);
    
    /**
     * Get a reference to a stock in the watchlist by specifying its symbol
     *
     * @param symbol the symbol that identifies the stock to get
     * @return a reference to the stock with the specified symbol in the watchlist
     */
    Stock getStockBySymbol(String symbol);

    /**
     * Set a new price for the stock with the specified symbol.
     *
     * @param stockSymbol the symbol of the stock to set the price for
     * @param newPrice the new price
     */
    void updateStockPrice(String stockSymbol, double newPrice);
    
    /**
     * Get a list of stocks contained in the watchlist.
     *
     * @return a list of the stocks contained in the watchlist
     */
    ArrayList<Stock> getStockList();

    /**
     * Empty the watchlist.
     */
    void clearWatchlist();

}
