package stockFinder;

import main.Stock;

/**
 * Implementing classes provide a method that a search term as a string,
 * query a data provider and return an array of stocks that match the search.
 */
public interface IStockFinder {
    
    /**
     * Find stocks matching the specified search term.
     *
     * @param searchterm the search term to look up
     * @return a stock array that matches the search term
     */
    Stock[] findMatchingStocks(String searchterm);
    
}
