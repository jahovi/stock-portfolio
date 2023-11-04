package watchlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import main.Stock;

/**
 * An instance of this class functions as the watchlist's data model. It holds the stocks that are contained in the 
 * watchlist, specifies the column names and overrides AbstractTableModel methods to act as a model for the WatchlistFrame,
 * which is the view for the data contained herein.
 */
public class WatchlistModel extends AbstractTableModel implements IWatchlistModel, ActionListener {

    private String[] columnNames = { "Name", "Symbol", "Price", "%" };
    private ArrayList<Stock> stockList = new ArrayList<>();
    
    /**
     * Gets an ArrayList of the stocks in the watchlist.
     *
     * @return the stock list
     */
    @Override
    public ArrayList<Stock> getStockList() {
        return stockList;
    }

    /**
     * Returns the number of rows in the model, which is the number of
     * stocks in the watchlist.
     */
    @Override
    public int getRowCount() {
        return stockList.size();
    }

    /**
     * Returns the number of columns of the table.
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Gets the name of the specified column.
     *
     * @param column the index of the column to get the name for
     * @return the column name as specified in the columnNames field
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Returns the value for the cell rowIndex, columnIndex. To do that, this method
     * asks the stock that corresponds to the specified row for an array of its field values.
     *
     * @param rowIndex the row index in the table to get the values for
     * @param columnIndex the column index in the table to get the value for
     * @return the value for the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] tableRow = stockList.get(rowIndex).toObjectArray();
        return tableRow[columnIndex];
    }
    
    /**
     * Gets the column class to enable sorting and correct presentation of 
     * the different types of values in the various columns.
     *
     * @param c the column index
     * @return the column class
     */
    @Override
    public Class<?> getColumnClass(int c) {
        if (!stockList.isEmpty()) {
            return getValueAt(0, c).getClass();
        } else {
            return Integer.class;
        }
    }

    /**
     * Adds the specified stock to the watchlist.
     *
     * @param stock the stock to be added
     */
    @Override
    public void addStock(Stock stock) {
        if (!contains(stock)) {
            stock.resetChangeDirection();
            stockList.add(stock);
            stock.getColorResetTimer().addActionListener(this);
            fireTableDataChanged();
        }
    }

    /**
     * Removes the specified stock from the watchlist.
     *
     * @param stock the stock to be removed
     */
    @Override
    public void removeStock(Stock stock) {
        int index = stockList.indexOf(stock);
        stockList.remove(stock);
        fireTableRowsDeleted(index, index);
    }
    
    /**
     * Gets the stock with the specified row index.
     *
     * @param index the row index
     * @return the stock in the specified row 
     */
    public Stock getStockByIndex(int index) {
        return stockList.get(index);
    }
    
    /**
     * Gets the stock with the specified symbol.
     *
     * @param symbol the symbol to get the stock for
     * @return the stock with the specified symbol
     */
    @Override
    public Stock getStockBySymbol(String symbol) {
        // returns null if not found
        Stock result = null;
        for (Stock s : stockList){
            if (s.getSymbol().equals(symbol)) {
                result = s;
                break;
            }
        }
        return result;
    }

    /**
     * Check if the watchlist contains the specified stock.
     *
     * @param stock the stock to be checked for
     * @return true, if the watchlist contains the specified stock
     */
    @Override
    public boolean contains(Stock stock) {
        boolean result = false;
        for (Stock s : stockList) {
            if (s.getSymbol().equals(stock.getSymbol())) result = true;
        }
        return result;
    }
    
    /**
     * Set a new price for the stock with the specified symbol. The stock will set its
     * price change direction value according to the new price which also changes price cell background color.
     *  Therefore, this method also starts the stock's timer to reset the change direction.
     *
     * @param stockSymbol the symbol of the stock to set the price for
     * @param newPrice the new price
     */
    @Override
    public void updateStockPrice(String stockSymbol, double newPrice) {
        Stock target;
        for (int i = 0; i < stockList.size(); i++) {
            if ((target = stockList.get(i)).getSymbol().equals(stockSymbol)) {
                target.setPrice(newPrice);
                fireTableRowsUpdated(i, i);     
                target.getColorResetTimer().restart();
                break;
            }
        }
    }
    
    /**
     * Empty the watchlist.
     */
    @Override
    public void clearWatchlist() {
        if (!stockList.isEmpty()) {
            stockList.clear();
            fireTableRowsDeleted(0, stockList.size());
        }
    }

    /**
     * Handles the events fired by each stocks timer and resets change direction and therefore price cell render color.
     *
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ColorResetTimer source = (ColorResetTimer) e.getSource();
        Stock parent = source.getParent();
        parent.resetChangeDirection();
        int index = stockList.indexOf(parent);
        fireTableRowsUpdated(index, index);
    }
    
    /**
     * Gets the symbols of all the stocks contained in the watchlist.
     * Used for the provided unit tests.
     *
     * @return a String array of the stock symbols.
     */
    public String[] getWatchlistStockIds() {
        String[] result = new String[stockList.size()];
        for (int i = 0; i < stockList.size(); i++) {
            result[i] = stockList.get(i).getSymbol();
        }
        return result;
    }

    
}
