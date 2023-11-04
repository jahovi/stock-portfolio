package preferences;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 * An instance of this class displays a list of data providers in the preferences dialog and provides
 * buttons to edit the list and individual data providers. Therefore it acts as a preferences view.
 */
public class DataProviderPreferencesPanel extends JPanel {
    private ApplicationPreferencesModel tableData;
    private AbstractTableModel tableModel;
    private JButton editButton;
    private JButton addButton;
    private JButton deleteButton;
    private JTable table;
    private JButton toTopButton;

    /**
     * Constructs a data provider preferences panel.
     *
     * @param tableData the ApplicationPreferencesModel that contains the data provider information to be displayed on this panel
     */
    public DataProviderPreferencesPanel(ApplicationPreferencesModel tableData) {
        super(new GridLayout(1, 2));

        this.tableData = tableData;
        
        tableModel = new MyTableModel();
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 200));  // change to appropriate size
        add(scrollPane);

        // create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        toTopButton = new JButton("To Top");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        addButton = new JButton("Add");
        JLabel hint = new JLabel("The first data provider in the list is active.");

        // Disable edit and delete buttons until a table item is selected
        toTopButton.setEnabled(false);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(new TableItemSelectedListener());

        buttonPanel.add(toTopButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        buttonPanel.add(hint);
        add(buttonPanel);

    }
    
    private final class TableItemSelectedListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (table.getSelectedRow() == -1) {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
                toTopButton.setEnabled(false);
            } else {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
                toTopButton.setEnabled(true);
            }
        }
    }

    private final class MyTableModel extends AbstractTableModel {
        private String[] columnNames = { "Name", "API Key", "Pull URL", "Push URL" };

        @Override
        public int getRowCount() {
            return tableData.getDataProviderListLength();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public String getValueAt(int rowIndex, int columnIndex) {
            String[] tableRow = tableData.getDataProviderListItem(rowIndex).toStringArray();
            return tableRow[columnIndex];
        }

    }

    /**
     * Registers the preferences controller as action listener at the buttons on this panel in order 
     * to change the preferences data in response to user input.
     *
     * @param controller the main controller instance to register 
     */
    public void addController(PreferencesController controller) {
        toTopButton.addActionListener(controller);
        editButton.addActionListener(controller);
        addButton.addActionListener(controller);
        deleteButton.addActionListener(controller);
    }

    /**
     * Refresh the data provider table when the underlying data has changed.
     */
    public void refreshTable() {
        tableModel.fireTableDataChanged();
    }

    /**
     * Gets the selected table row.
     *
     * @return the selected table row
     */
    public int getSelectedTableRow() {
        return table.getSelectedRow();
    }


    /**
     * Clear table selection. Used by the preferences controller to clear the selection in this table.
     * 
     */
    public void clearTableSelection() {
        table.getSelectionModel().clearSelection();
    }

}
