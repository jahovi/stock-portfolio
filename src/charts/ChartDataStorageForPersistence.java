package charts;

import java.awt.Dimension;
import java.awt.Point;

import main.Stock;

/**
 * The data of each chart frame that needs to be persisted when the program closes 
 * in order to be able to restore the chart frame, is stored in an instance of this class.
 */
public class ChartDataStorageForPersistence {
    private final Stock stock;
    private final Dimension size;
    private final Point position;
    
    
    /**
     * Constructs a new chart data storage object and stores in it the specified chart data.
     *
     * @param stock the stock to which the chart corresponds
     * @param size the size of the chart frame to store
     * @param position the position of the chart frame store
     */
    public ChartDataStorageForPersistence(Stock stock, Dimension size, Point position) {
        this.stock = stock;
        this.size = size;
        this.position = position;
    }


    /**
     * Gets the stock which the associated chart frame displays
     *
     * @return the stock that is stored
     */
    public Stock getStock() {
        return stock;
    }


    /**
     * Gets the stored chart frame size.
     *
     * @return the size that is stored
     */
    public Dimension getSize() {
        return size;
    }


    /**
     * Gets the stored chart frame position.
     *
     * @return the position that is stored
     */
    public Point getPosition() {
        return position;
    }
}
