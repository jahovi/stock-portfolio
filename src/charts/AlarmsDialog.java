package charts;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Stock;

/**
 * Alarms Dialogs can be opened by the user from the menu bar of a chart frame. 
 * This is where the user sets alarm prices for a stock. 
 */
public class AlarmsDialog extends JDialog implements ActionListener {
    private JButton okButton;
    private JTextField newAlarmTextField;
    private JButton addButton;
    private DefaultListModel<Double> existingAlarmsListModel;
    private JList<Double> existingAlarmsList;
    private JButton deleteButton;
    private Stock stock;
    
    private List<Alarm> alarmsToSet = new ArrayList<>();    // stock's alarm list gets set to this
    

    /**
     * Creates an alarms dialog.
     *
     * @param chartPanel the chart panel this dialog is opened from 
     * @param stock the stock for which alarms can be set with this dialog
     */
    public AlarmsDialog(ChartPanel chartPanel, Stock stock) {
        super();
        setTitle("Alarms");
        setModal(true);
        setLocationRelativeTo(chartPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.stock = stock;
        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        JPanel newAlarmPanel = createNewAlarmPanel();
        JPanel existingAlarms = createExistingAlarmsPanel();

        mainPanel.add(newAlarmPanel);
        mainPanel.add(existingAlarms);
        add(mainPanel, BorderLayout.NORTH);

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        add(okButton, BorderLayout.SOUTH);

        setResizable(false);
        pack();
        setVisible(true);

    }

    private JPanel createNewAlarmPanel() {
        JPanel newAlarmPanel = new JPanel(new GridLayout(3, 0));

        JLabel newAlarmLabel = new JLabel("New Alarm");
        newAlarmTextField = new JTextField();
        addButton = new JButton("Add");
        addButton.addActionListener(this);

        newAlarmPanel.add(newAlarmLabel);
        newAlarmPanel.add(newAlarmTextField);
        newAlarmPanel.add(addButton);

        return newAlarmPanel;
    }

    private JPanel createExistingAlarmsPanel() {
        JPanel existingAlarmsPanel = new JPanel(new GridLayout(3, 0));
        JLabel existingAlarmsLabel = new JLabel("Existing Alarms");

        existingAlarmsListModel = new DefaultListModel<>();
        for (Alarm alarm : stock.getAlarms()) {
            // set to match stock's alarm list on dialog creation
            existingAlarmsListModel.addElement(alarm.price);

        }
        alarmsToSet.addAll(stock.getAlarms());
        
        existingAlarmsList = new JList<Double>(existingAlarmsListModel);
        existingAlarmsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane existingAlarmsScrollPane = new JScrollPane(existingAlarmsList);
        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(this);

        existingAlarmsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (existingAlarmsList.getSelectedIndex() == -1) {
                    deleteButton.setEnabled(false);
                } else {
                    deleteButton.setEnabled(true);
                }
            }
        });

        existingAlarmsPanel.add(existingAlarmsLabel);
        existingAlarmsPanel.add(existingAlarmsScrollPane);
        existingAlarmsPanel.add(deleteButton);

        return existingAlarmsPanel;
    }

    /**
     * Handles the action events triggered by the user clicking on the "Add", "Delete" and "Ok" buttons in this dialog.
     *
     * @param e the ActionEvent created by the button press
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(addButton.getText())) {
            if (isDouble(newAlarmTextField.getText())) {
                // input is valid
                Alarm newAlarm = new Alarm(stock, Double.parseDouble(newAlarmTextField.getText()));
                existingAlarmsListModel.addElement(newAlarm.price);
                alarmsToSet.add(newAlarm);
            } else {
                // invalid input
                JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
            
        } else if (e.getActionCommand().equals(deleteButton.getText())) {
            alarmsToSet.remove(existingAlarmsList.getSelectedIndex());
            existingAlarmsListModel.removeElement(existingAlarmsList.getSelectedValue());
        } else if (e.getActionCommand().equals(okButton.getText())) {
            stock.setAlarms(alarmsToSet);
            dispose();
        }
    }
    
    private boolean isDouble(String s) {
        try {
            @SuppressWarnings("unused")
            double result = Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
