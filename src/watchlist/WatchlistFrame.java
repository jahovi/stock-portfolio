package watchlist;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableRowSorter;

import main.Stock;

/**
 * An instance of this internal frame holds the watchlist and buttons to add and remove stocks,
 * as well as to show a chart for the selected stock. It serves as a view for the watchlist model.
 */
public class WatchlistFrame extends JInternalFrame {
    private final String ADD_BUTTON_LABEL = "Add Stock";
    private final String REMOVE_BUTTON_LABEL = "Remove Stock";
    private final String SHOW_BUTTON_LABEL = "Show Chart";

    /** The "Add Stock" button action command. */
    public final String ADD_BUTTON_COMMAND = "WatchlistFrame.AddStock";

    /** The "Remove Stock" button action command. */
    public final String REMOVE_BUTTON_COMMAND = "WatchlistFrame.RemoveSelected";

    /** The "Show Chart" button action command. */
    public final String SHOW_BUTTON_COMMAND = "WatchlistFrame.ShowSelected";

    private JTable table;
    private JButton addButton;
    private JButton removeButton;
    private JButton showButton;
    private WatchlistModel watchlistModel;

    /**
     * Constructs a watchlist frame with a table that shows data of the specified WatchlistModel instance.
     *
     * @param watchlistModel the watchlist model to take data from
     */
    public WatchlistFrame(WatchlistModel watchlistModel) {
        super("Watchlist", true, true, false, true); // title, resizable, closable, maximizable, iconifiable
        this.watchlistModel = watchlistModel;

        // Table
        table = new JTable(watchlistModel);
        
//       every time an element is removed from the table model, the renderer seems to fall back to some default. 
//       One thing that seems to prevent this is calling the following method: 
         table.setAutoCreateColumnsFromModel(false);
        
        table.getColumn("Price").setCellRenderer(new PriceColorRenderer());     // assign custom renderer to Price column
//        table.getModel().addTableModelListener(this);
        
        
        table.setRowSorter(new TableRowSorter<WatchlistModel>(watchlistModel));
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            // disable remove and show buttons when there is no selection
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() == -1) {
                    removeButton.setEnabled(false);
                    showButton.setEnabled(false);
                } else {
                    removeButton.setEnabled(true);
                    showButton.setEnabled(true);
                }
            }
        });

        // create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(0, 3));
        addButton = new JButton(ADD_BUTTON_LABEL);
        removeButton = new JButton(REMOVE_BUTTON_LABEL);
        showButton = new JButton(SHOW_BUTTON_LABEL);
        addButton.setActionCommand(ADD_BUTTON_COMMAND);
        removeButton.setActionCommand(REMOVE_BUTTON_COMMAND);
        showButton.setActionCommand(SHOW_BUTTON_COMMAND);
        removeButton.setEnabled(false);
        showButton.setEnabled(false);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(showButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(400, 400);
        setMinimumSize(new Dimension(300, 200));
        setVisible(true);

    }


    /**
     * Gets a reference to the "Add Stock" button. Used to register the main controller
     * as action listener in main.
     *
     * @return a reference to the "Add Stock" button
     */
    public JButton getAddButton() {
        return addButton;
    }


    /**
     * Gets a reference to the "Remove Stock" button. Used to register the main controller
     * as action listener in main.
     *
     * @return a reference to the "Remove Stock" button
     */
    public JButton getRemoveButton() {
        return removeButton;
    }


    /**
     * Gets a reference to the "Show Chart" button. Used to register the main controller
     * as action listener in main.
     *
     * @return a reference to the "Show Chart" button
     */
    public JButton getShowButton() {
        return showButton;
    }

    /**
     * Gets a reference to the currently selected stock in the watchlist table.
     *
     * @return the selected stock
     */
    public Stock getSelectedStock() {
        Stock selectedStock = null;
        int selectedTableRow = table.getSelectedRow();
        if (selectedTableRow != -1) {
            selectedStock = watchlistModel.getStockByIndex(table.convertRowIndexToModel(selectedTableRow));
        }
        return selectedStock;
    }
    


//    /**
//     * every time an element is removed from the table model, the renderer seems to fall back to some default. 
//     * One thing that seem to prevent this is calling the following method: 
//     *   table.setAutoCreateColumnsFromModel(false);
//     * but since I don't understand what that method does, I resort to reapplying the renderer each time the table is changed
//     * via setting this class as table model listener.
//     */
//    @Override
//    public void tableChanged(TableModelEvent e) {
////        table.getColumn("Price").setCellRenderer(new PriceColorRenderer());     // assign custom renderer to Price column
//    }



}
