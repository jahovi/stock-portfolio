package watchlist;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * An instance of this renderer handles the coloration of the price cells in the watchlist table in response
 * to changing stock prices.
 */
public class PriceColorRenderer extends DefaultTableCellRenderer {
    
    /**
     * This method is overridden in order to enable custom rendering of the price cells in the watchlist table
     * in response to changing stock prices. For that, it changes the background color of said cells according to 
     * the a field of the Type PriceChangeDirection of the stock.
     *
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel priceLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        WatchlistModel watchlistModel = (WatchlistModel) table.getModel();
        
        switch (watchlistModel.getStockByIndex(table.convertRowIndexToModel(row)).getChangeDirection()) {
        case RISING:
            priceLabel.setBackground(Color.GREEN);
            break;
        case FALLING:
            priceLabel.setBackground(Color.RED);
            break;
        case CONSTANT:
            priceLabel.setBackground(table.getBackground());
            break;
         default:
             priceLabel.setBackground(table.getBackground());
        }
          
        return priceLabel;
        
    }


}
