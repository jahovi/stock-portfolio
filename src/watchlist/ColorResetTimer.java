package watchlist;

import java.awt.event.ActionListener;

import javax.swing.Timer;

import main.Stock;

/**
 * Instances of this class provide timers to reset price cell color in the watchlist after
 * a specified amount of time.
 */
public class ColorResetTimer extends Timer {
    private Stock parent;

    /**
     * Constructs a new color reset timer and associates it with a stock to be able to decide
     * which cell in the watch list to reset. The reset time is hard coded to 800 milliseconds. 
     * The action listener is set to the WatchlistModel instance when a stock is added to it,
     * so that it can trigger a repaint of the affected rows when the timer runs out.
     *
     * @param parent the stock this timer is associated with
     * @param listener the WatchlistModel instance that contains the associated stock
     */
    public ColorResetTimer(Stock parent, ActionListener listener) {
        super(800, listener);
        this.parent = parent;
        setActionCommand("ColorResetTimer");
        setRepeats(false);
    }

    /**
     * Gets the stock this timer is associated with.
     *
     * @return the parent stock
     */
    public Stock getParent() {
        return parent;
    }
    
    
}
